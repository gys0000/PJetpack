package com.gystry.pjetpack.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.gystry.libcommon.PixUtils;
import com.gystry.pjetpack.R;
import com.gystry.pjetpack.exoplayer.IPlayTarget;
import com.gystry.pjetpack.exoplayer.PageListPlay;
import com.gystry.pjetpack.exoplayer.PageListPlayManager;

public class ListPlayerView extends FrameLayout implements IPlayTarget, PlayerControlView.VisibilityListener, Player.EventListener {
    protected ProgressBar bufferView;
    protected PpImageView cover, blur;
    protected ImageView playBtn;
    protected String mCategory;
    protected String mVideoUrl;
    protected boolean isPlaying;

    public ListPlayerView(@NonNull Context context) {
        this(context, null);
    }

    public ListPlayerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListPlayerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);

    }

    public ListPlayerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        LayoutInflater.from(context).inflate(R.layout.layout_player_view, this, true);
        bufferView = findViewById(R.id.buffer_view);
        cover = findViewById(R.id.cover);
        blur = findViewById(R.id.blur_background);
        playBtn = findViewById(R.id.play_btn);

        playBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    inActive();
                } else {
                    onActive();
                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        PageListPlay pageListPlay = PageListPlayManager.get(mCategory);
        pageListPlay.controlView.show();
        return true;
    }

    public void bindData(String category, int widthPx, int heightPx, String coverUrl, String videoUrl) {
        mCategory = category;
        mVideoUrl = videoUrl;

        cover.setImageUrl(cover, coverUrl, false);

        if (widthPx < heightPx) {
            blur.setBlurImageUrl(coverUrl, 10);
            blur.setVisibility(VISIBLE);
        } else {
            blur.setVisibility(INVISIBLE);
        }

        setSize(widthPx, heightPx);
    }

    protected void setSize(int widthPx, int heightPx) {
        int maxWidth = PixUtils.getScreenWidth();
        int maxHeight = maxWidth;

        int layoutWidth = maxWidth;
        int layoutHeight = 0;

        int coverWidth;
        int coverHeight;

        if (widthPx >= heightPx) {
            coverWidth = maxWidth;
            layoutHeight = coverHeight = (int) (heightPx / (widthPx * 1.0f / maxWidth));
        } else {
            layoutHeight = coverHeight = maxHeight;
            coverWidth = (int) (widthPx / (heightPx * 1.0f / maxHeight));
        }
        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = layoutWidth;
        params.height = layoutHeight;
        setLayoutParams(params);

        ViewGroup.LayoutParams blurParams = blur.getLayoutParams();
        blurParams.width = layoutWidth;
        blurParams.height = layoutHeight;
        blur.setLayoutParams(blurParams);

        FrameLayout.LayoutParams coverParams = (LayoutParams) cover.getLayoutParams();
        coverParams.width = coverWidth;
        coverParams.height = coverHeight;
        coverParams.gravity = Gravity.CENTER;
        cover.setLayoutParams(coverParams);

        FrameLayout.LayoutParams playBtnParams = (LayoutParams) playBtn.getLayoutParams();
        playBtnParams.gravity = Gravity.CENTER;
        playBtn.setLayoutParams(playBtnParams);
    }

    @Override
    public ViewGroup getOwner() {
        return this;
    }

    @Override
    public void onActive() {
        PageListPlay pageListPlay = PageListPlayManager.get(mCategory);
        PlayerView playerView = pageListPlay.playerView;
        PlayerControlView controlView = pageListPlay.controlView;
        ExoPlayer exoPlayer = pageListPlay.exoPlayer;

        if (playerView==null) {
            return;
        }
pageListPlay.switchPlayerView(playerView);
        ViewParent parent = playerView.getParent();
        //将playview添加到容器中
        if (parent != this) {
            if (parent != null) {
                ((ViewGroup) parent).removeView(playerView);
                //还应该暂停掉列表上正在播放的那个
                ((ListPlayerView) parent).inActive();
            }
            ViewGroup.LayoutParams layoutParams = cover.getLayoutParams();
            this.addView(playerView, 1, layoutParams);
        }
        ViewParent controlParent = controlView.getParent();
        //将playview添加到容器中
        if (controlParent != this) {
            if (controlParent != null) {
                ((ViewGroup) controlParent).removeView(controlView);
            }
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.BOTTOM;
            this.addView(controlView, layoutParams);
            controlView.setVisibilityListener(this);
        }
        controlView.show();

        if (TextUtils.equals(pageListPlay.playUrl, mVideoUrl)) {
        } else {
            MediaSource mediaSource = PageListPlayManager.createMediaSource(mVideoUrl);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setRepeatMode(Player.REPEAT_MODE_ONE);
            exoPlayer.addListener(this);
        }

        exoPlayer.setPlayWhenReady(true);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isPlaying = false;
        bufferView.setVisibility(GONE);
        cover.setVisibility(VISIBLE);
        playBtn.setVisibility(VISIBLE);
        playBtn.setImageResource(R.drawable.icon_video_play);
    }

    @Override
    public void inActive() {
        PageListPlay pageListPlay = PageListPlayManager.get(mCategory);
        pageListPlay.exoPlayer.setPlayWhenReady(false);
        playBtn.setVisibility(VISIBLE);
        playBtn.setImageResource(R.drawable.icon_video_play);
    }

    @Override
    public boolean isPlaying() {
        return isPlaying;
    }

    @Override
    public void onVisibilityChange(int visibility) {
        playBtn.setVisibility(visibility);
        playBtn.setImageResource(isPlaying ? R.drawable.icon_video_pause : R.drawable.icon_video_play);
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        PageListPlay pageListPlay = PageListPlayManager.get(mCategory);
        ExoPlayer exoPlayer = pageListPlay.exoPlayer;
        if (playbackState == Player.STATE_READY && exoPlayer.getBufferedPosition() != 0) {
            cover.setVisibility(INVISIBLE);
            bufferView.setVisibility(INVISIBLE);
        } else if (playbackState == Player.STATE_BUFFERING) {
            bufferView.setVisibility(VISIBLE);
        }

        isPlaying = playbackState == Player.STATE_READY && exoPlayer.getBufferedPosition() != 0 && playWhenReady;
        playBtn.setImageResource(isPlaying ? R.drawable.icon_video_pause : R.drawable.icon_video_play);
    }
}
