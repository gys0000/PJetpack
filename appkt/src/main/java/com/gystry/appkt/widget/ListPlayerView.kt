package com.gystry.appkt.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import com.gystry.appkt.R
import com.gystry.appkt.widget.PpImageView.Companion.setImageUrl
import com.gystry.libcommon.PixUtils
import kotlinx.android.synthetic.main.layout_player_view.view.*

/**
 * @author gystry
 * 创建日期：2021/3/30 16
 * 邮箱：gystry@163.com
 * 描述：
 */
class ListPlayerView(context: Context, attrs: AttributeSet? = null, def: Int = 0) : FrameLayout(context, attrs, def) {

    private var mCategory: String? = null
    private var mVideoUrl: String? = null
    private var mWidthPx: Int = 0
    private var mHeightPx: Int = 0
    private val cover: PpImageView by lazy { findViewById<PpImageView>(R.id.cover) }

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_player_view, this, true)

    }

    fun binData(category: String, widthPx: Int, heightPx: Int, coverUrl: String, videoUrl: String) {
        mCategory = category
        mVideoUrl = videoUrl
        mWidthPx = widthPx
        mHeightPx = heightPx
        setImageUrl(cover, coverUrl, false)
        if (widthPx < heightPx) {
            blur_background.setBlurImageUrl(coverUrl, 10)
            blur_background.visibility= VISIBLE
        }else{
            blur_background.visibility= INVISIBLE
        }
        setSize(widthPx, heightPx)
    }

    private fun setSize(widthPx: Int, heightPx: Int) {
        val maxWidth = PixUtils.getScreenWidth()

        var layoutHeight = 0

        val coverWidth: Int
        val coverHeight: Int

        if (widthPx >= heightPx) {
            coverWidth = maxWidth
            coverHeight = (heightPx / (widthPx * 1.0f / maxWidth)).toInt()
            layoutHeight = coverHeight
        } else {
            coverHeight = maxWidth
            layoutHeight = coverHeight
            coverWidth = (widthPx / (heightPx * 1.0f / maxWidth)).toInt()
        }
        val params = layoutParams
        params.width = maxWidth
        params.height = layoutHeight
        layoutParams = params

        val blurParams: ViewGroup.LayoutParams = blur_background.layoutParams
        blurParams.width = maxWidth
        blurParams.height = layoutHeight
        blur_background.layoutParams = blurParams

        val coverParams = cover.layoutParams as LayoutParams
        coverParams.width = coverWidth
        coverParams.height = coverHeight
        coverParams.gravity = Gravity.CENTER
        cover.layoutParams = coverParams

        val playBtnParams = play_btn.layoutParams as LayoutParams
        playBtnParams.gravity = Gravity.CENTER
        play_btn.layoutParams = playBtnParams
    }
}