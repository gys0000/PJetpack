package com.gystry.appkt.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.gystry.libcommon.PixUtils
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

/**
 * @author gystry
 * 创建日期：2021/3/30 11
 * 邮箱：gystry@163.com
 * 描述：
 */
@SuppressLint("CheckResult")
class PpImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatImageView(context, attrs, defStyleAttr) {


    companion object {
        /*
        requireAll 当为true的时候，在布局文件中必须两个参数都调用，
        才会调用这个方法，false则是有一个参数就会调用这个方法
         */
        @BindingAdapter(value = ["image_url", "isCircle"], requireAll = true)
        @JvmStatic
        public fun setImageUrl(view: PpImageView, imageUrl: String, isCircle: Boolean) {
            Glide.with(view).load(imageUrl).run {
                if (isCircle) {
                    transform(CircleCrop())
                }
                view.layoutParams?.let {
                    if (it.width > 0 && it.height > 0) {
                        override(it.width, it.height)
                    }
                }
                into(view)
            }
        }


        @BindingAdapter(value = ["image_url", "isCircle", "radius"], requireAll = false)
        @JvmStatic
        fun setImageUrl(view: PpImageView, imageUrl: String?, isCircle: Boolean, radius: Int) {
            val builder = Glide.with(view).load(imageUrl)
            if (isCircle) {
                builder.transform(CircleCrop())
            } else if (radius > 0) {
                builder.transform(RoundedCornersTransformation(PixUtils.dp2px(radius), 0))
            }
            val layoutParams = view.layoutParams
            if (layoutParams != null && layoutParams.width > 0 && layoutParams.height > 0) {
                builder.override(layoutParams.width, layoutParams.height)
            }
            builder.into(view)
        }
    }

    fun setImageUrl(imageUrl: String?) {
        setImageUrl(this, imageUrl!!, false)
    }
   open fun bind(widthPx: Int, heightPx: Int, marginLeft: Int, imgUrl: String) {
        bind(widthPx, heightPx, marginLeft, PixUtils.getScreenWidth(), PixUtils.getScreenWidth(), imgUrl)
    }

   open fun bind(widthPx: Int, heightPx: Int, marginLeft: Int, maxWidth: Int, maxHeight: Int, imgUrl: String) {
        if (widthPx <= 0 || heightPx <= 0) {
            Glide.with(this).load(imgUrl).into(object : SimpleTarget<Drawable>() {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    val intrinsicHeight = resource.intrinsicHeight
                    val intrinsicWidth = resource.intrinsicWidth
                    setSize(intrinsicWidth, intrinsicHeight, marginLeft, maxWidth, maxHeight)
                    setImageDrawable(resource)
                }
            })
            return@bind
        }
        setSize(widthPx, heightPx, marginLeft, maxWidth, maxHeight)
        setImageUrl(this, imgUrl, false)
    }

    private fun setSize(intrinsicWidth: Int, intrinsicHeight: Int, marginLeft: Int, maxWidth: Int, maxHeight: Int) {
        val finalWidth: Int
        val finalHeight: Int
        if (width > height) {
            finalWidth = maxWidth
            finalHeight = (height / (width * 1.0f / finalWidth)).toInt()
        } else {
            finalHeight = maxHeight
            finalWidth = (width / (height * 1.0f / finalHeight)).toInt()
        }

        val params = layoutParams
        layoutParams = params.apply {
            width = finalWidth
            height = finalHeight
            if (this is FrameLayout.LayoutParams) {
                leftMargin = if (height > width) PixUtils.dp2px(marginLeft) else 0
            } else if (this is LinearLayout.LayoutParams) {
                leftMargin = if (height > width) PixUtils.dp2px(marginLeft) else 0
            }
        }
    }

    fun setBlurImageUrl(coverUrl: String?, radius: Int) {
        Glide.with(this).load(coverUrl).override(50)
                .transform(BlurTransformation())
                .into(object : SimpleTarget<Drawable?>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable?>?) {
                        background = resource
                    }
                })
    }

}