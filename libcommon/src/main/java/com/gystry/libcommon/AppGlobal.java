package com.gystry.libcommon;

import android.app.Application;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author gystry
 * 创建日期：2020/6/23 17
 * 邮箱：gystry@163.com
 * 描述：
 */
public class AppGlobal {
    private static Application application;

    public static Application getApplication(){
        if (application==null) {
            try {
                Method currentApplication = Class.forName("android.app.ActivityThread").getDeclaredMethod("currentApplication");
                application = (Application) currentApplication.invoke(null, (Object[]) null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return application;
    }
}
