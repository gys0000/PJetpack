package com.gystry.pjetpack.exoplayer;

import android.app.Application;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.gystry.libcommon.AppGlobal;
import com.gystry.pjetpack.R;

/**
 * @author gystry
 * 创建日期：2020/7/17 10
 * 邮箱：gystry@163.com
 * 描述：
 */
public class PageListPlay {

    public ExoPlayer exoPlayer;
    public PlayerView playerView;
    public PlayerControlView controlView;
    public String playUrl;

    public PageListPlay() {
        Application application = AppGlobal.getApplication();
        //创建一个播放器实例
        exoPlayer = ExoPlayerFactory.newSimpleInstance(application,
                //视频每一帧的画面如何渲染，实现默认的实现类
                new DefaultRenderersFactory(application),
                //视频的音视频轨道如何加载，使用默认的轨道选择器
                new DefaultTrackSelector(),
                //视频缓存控制逻辑，使用默认的即可
                new DefaultLoadControl());
        //加载能够展示视频画面的view
        playerView = (PlayerView) LayoutInflater.from(application).inflate(R.layout.layout_exo_player_view, null, false);
        //加载视频播放控制器
        controlView = (PlayerControlView) LayoutInflater.from(application).inflate(R.layout.layout_exo_player_contorller_view, null, false);

        playerView.setPlayer(exoPlayer);
        controlView.setPlayer(exoPlayer);
    }

    public void release() {
        if (exoPlayer != null) {
            exoPlayer.setPlayWhenReady(false);
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
        if (playerView != null) {
            playerView.setPlayer(null);
            playerView = null;
        }
        if (controlView != null) {
            controlView.setPlayer(null);
            controlView.setVisibilityListener(null);
            controlView = null;
        }
    }
}
