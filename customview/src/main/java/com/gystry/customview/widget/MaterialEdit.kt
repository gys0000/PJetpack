package com.gystry.customview.widget

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatEditText

/**
 * @author gystry
 * 创建日期：2021/2/24 15
 * 邮箱：gystry@163.com
 * 描述：
 */
class MaterialEdit @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatEditText(context, attrs, defStyleAttr) {
    var floatingLabelFraction = 0f
        set(floatingLabelFraction) {
            field = floatingLabelFraction
            invalidate()
        }

    private fun initView(context: Context, attrs: AttributeSet?) {}
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        Log.e("MaterialEdit", "MaterialEditText-onDraw: \${hint}")
    }

    init {
        initView(context, attrs)
    }

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                Log.e("MaterialEdit", "MaterialEditText-afterTextChanged: " + s + ":" + this@MaterialEdit)
            }
        })
    }
}