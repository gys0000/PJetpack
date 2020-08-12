package com.gystry.pjetpack.ui.publish;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.gystry.libcommon.PixUtils;
import com.gystry.libnetwork.ApiResponse;
import com.gystry.libnetwork.ApiService;
import com.gystry.libnetwork.JsonCallback;
import com.gystry.pjetpack.R;
import com.gystry.pjetpack.model.TagList;
import com.gystry.pjetpack.ui.login.UserManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gystry
 * 创建日期：2020/8/1 17
 * 邮箱：gystry@163.com
 * 描述：
 */
public class TagBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private RecyclerView recyclerView;
    private List<TagList> mTagLists = new ArrayList<>();
    private OnTagItemSelectedListener onTagItemSelectedListener;
    private TagsAdapter tagsAdapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_tag_bottom_sheet_dialog, null, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        tagsAdapter = new TagsAdapter();
        recyclerView.setAdapter(tagsAdapter);

        dialog.setContentView(view);

        ViewGroup parent = (ViewGroup) view.getParent();
        BottomSheetBehavior<View> sheetBehavior = BottomSheetBehavior.from(parent);
        //设置默认展开的高度
        sheetBehavior.setPeekHeight(PixUtils.getScreenHeight() / 3);
        //设置往下滑动的时候不会收缩为0，只能收缩到设置的默认展开高度  true则相反
        sheetBehavior.setHideable(false);

        ViewGroup.LayoutParams layoutParams = parent.getLayoutParams();
        layoutParams.height = PixUtils.getScreenHeight() / 3 * 2;
        parent.setLayoutParams(layoutParams);

        queryTagList();
        return dialog;
    }

    private void queryTagList() {
        ApiService.get("/tag/queryTagList")
                .addParams("userId", UserManager.getInstance().getUserId())
                .addParams("pageCount", 100)
                .addParams("tagId", 0).execute(new JsonCallback<List<TagList>>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onSuccess(ApiResponse<List<TagList>> response) {
                super.onSuccess(response);
                if (response.body != null) {
                    List<TagList> list = response.body;
                    mTagLists.addAll(list);
                    ArchTaskExecutor.getMainThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            tagsAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onError(ApiResponse<List<TagList>> response) {
                ArchTaskExecutor.getMainThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), response.message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    class TagsAdapter extends RecyclerView.Adapter {

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TextView textView = new TextView(parent.getContext());
            textView.setTextSize(13);
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            textView.setTextColor(ContextCompat.getColor(parent.getContext(), R.color.color_000));
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setLayoutParams(new RecyclerView.LayoutParams(-1, PixUtils.dp2px(45)));

            return new RecyclerView.ViewHolder(textView) {
            };
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            TextView textView = (TextView) holder.itemView;
            TagList tagList = mTagLists.get(position);
            textView.setText(tagList.title);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onTagItemSelectedListener != null) {
                        onTagItemSelectedListener.onTagItemSelected(tagList);
                        dismiss();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mTagLists.size();
        }
    }

    public void setOnTagItemSelectedListener(OnTagItemSelectedListener listener) {
        onTagItemSelectedListener = listener;
    }

    public interface OnTagItemSelectedListener {
        void onTagItemSelected(TagList tagList);
    }
}
