package com.gystry.threads;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MyClass {
    public static void main(String[] args) {
//        executors();
//        callable();
//        RunnableTest runnableTest = new RunnableTest();
//        runnableTest.runTest();
        WaitNotify waitNotify=new WaitNotify();
        waitNotify.runTest();
    }

    private static void thread() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                System.out.println("thread方法打印");
            }
        };
        thread.start();
    }

    private static void runnableThread() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("runnableThread方法打印");
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private static void executors() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("executors方法打印：" + Thread.currentThread().getName());
            }
        };
        Executor executor = Executors.newCachedThreadPool();
        executor.execute(runnable);
        executor.execute(runnable);
        executor.execute(runnable);
        executor.execute(runnable);
    }

    private static void callable(){
        Callable<String> callable=new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(1500);
                return "结束";
            }
        };
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<String> future = executorService.submit(callable);
        System.out.println(System.currentTimeMillis());
        String s = null;
        try {
            s = future.get();
            System.out.println(s+":"+System.currentTimeMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}