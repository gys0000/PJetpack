package com.gystry.libcommon;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.RequiresApi;
import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author gystry
 * 创建日期：2020/7/27 15
 * 邮箱：gystry@163.com
 * 描述：
 */
public class FileUtils {
    @SuppressLint("RestrictedApi")
    public static LiveData<String> generateVideoCover(final String filepath) {
        final MutableLiveData<String> liveData = new MutableLiveData<>();

        ArchTaskExecutor.getIOThreadExecutor().execute(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void run() {
                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                mediaMetadataRetriever.setDataSource(filepath);
                Bitmap frame = mediaMetadataRetriever.getFrameAtIndex(-1);
                if (frame != null) {
                    byte[] bytes = compressBitmap(frame, 200);//压缩到200KB以下
                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), System.currentTimeMillis() + ".jpeg");
                    try {
                        file.createNewFile();
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        fileOutputStream.write(bytes);
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        fileOutputStream = null;
                        liveData.postValue(file.getAbsolutePath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    liveData.postValue(null);
                }
            }
        });
        return liveData;
    }

    private static byte[] compressBitmap(Bitmap frame, int i) {
        if (frame != null && i > 0) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int options = 100;
            frame.compress(Bitmap.CompressFormat.JPEG, options, baos);
            while (baos.toByteArray().length > i * 1024) {
                baos.reset();
                options -= 5;
                frame.compress(Bitmap.CompressFormat.JPEG, options, baos);
            }

            byte[] bytes = baos.toByteArray();
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                baos = null;
            }
            return bytes;
        }
        return null;
    }
}
