package com.gystry.pjetpack.model;

import androidx.annotation.Nullable;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.gystry.pjetpack.BR;

import java.io.Serializable;

/**
 * @author gystry
 * 创建日期：2020/7/8 10
 * 邮箱：gystry@163.com
 * 描述：
 */
public class Ugc extends BaseObservable implements Serializable {
    public int likeCount;
    public int shareCount;
    public int commentCount;
    public boolean hasFavorite;
    public boolean hasLiked;
    public boolean hasdiss;
    public boolean hasDissed;



    @Bindable
    public int getShareCount() {
        return shareCount;
    }

    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
    }

    @Bindable
    public boolean isHasdiss() {
        return hasdiss;
    }

    public void setHasdiss(boolean hasdiss) {
        if (this.hasdiss == hasdiss) {
            return;
        }
        if (hasdiss) {
            setHasLiked(false);
        }
        this.hasdiss = hasdiss;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    @Bindable
    public boolean isHasLiked() {
        return hasLiked;
    }

    public void setHasLiked(boolean hasLiked) {
        if (this.hasLiked == hasLiked) {
            return;
        }
        if (hasLiked) {
            likeCount += 1;
            setHasdiss(false);
        } else {
            likeCount -= 1;
        }
        this.hasLiked = hasLiked;
        notifyPropertyChanged(BR._all);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || !(obj instanceof Ugc))
            return false;
        Ugc newUgc = (Ugc) obj;
        return likeCount == newUgc.likeCount
                && shareCount == newUgc.shareCount
                && commentCount == newUgc.commentCount
                && hasFavorite == newUgc.hasFavorite
                && hasLiked == newUgc.hasLiked
                && hasdiss == newUgc.hasdiss;
    }
}
