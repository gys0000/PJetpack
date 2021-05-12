package com.gystry.designmodes.dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author gystry
 * 创建日期：2021/5/11 15
 * 邮箱：gystry@163.com
 * 描述：
 */
public class GuitaiA implements InvocationHandler {

    private final Object obj;

    public GuitaiA(Object obj){
        this.obj = obj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("invoke: 代理类，正式方法运行之前的处理");
        method.invoke(obj,args);
        System.out.println("invoke: 代理类，正式方法运行之后的处理" );
        return null;
    }
}
