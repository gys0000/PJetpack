package com.gystry.pjetpack.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.gystry.libcommon.PixUtils;

/**
 * @author gystry
 * 创建日期：2020/7/8 10
 * 邮箱：gystry@163.com
 * 描述：
 */
public class PpImageView extends androidx.appcompat.widget.AppCompatImageView {
    public PpImageView(Context context) {
        super(context);
    }

    public PpImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PpImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @BindingAdapter(value = {"image_url", "isCircle"}, requireAll = true)
    public static void setImageUrl(PpImageView view, String imageUrl, boolean isCircle) {
        final RequestBuilder<Drawable> builder = Glide.with(view).load(imageUrl);
        if (isCircle) {
            builder.transform(new CircleCrop());
        }
        final ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams != null && layoutParams.width > 0 && layoutParams.height > 0) {
            builder.override(layoutParams.width, layoutParams.height);
        }
        builder.into(view);
    }

    public void bind(int widthPx, int heightPx, int marginLeft, String imgUrl){
        bind(widthPx,heightPx,marginLeft,PixUtils.getScreenWidth(),PixUtils.getScreenWidth(),imgUrl);
    }

    public void bind(int widthPx, int heightPx, int marginLeft, int maxWidth, int maxHeight, String imgUrl) {
        if (widthPx <= 0 || heightPx <= 0) {
            Glide.with(this).load(imgUrl).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    int height = resource.getIntrinsicHeight();
                    int width = resource.getIntrinsicWidth();
                    setSize(width, height, marginLeft, maxWidth, maxHeight);

                    setImageDrawable(resource);
                }
            });
            return;
        }
        setSize(widthPx,heightPx,marginLeft,maxWidth,maxHeight);
        setImageUrl(this,imgUrl,false);
    }

    private void setSize(int width, int height, int marginLeft, int maxWidth, int maxHeight) {
        int finalWidth, finalHeight;
        if (width > height) {
            finalWidth = maxWidth;
            finalHeight = (int) (height / (width * 1.0f / finalWidth));
        } else {
            finalHeight = maxHeight;
            finalWidth = (int) (width / (height * 1.0f / finalHeight));
        }
        ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(finalWidth, finalHeight);
        layoutParams.leftMargin = height > width ? PixUtils.dp2px(marginLeft) : 0;
        setLayoutParams(layoutParams);
    }

}
