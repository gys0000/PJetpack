package com.gystry.customview.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.gystry.customview.R
import com.gystry.libcommon.utils.dp2Px
import com.gystry.libcommon.utils.drawable2Bitmap

/**
 * 在Kotlin中@JvmOverloads注解的作用就是：在有默认参数值的方法中使用@JvmOverloads注解，则Kotlin就会暴露多个重载方法。
在 Kotlin 中调用默认参数值的方法或者构造函数是完全没问题的，但是在 Java 代码调用相应 Kotlin 代码却不行，也就是，Java 代码不能调用在 Kotlin 中使用默认值实现的重载函数或构造函数。
@JvmOverloads 就是解决这一问题的，从命名 —— “Jvm 重载”
class CircleImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context)
class CircleImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context!!, attrs, defStyleAttr)
后边一个是可以调用xml中的属性值

@JvmOverloads fun f(a: String, b: Int=0, c:String="abc"){
}
👇👇👇

void f(String a)
void f(String a, int b)
void f(String a, int b, String c)
 */
class CircleImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context) {
    private var paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var rect: RectF = RectF(dp2Px(10f), dp2Px(10f), dp2Px(310f), dp2Px(310f))
    private var radius = dp2Px(150f)
    private var bitmap = drawable2Bitmap(R.mipmap.ic_huluwa, dp2Px(300f), getContext())

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val saveLayer = canvas?.saveLayer(rect, paint)
        canvas?.drawCircle(dp2Px(160f), dp2Px(160f), radius, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas?.drawBitmap(bitmap, dp2Px(10f), dp2Px(10f), paint)
        paint.xfermode = null
        saveLayer?.let {
            canvas.restoreToCount(it)
        }
    }
}
