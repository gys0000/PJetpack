package com.gystry.threads;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author gystry
 * 创建日期：2021/4/26 17
 * 邮箱：gystry@163.com
 * 描述：
 */
public class RunnableTest {
    private boolean isRun=true;
    private int x=0;
    ReentrantReadWriteLock lock=new ReentrantReadWriteLock();
    Lock readLock=lock.readLock();
    Lock writeLock=lock.writeLock();

    private void count(){
        writeLock.lock();
        try {
            x++;
        }finally {
            writeLock.unlock();
        }
    }

    private void print(){
        readLock.lock();
        try {
            System.out.println("---->"+x);
        }finally {
            readLock.unlock();
        }
    }

    private void stop(){
        isRun=false;
    }

    public void runTest(){
        new Thread(){
            @Override
            public void run() {
                while (isRun){
                }
            }
        }.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stop();
    }
}
