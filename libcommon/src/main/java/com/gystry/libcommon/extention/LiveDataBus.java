package com.gystry.libcommon.extention;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gystry
 * 创建日期：2020/7/22 15
 * 邮箱：gystry@163.com
 * 描述：livedata实现的事件总线
 */
public class LiveDataBus {
    private ConcurrentHashMap<String, StickyLiveData> mHashMap = new ConcurrentHashMap<>();

    private static class Lazy {
        static LiveDataBus sLiveDataBus = new LiveDataBus();
    }

    public static LiveDataBus getInstance() {
        return Lazy.sLiveDataBus;
    }

    public StickyLiveData with(String eventName) {
        StickyLiveData stickyLiveData = mHashMap.get(eventName);
        if (stickyLiveData == null) {
            stickyLiveData = new StickyLiveData(eventName);
            mHashMap.put(eventName, stickyLiveData);
        }
        return stickyLiveData;
    }


    public class StickyLiveData<T> extends LiveData<T> {
        private String mEventName;
        private T mStickyData;
        private int mVersion = 0;

        public StickyLiveData(String mEventName) {

            this.mEventName = mEventName;
        }

        @Override
        public void setValue(T value) {
            mVersion++;
            super.setValue(value);
        }

        @Override
        public void postValue(T value) {
            mVersion++;
            super.postValue(value);
        }

        public void setStickyData(T stickyData) {
            this.mStickyData = stickyData;
            setValue(stickyData);
        }

        public void postStickyData(T stickyData) {
            this.mStickyData = stickyData;
            postValue(stickyData);
        }

        @Override
        public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
            observerSticky(owner, observer, false);
        }

        private void observerSticky(final LifecycleOwner owner, Observer<? super T> observer, boolean sticky) {
            super.observe(owner, new WrapperObserver(this, observer, sticky));
            owner.getLifecycle().addObserver(new LifecycleEventObserver() {
                @Override
                public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                    if (event == Lifecycle.Event.ON_DESTROY) {
                        mHashMap.remove(mEventName);
                    }
                }
            });
        }

        private class WrapperObserver<T> implements Observer<T> {


            private StickyLiveData<T> tStickyLiveData;
            private Observer<T> observer;
            private boolean sticky;

            private int mLastVersion = 0;

            public WrapperObserver(StickyLiveData<T> tStickyLiveData, Observer<T> observer, boolean sticky) {

                this.tStickyLiveData = tStickyLiveData;
                this.observer = observer;
                this.sticky = sticky;

                mLastVersion = tStickyLiveData.mVersion;
            }

            @Override
            public void onChanged(T o) {
                if (mLastVersion >= tStickyLiveData.mVersion) {
                    if (sticky && tStickyLiveData.mStickyData != null) {
                        observer.onChanged(tStickyLiveData.mStickyData);
                    }
                    return;
                }
                mLastVersion = tStickyLiveData.mVersion;
                observer.onChanged(o);
            }
        }
    }
}
