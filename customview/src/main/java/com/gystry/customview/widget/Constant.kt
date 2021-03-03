package com.gystry.customview.widget

import com.gystry.libcommon.utils.dp2Px
import jp.wasabeef.glide.transformations.internal.Utils

/**
 * @author gystry
 * 创建日期：2021/2/19 11
 * 邮箱：gystry@163.com
 * 描述：
 */

//改为带@JvmField注解就可以了，此注解表示编译时不会给GET_CODE_URL生成getter和setter，仅作为常数使用
// 不使用@JvmField也不会报错，但是它将在底层生成访问该变量的不必要的对象和getter。[Where Should I Keep My Constants in Kotlin?](https://blog.egorand.me/where-do-i-put-my-constants-in-kotlin/)
@JvmField
val RADIUS: Float = dp2Px(150f)

@JvmField
val STROKE_WIDTH = dp2Px(3f)

val TEXT_SIZE = dp2Px(12f)
val TEXT_MARGIN = dp2Px(8f)
val VERTICAL_OFFSET = dp2Px(31f)
val HORIZONTAL_OFFSET = dp2Px(5f)
val EXTRA_OFFSET = dp2Px(16f)

const val OPEN_ANGLE = 120