package com.gystry.pjetpack.model;

import androidx.annotation.Nullable;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import java.io.Serializable;

/**
 * @author gystry
 * 创建日期：2020/7/8 10
 * 邮箱：gystry@163.com
 * 描述：
 */
public class Comment extends BaseObservable implements Serializable {
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

    @Bindable
    public Ugc getUgc() {
        if (ugc == null) {
            ugc = new Ugc();
        }
        return ugc;
    }

    public void setUgc(Ugc ugc) {
        this.ugc = ugc;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || !(obj instanceof Comment))
            return false;

        Comment newComment = (Comment) obj;
        return likeCount == newComment.likeCount
                && hasLiked == newComment.hasLiked
                && (author != null && author.equals(newComment.author))
                && (ugc != null && ugc.equals(newComment.ugc));
    }

}
