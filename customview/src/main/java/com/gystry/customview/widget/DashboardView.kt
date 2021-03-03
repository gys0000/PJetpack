package com.gystry.customview.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

/**
 * @author gystry
 * 创建日期：2021/2/19 11
 * 邮箱：gystry@163.com
 * 描述：
 */
class DashboardView@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context) {

    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val dash = Path()

    init {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = STROKE_WIDTH

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawArc(
                width / 2f - RADIUS, height / 2f - RADIUS,
                width / 2f + RADIUS, height / 2f + RADIUS, (90 + OPEN_ANGLE / 2).toFloat(), (360 - OPEN_ANGLE).toFloat(), false, paint)

    }

}