package com.gystry.customview.widget

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatEditText
import com.gystry.customview.R

/**
 * @author gystry
 * 创建日期：2021/2/23 15
 * 邮箱：gystry@163.com
 * 描述：
 */
class MaterialEditText @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatEditText(context!!, attrs, defStyleAttr) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var floatingLabelShown: Boolean = false
    val animator1: ObjectAnimator by lazy { ObjectAnimator.ofFloat(this@MaterialEditText, "floatingLabelFraction", 1f) }
    var useFloatingLabel: Boolean = true
    val backgroundPaddings = Rect()
    private var floatingLabelFraction = 0f
        set(floatingLabelFraction) {
            Log.e("MaterialEditText", "setfloatingLabelFraction:-----floatingLabelShown:$floatingLabelFraction")
            field = floatingLabelFraction
            invalidate()
        }

    init {

        for (i in 0 until attrs?.attributeCount!!) {

            Log.e("MaterialEditText", "${attrs.attributeCount}-attrs.attribute------:${attrs.getAttributeName(i)}----${attrs.getAttributeValue(i)} ")
        }
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MaterialEditText)
        useFloatingLabel = typedArray.getBoolean(R.styleable.MaterialEditText_useFloatingLabel, true)
        typedArray.recycle()

        refreshPading()
        paint.textSize = TEXT_SIZE

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.e("MaterialEditText", "beforeTextChanged: $s-----floatingLabelShown:$floatingLabelShown")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.e("MaterialEditText", "onTextChanged: $s-----floatingLabelShown:$floatingLabelShown")
            }

            @SuppressLint("ObjectAnimatorBinding")
            override fun afterTextChanged(s: Editable?) {
                Log.e("MaterialEditText", "afterTextChanged: $s-----floatingLabelShown:$floatingLabelShown")
                if (TextUtils.isEmpty(s).not() && !floatingLabelShown) {
                    floatingLabelShown = true
                    animator1.start()
                } else if (TextUtils.isEmpty(s) && floatingLabelShown) {
                    floatingLabelShown = false
                    animator1.reverse()
                }
            }

        })
    }

    @JvmName("setUseFloatingLabel1")
    public fun setUseFloatingLabel(value: Boolean) {
        if (useFloatingLabel != value) {
            useFloatingLabel = value
            //设置padding，会自动触发测量
            refreshPading()
        }
//        requestLayout()//调用此方法，重新测量布局
    }

    private fun refreshPading() {
        Log.e("MaterialEditText", "refreshPading: $background")
        background.getPadding(backgroundPaddings)//获取背景的padding,背景padding的内边距是固定的，不会增加减小
        if (useFloatingLabel) {
            setPadding(backgroundPaddings.left, (backgroundPaddings.top + TEXT_SIZE + TEXT_MARGIN).toInt(), backgroundPaddings.right, backgroundPaddings.bottom)
        } else {
            setPadding(backgroundPaddings.left, (backgroundPaddings.top).toInt(), backgroundPaddings.right, backgroundPaddings.bottom)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        Log.e("MaterialEditText", "onDraw: ${hint}")
        if (useFloatingLabel) {
            paint.alpha = (floatingLabelFraction * 0xff).toInt()
            val extraOffset = -EXTRA_OFFSET * floatingLabelFraction
            canvas?.drawText(hint.toString(), HORIZONTAL_OFFSET, VERTICAL_OFFSET + extraOffset, paint)
        }
    }
}