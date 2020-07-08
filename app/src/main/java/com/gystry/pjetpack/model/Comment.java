package com.gystry.pjetpack.model;

import java.io.Serializable;

/**
 * @author gystry
 * 创建日期：2020/7/8 10
 * 邮箱：gystry@163.com
 * 描述：
 */
public class Comment implements Serializable {
    public int id;
    public long itemId;
    public long commentId;
    public long userId;
    public int commentType;
    public long createTime;
    public int commentCount;
    public int likeCount;
    public String commentText;
    public String imageUrl;
    public String videoUrl;
    public int width;
    public int height;
    public boolean hasLiked;
    public User author;
    public Ugc ugc;
}
