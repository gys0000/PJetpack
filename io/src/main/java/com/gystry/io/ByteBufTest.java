package com.gystry.io;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * @author gystry
 * 创建日期：2020/12/28 14
 * 邮箱：gystry@163.com
 * 描述：
 */
public class ByteBufTest {

    public static void main(String[] args) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(9, 100);

    }
}
