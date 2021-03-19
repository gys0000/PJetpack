package com.gystry.pjetpack.ui.detail;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;

import com.gystry.libcommon.AppGlobal;
import com.gystry.libcommon.FileUploadManager;
import com.gystry.libcommon.FileUtils;
import com.gystry.libcommon.LoadingDialog;
import com.gystry.libnetworkkt.ApiResponse;
import com.gystry.libnetworkkt.ApiService;
import com.gystry.libnetworkkt.JsonCallback;
import com.gystry.pjetpack.R;
import com.gystry.pjetpack.databinding.LayoutCommentDialogBinding;
import com.gystry.pjetpack.model.Comment;
import com.gystry.pjetpack.ui.login.UserManager;
import com.gystry.pjetpack.ui.publish.CaptureActivity;

import java.util.concurrent.atomic.AtomicInteger;

public class CommentDialog extends DialogFragment implements View.OnClickListener {
    private LayoutCommentDialogBinding mBinding;
    private long itemId;
    private CommentAddListener listener;
    public static final String KEY_ITEM_ID = "key_item_id";
    private String filePath;
    private int width;
    private int height;
    private boolean isVideo;
    private LoadingDialog loadingDialog;
    private String fileUrl;
    private String coverUrl;

    public static CommentDialog newInstance(long itemId) {

        Bundle args = new Bundle();
        args.putLong(KEY_ITEM_ID, itemId);
        CommentDialog fragment = new CommentDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = LayoutCommentDialogBinding.inflate(inflater, container, false);
        mBinding.commentVideo.setOnClickListener(this);
        mBinding.commentDelete.setOnClickListener(this);
        mBinding.commentSend.setOnClickListener(this);

        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        itemId = getArguments().getLong(KEY_ITEM_ID);
        return mBinding.getRoot();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.comment_send) {
            publishComment();
        } else if (v.getId() == R.id.comment_video) {
            CaptureActivity.startActivityForResult(getActivity());
        } else if (v.getId() == R.id.comment_delete) {
            filePath = null;
            isVideo = false;
            width = 0;
            height = 0;
            mBinding.commentCover.setImageDrawable(null);
            mBinding.commentExtLayout.setVisibility(View.GONE);
            mBinding.commentVideo.setEnabled(true);
            mBinding.commentVideo.setAlpha(100);
        }
    }

    private void publishComment() {

        if (TextUtils.isEmpty(mBinding.inputView.getText())) {
            return;
        }

        if (isVideo&& TextUtils.isEmpty(filePath)) {
            FileUtils.generateVideoCover(filePath).observe(this, new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    uploadFile(s,filePath);
                }
            });
        }else if (!TextUtils.isEmpty(filePath)){
            uploadFile(null,filePath);
        }else {
            publish();
        }
    }

    /**
     * 上传文件到阿里云oss
     * @param coverPath
     * @param filePath
     */
    @SuppressLint("RestrictedApi")
    private void uploadFile(String coverPath, String filePath) {
        showLoadingDialog();
        AtomicInteger count = new AtomicInteger(1);
        if (!TextUtils.isEmpty(coverPath)) {
            count.set(2);
            ArchTaskExecutor.getIOThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    int remain = count.decrementAndGet();
                    coverUrl = FileUploadManager.upload(coverPath);
                    if (remain<=0) {
                        if (!TextUtils.isEmpty(fileUrl)&& !TextUtils.isEmpty(coverUrl)) {
                            publish();
                        }else {
                            dismissLoadingDialog();
                            showToast(getString(R.string.file_upload_failed));
                        }
                    }
                }
            });

            ArchTaskExecutor.getIOThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    int remain = count.decrementAndGet();
                    fileUrl = FileUploadManager.upload(filePath);

                    if (remain<=0) {
                        if (!TextUtils.isEmpty(fileUrl)||!TextUtils.isEmpty(coverPath)&& !TextUtils.isEmpty(coverUrl)) {
                            publish();
                        }else {
                            dismissLoadingDialog();
                            showToast(getString(R.string.file_upload_failed));
                        }
                    }
                }
            });
        }
    }

    private void publish() {
        String commentText = mBinding.inputView.getText().toString();
        ApiService.INSTANCE.<Comment>post("/comment/addComment")
                .addParams("userId", UserManager.getInstance().getUserId())
                .addParams("itemId", itemId)
                .addParams("commentText", commentText)
                .addParams("image_url", isVideo?coverUrl:fileUrl)
                .addParams("video_url", fileUrl)
                .addParams("width", width)
                .addParams("height", height)
                .execute(new JsonCallback<Comment>() {
                    @Override
                    public void onSuccess(ApiResponse<Comment> response) {
                        onCommentSuccess(response.getBody());
                        dismissLoadingDialog();
                    }

                    @Override
                    public void onError(ApiResponse<Comment> response) {
                        showToast("评论失败:" + response.getMessage());
                        dismissLoadingDialog();
                    }
                });
    }

    private void showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(getContext());
            loadingDialog.setLoadingText(getString(R.string.upload_text));
            loadingDialog.setCanceledOnTouchOutside(false);
            loadingDialog.setCancelable(false);
        }
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    @SuppressLint("RestrictedApi")
    private void dismissLoadingDialog() {
        if (loadingDialog != null) {
            //dismissLoadingDialog  的调用可能会出现在异步线程调用
            if (Looper.myLooper() == Looper.getMainLooper()) {
                ArchTaskExecutor.getMainThreadExecutor().execute(() -> {
                    if (loadingDialog != null && loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }
                });
            } else if (loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }
        }
    }
    private void onCommentSuccess(Comment body) {
        showToast("评论发布成功");
        if (listener != null) {
            listener.onAddComment(body);
        }
    }

    public interface CommentAddListener {
        void onAddComment(Comment comment);
    }

    public void setCommentAddListener(CommentAddListener listener) {
        this.listener = listener;
    }

    @SuppressLint("RestrictedApi")
    private void showToast(String s) {
        //showToast几个可能会出现在异步线程调用
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Toast.makeText(AppGlobal.getApplication(), s, Toast.LENGTH_SHORT).show();
        } else {
            ArchTaskExecutor.getMainThreadExecutor().execute(() -> Toast.makeText(AppGlobal.getApplication(), s, Toast.LENGTH_SHORT).show());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CaptureActivity.REQ_CAPTURE && resultCode == Activity.RESULT_OK) {
            filePath = data.getStringExtra(CaptureActivity.RESULT_FILE_PATH);
            width = data.getIntExtra(CaptureActivity.RESULT_FILE_WIDTH, 0);
            height = data.getIntExtra(CaptureActivity.RESULT_FILE_HEIGHT, 0);
            isVideo = data.getBooleanExtra(CaptureActivity.RESULT_FILE_TYPE, false);

            if (!TextUtils.isEmpty(filePath)) {
                mBinding.commentExtLayout.setVisibility(View.VISIBLE);
                mBinding.commentCover.setImageUrl(filePath);
                if (isVideo) {
                    mBinding.commentIconVideo.setVisibility(View.VISIBLE);
                }
            }
            mBinding.commentVideo.setEnabled(false);
            mBinding.commentVideo.setAlpha(50);
        }
    }
}
