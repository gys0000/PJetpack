package com.gystry.threads;

/**
 * @author gystry
 * 创建日期：2021/4/26 17
 * 邮箱：gystry@163.com
 * 描述：
 */
public class RunnableTest {
    private boolean isRun=true;

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
