package com.gystry.pjetpack.model;

import java.io.Serializable;

/**
 * @author gystry
 * 创建日期：2020/7/8 10
 * 邮箱：gystry@163.com
 * 描述：
 */
public class Feed implements Serializable {
    public int id;
    public long itemId;
    public int itemType;
    public long createTime;
    public double duration;
    public String feeds_text;
    public long authorId;
    public String activityIcon;
    public String activityText;
    public int width;
    public int height;
    public String url;
    public String cover;

    public User author;
    public Comment topComment;
    public Ugc ugc;

}
