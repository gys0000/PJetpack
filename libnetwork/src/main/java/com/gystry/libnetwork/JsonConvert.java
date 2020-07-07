package com.gystry.libnetwork;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Type;

public class JsonConvert implements Convert {
    @Override
    public Object convert(String content, Type type) {
        JSONObject jsonObject = JSON.parseObject(content);
        JSONObject data = jsonObject.getJSONObject("data");
        if (data!=null) {
            Object data1 = data.get("data");
            return JSON.parseObject(data1.toString(),type);
        }
        return null;
    }

    @Override
    public Object convert(String content, Class claz) {
        JSONObject jsonObject = JSON.parseObject(content);
        JSONObject data = jsonObject.getJSONObject("data");
        if (data!=null) {
            Object data1 = data.get("data");
            return JSON.parseObject(data1.toString(),claz);
        }
        return null;
    }
}
