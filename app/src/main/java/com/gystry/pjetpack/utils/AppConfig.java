package com.gystry.pjetpack.utils;

import android.content.res.AssetManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.gystry.pjetpack.model.Destination;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * @author gystry
 * 创建日期：2020/6/23 16
 * 邮箱：gystry@163.com
 * 描述：
 */
public class AppConfig {

    private static HashMap<String, Destination> sDestConfig;

    public static HashMap<String, Destination> getDestConfig() {
        if (sDestConfig == null) {
            String s = parseFile("destnation.json");
            sDestConfig = JSONObject.parseObject(s,new TypeReference<HashMap<String,Destination>>(){}.getType());
        }
        return sDestConfig;
    }

    private static String parseFile(String fileName) {
        AssetManager assets = AppGlobal.getApplication().getResources().getAssets();
        StringBuffer buffer = new StringBuffer();
        try (InputStream inputStream = assets.open(fileName);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }
}
