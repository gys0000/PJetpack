package com.gystry.pjetpack.ui.publish;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkContinuation;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.alibaba.fastjson.JSONObject;
import com.gystry.libcommon.FileUtils;
import com.gystry.libcommon.LoadingDialog;
import com.gystry.libnavannotation.ActivityDestination;
import com.gystry.libnetworkkt.ApiResponse;
import com.gystry.libnetworkkt.ApiService;
import com.gystry.libnetworkkt.JsonCallback;
import com.gystry.pjetpack.R;
import com.gystry.pjetpack.databinding.ActivityLayoutPublishBinding;
import com.gystry.pjetpack.model.Feed;
import com.gystry.pjetpack.model.TagList;
import com.gystry.pjetpack.ui.login.UserManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@ActivityDestination(pageUrl = "main/tabs/publish", asStarter = false)
public class PublishActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityLayoutPublishBinding mBinding;
    private int width;
    private int height;
    private String filePath;
    private boolean isVideo;
    private String mCoverPath;
    private UUID coverUUID;
    private UUID fileUploadUUID;
    private String coverUploadUrl;
    private String fileUploadUrl;
    private LoadingDialog mLoadingDialog;
    private TagList mTagList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_layout_publish);
        mBinding.actionClose.setOnClickListener(this);
        mBinding.actionPublish.setOnClickListener(this);
        mBinding.actionDeleteFile.setOnClickListener(this);
        mBinding.actionAddFile.setOnClickListener(this);
        mBinding.actionAddTag.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_close:
                //退出时展示退出弹窗
                showExitDialog();
                break;
            case R.id.action_publish:
                publish();
                break;
            case R.id.action_add_tag:
                TagBottomSheetDialogFragment tagBottomSheetDialogFragment = new TagBottomSheetDialogFragment();
                tagBottomSheetDialogFragment.setOnTagItemSelectedListener(new TagBottomSheetDialogFragment.OnTagItemSelectedListener() {

                    @Override
                    public void onTagItemSelected(TagList tagList) {
                        mTagList = tagList;
                        mBinding.actionAddTag.setText(tagList.title);
                    }
                });
                tagBottomSheetDialogFragment.show(getSupportFragmentManager(), "tag_dialog");
                break;
            case R.id.action_add_file:
                CaptureActivity.startActivityForResult(this);
                break;
            case R.id.action_delete_file:
                mBinding.actionAddFile.setVisibility(View.VISIBLE);
                mBinding.fileContainer.setVisibility(View.GONE);
                mBinding.cover.setImageDrawable(null);
                filePath = null;
                width = 0;
                height = 0;
                isVideo = false;
                break;
    }        }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void publish() {
        showExitDialog();
        List<OneTimeWorkRequest> workRequests = new ArrayList<>();
        if (!TextUtils.isEmpty(filePath)) {
            if (isVideo) {
                FileUtils.generateVideoCover(filePath).observe(this, new Observer<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onChanged(String s) {
                        mCoverPath = s;
                        OneTimeWorkRequest request = getOneTimeWorkRequest(s);
                        coverUUID = request.getId();
                        workRequests.add(request);

                        enqueue(workRequests);
                    }
                });
            }
            OneTimeWorkRequest request = getOneTimeWorkRequest(filePath);
            fileUploadUUID = request.getId();
            workRequests.add(request);
            if (!isVideo) {
                enqueue(workRequests);
            }
        }else {
            publishFeed();
        }
    }

    private void enqueue(List<OneTimeWorkRequest> workRequests) {
        WorkContinuation workContinuation = WorkManager.getInstance(PublishActivity.this).beginWith(workRequests);
        workContinuation.enqueue();

        workContinuation.getWorkInfosLiveData().observe(PublishActivity.this, new Observer<List<WorkInfo>>() {
            @Override
            public void onChanged(List<WorkInfo> workInfos) {
                int completeCount = 0;
                //这个方法会回调多次，block running enuqued failed success finish 每一个状态都会回调一次
                for (WorkInfo workInfo : workInfos) {
                    WorkInfo.State state = workInfo.getState();
                    Data outputData = workInfo.getOutputData();
                    UUID uuid = workInfo.getId();
                    if (state == WorkInfo.State.FAILED) {
                        if (uuid.equals(coverUUID)) {
                            showToast("封面图上传失败");
                        } else if (uuid.equals(fileUploadUUID)) {
                            showToast("原始文件上传失败");
                        }
                    } else if (state == WorkInfo.State.SUCCEEDED) {
                        String url = outputData.getString("fileUrl");
                        if (uuid.equals(coverUUID)) {
                            coverUploadUrl = url;
                        } else if (uuid.equals(fileUploadUUID)) {
                            fileUploadUrl = url;
                        }
                        completeCount++;
                    }
                }
                if (completeCount >= workInfos.size()) {
                    //全部上传完成，接着做帖子发布操作
                    publishFeed();
                }
            }
        });
    }

    private void publishFeed() {
        ApiService.INSTANCE.<JSONObject>post("/feeds/publish")
                .addParams("coverUrl", coverUploadUrl)
                .addParams("fileUrl", fileUploadUrl)
                .addParams("fileWidth", width)
                .addParams("fileHeight", height)
                .addParams("userId", UserManager.getInstance().getUserId())
                .addParams("tagId", mTagList == null ? 0 : mTagList.tagId)
                .addParams("tagTitle", mTagList == null ? "" : mTagList.title)
                .addParams("feedText", mBinding.inputView.getText().toString())
                .addParams("feedType", isVideo ? Feed.VIDEO_TYPE : Feed.IMAGE_TYPE)
                .execute(new JsonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(ApiResponse<JSONObject> response) {
                        showToast(getString(R.string.feed_publisj_success));
                        PublishActivity.this.finish();
                        dismissLoading();
                    }

                    @Override
                    public void onError(ApiResponse<JSONObject> response) {
                        showToast(response.getMessage());
                        dismissLoading();
                    }
                });
    }

    private void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
            mLoadingDialog.setLoadingText(getString(R.string.feed_publish_ing));
        }
        mLoadingDialog.show();
    }

    private void dismissLoading() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            if (mLoadingDialog != null) {
                mLoadingDialog.dismiss();
            }
        } else {
            runOnUiThread(() -> {
                if (mLoadingDialog != null) {
                    mLoadingDialog.dismiss();
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("RestrictedApi")
    private OneTimeWorkRequest getOneTimeWorkRequest(String filePath) {
        Data inputData = new Data.Builder()
                .putString("filePath", filePath)
                .build();
        @SuppressLint("RestrictedApi") Constraints constraints = new Constraints();
        //设备存储空间充足的时候，才能执行。>15%
        constraints.setRequiresStorageNotLow(true);
        //必须在指定的网络条件下才能执行   NetworkType.UNMETERED不计流量  wifi
        constraints.setRequiredNetworkType(NetworkType.UNMETERED);
        //设备的电量充足的时候执行
        constraints.setRequiresBatteryNotLow(true);
        //指定设备在充电的时候执行
        constraints.setRequiresCharging(true);
        //只有设备在空闲的情况下才能被执行 比如息屏，cpu利用率不高等
        constraints.setRequiresDeviceIdle(true);
        //WorkManager利用ContentObserver监控传递进来的uri对应的内容是否发生变化，当且仅当发生变化的时候，
        //任务才会被执行
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            constraints.setContentUriTriggers(null);
        }
        //从content变化，到任务被执行延迟的时间。如果在这期间content发生变化，延迟时间会重新计算
        constraints.setTriggerContentUpdateDelay(0);
        //设置从content变化到被执行中间最大的延迟时间
        constraints.setTriggerMaxContentDelay(0);
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(UploadFileWork.class)
                .setInputData(inputData)
                .setConstraints(constraints)
                //设置一个拦截器，在任务执行之前可以做一次拦截，去修改入参的数据然后返回新的数据交友worker使用
                .setInputMerger(null)
                //当一个任务被调度失败后，所要采取的重试策略，可以通过BackOffPolicy来执行具体的策略
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 10, TimeUnit.SECONDS)
                //任务被调度执行的延迟时间
                .setInitialDelay(10, TimeUnit.SECONDS)
                //设置重试最大次数
                .setInitialRunAttemptCount(2)
                //设置任务开始执行的时间   时间戳
                .setPeriodStartTime(0, TimeUnit.SECONDS)
                //指定该任务被调度的时间
                .setScheduleRequestedAt(0, TimeUnit.SECONDS)
                //当一个任务执行状态变成finish时，有没有持续的观察者消费这个结果，那么
                //WorkManager会在内存中保留一段时间该任务的结果，超过这个时间，这个结果会被存储到数据库中，
                //下次想要查询该任务的结果时，会触发workmaneger的数据库查询操作，可以通过uuid来查询任务的状态
                .keepResultsForAtLeast(10, TimeUnit.SECONDS)
                .build();
        return request;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CaptureActivity.REQ_CAPTURE && data != null) {
            width = data.getIntExtra(CaptureActivity.RESULT_FILE_WIDTH, 0);
            height = data.getIntExtra(CaptureActivity.RESULT_FILE_HEIGHT, 0);
            filePath = data.getStringExtra(CaptureActivity.RESULT_FILE_PATH);
            isVideo = data.getBooleanExtra(CaptureActivity.RESULT_FILE_TYPE, false);

            showFileThumbnail();
        }
    }

    private void showFileThumbnail() {
        if (TextUtils.isEmpty(filePath)) {
            return;
        }
        mBinding.actionAddFile.setVisibility(View.GONE);
        mBinding.fileContainer.setVisibility(View.VISIBLE);
        mBinding.cover.setImageUrl(filePath);
        mBinding.videoIcon.setVisibility(isVideo ? View.VISIBLE : View.GONE);
        mBinding.cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreviewActivity.startActivityForResult(PublishActivity.this, filePath, isVideo, null);
            }
        });
    }

    private void showExitDialog() {

        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.publish_exit_message))
                .setNegativeButton(getString(R.string.publish_exit_action_cancel), null)
                .setPositiveButton(getString(R.string.publish_exit_action_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        PublishActivity.this.finish();
                    }
                }).create().show();
    }

    private void showToast(String message) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(PublishActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
