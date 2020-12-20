package com.gystry.libcommon.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.TypedValue

/**
 * @author gystry
 * 创建日期：2020/12/15 20
 * 邮箱：gystry@163.com
 * 描述：
 * options 选项
 * Density 密度
 */

fun drawable2Bitmap(drawableResId: Int, width: Float, context: Context): Bitmap {
    val options = BitmapFactory.Options()
    //是否只解析图片边界
    options.inJustDecodeBounds = true
    //第一次解析  只解析出图片大小
    BitmapFactory.decodeResource(context.resources, drawableResId, options)
    //修改图片解析选项，这次设置目标bitmap的宽高
    options.inJustDecodeBounds = false
    options.inDensity = options.outWidth
    options.inTargetDensity = width.toInt()
    //第二次解析  两次解析是为了性能，第一次解析只解析出原文件图片的宽高，第二次再根据原图片的宽高和设定的宽高进行解析出目标bitmap
    return BitmapFactory.decodeResource(context.resources, drawableResId, options)
}

fun dp2Px(dpValue:Float): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpValue,Resources.getSystem().displayMetrics)
}