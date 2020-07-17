package com.gystry.pjetpack.exoplayer;

import android.view.ViewGroup;

/**
 * @author gystry
 * 创建日期：2020/7/17 13
 * 邮箱：gystry@163.com
 * 描述：
 */
public interface IPlayTarget {

    //playView的父容器
    ViewGroup getOwner();

    //活跃状态 视频可播放
    void onActive();

    //非活跃状态 暂停
    void inActive();

    boolean isPlaying();
}
