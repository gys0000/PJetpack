package com.gystry.libnetwork;

import java.lang.reflect.Type;

public interface Convert<T> {
    public T convert(String content, Type type);
    public T convert(String content, Class claz);
}
