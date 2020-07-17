package com.gystry.pjetpack.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gystry.libcommon.PixUtils;
import com.gystry.libcommon.RoundFramLayout;
import com.gystry.libcommon.ViewHelper;
import com.gystry.pjetpack.R;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * @author gystry
 * 创建日期：2020/7/16 16
 * 邮箱：gystry@163.com
 * 描述：
 */
public class ShareDialog extends AlertDialog {
    List<ResolveInfo> shareItems = new ArrayList<>();
    private Adapter adapter;
    private String shareContent;
    private View.OnClickListener listener;

    public ShareDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        RoundFramLayout frameLayout = new RoundFramLayout(getContext());
        frameLayout.setViewOutLine(PixUtils.dp2px(20), ViewHelper.RADIUS_TOP);
        RecyclerView gridView = new RecyclerView(getContext());
        gridView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        adapter = new Adapter();
        gridView.setAdapter(adapter);
        final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int margin = PixUtils.dp2px(20);
        params.leftMargin = params.bottomMargin = params.rightMargin = params.topMargin = margin;
        params.gravity=Gravity.CENTER;
        frameLayout.addView(gridView, params);

        setContentView(frameLayout);
        getWindow().setGravity(Gravity.BOTTOM);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        queryShareItem();
    }

    public void setShareContent(String shareContent) {
        this.shareContent = shareContent;
    }

    public void setShareItemClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    private void queryShareItem() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");

        //查询出所有支持文本分享的应用
        List<ResolveInfo> resolveInfos = getContext().getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String packageName = resolveInfo.activityInfo.packageName;
            if (TextUtils.equals(packageName, "com.tencent.mm") || TextUtils.equals(packageName, "com.tencent.mobileqq")) {
                shareItems.add(resolveInfo);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private final PackageManager packageManager;

        public Adapter() {
            packageManager = getContext().getPackageManager();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_share_item, parent, false);
            return new RecyclerView.ViewHolder(view) {
            };
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            final ResolveInfo resolveInfo = shareItems.get(position);
            final ImageView shareIcon = (ImageView) holder.itemView.findViewById(R.id.share_icon);
            final Drawable icon = resolveInfo.loadIcon(packageManager);
            shareIcon.setImageDrawable(icon);
            final TextView shareName = holder.itemView.findViewById(R.id.share_text);
            shareName.setText(resolveInfo.loadLabel(packageManager));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String packageName = resolveInfo.activityInfo.packageName;
                    final String cls = resolveInfo.activityInfo.name;
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.setComponent(new ComponentName(packageName, cls));
                    intent.putExtra(Intent.EXTRA_TEXT, shareContent);
                    getContext().startActivity(intent);

                    if (listener != null) {
                        listener.onClick(v);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return shareItems == null ? 0 : shareItems.size();
        }

    }
}
