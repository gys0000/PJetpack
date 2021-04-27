package com.gystry.threads;

import java.awt.TextArea;

/**
 * @author gystry
 * 创建日期：2021/4/27 18
 * 邮箱：gystry@163.com
 * 描述：
 */
public class WaitNotify {
    private String shareString;
    private synchronized void initString(){
        shareString="xiugaileshuju";
        System.out.println("--->");
        notifyAll();
    }
    private synchronized void printString(){
        System.out.println("->");
        notifyAll();
        //这个地方用while和if是不一样的
        while (shareString==null) {
            try {
                System.out.println("-->");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("打印->"+shareString);
    }

    public void runTest(){
        Thread thread=new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                printString();
            }
        };
        thread.start();
        Thread thread2=new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                printString();
            }
        };
        thread2.start();
        Thread thread1=new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                initString();
            }
        };
        thread1.start();
    }



}
