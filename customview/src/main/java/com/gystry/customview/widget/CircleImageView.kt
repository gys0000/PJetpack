package com.gystry.customview.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.gystry.customview.R
import com.gystry.libcommon.utils.dp2Px
import com.gystry.libcommon.utils.drawable2Bitmap

class CircleImageView(context: Context, attrs: AttributeSet?=null, defStyleAttr: Int=0) : View(context, attrs, defStyleAttr) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rect = RectF(dp2Px(10f), dp2Px(10f), dp2Px(110f), dp2Px(110f))
    private val radius = dp2Px(110f)
    private val bitmap = drawable2Bitmap(R.mipmap.ic_huluwa, radius, getContext())


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val saveLayer = canvas?.saveLayer(rect, paint)
        canvas?.drawCircle(dp2Px(60f), dp2Px(60f), 50f, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas?.drawBitmap(bitmap, dp2Px(10f), dp2Px(10f), paint)
        paint.xfermode = null
        saveLayer?.let { canvas?.restoreToCount(it) }
    }

}