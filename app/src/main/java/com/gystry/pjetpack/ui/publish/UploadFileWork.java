package com.gystry.pjetpack.ui.publish;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.gystry.libcommon.FileUploadManager;

/**
 * @author gystry
 * 创建日期：2020/8/3 17
 * 邮箱：gystry@163.com
 * 描述：
 */
public class UploadFileWork extends Worker {

    public UploadFileWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    /**
     * 做具体的上传等工作
     *
     * @return
     */
    @NonNull
    @Override
    public Result doWork() {
        Data inputData = getInputData();
        String filePath = inputData.getString("filePath");
        String fileUrl = FileUploadManager.upload(filePath);
        if (TextUtils.isEmpty(fileUrl)) {
            return Result.failure();
        } else {
            Data outputData = new Data.Builder().putString("fileUrl", fileUrl)
                    .build();
            return Result.success(outputData);
        }
    }
}
