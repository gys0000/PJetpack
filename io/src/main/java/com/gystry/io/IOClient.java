package com.gystry.io;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;

/**
 * @author gystry
 * ?????2020/12/23 19
 * ???gystry@163.com
 * ???
 */
public class IOClient {
    public static void main(String[] args) {
        new Thread(() -> {
            try {
                Socket socket = new Socket("127.0.0.1", 8000);
                while (true) {
                    System.out.println("IOClient----->");
                    try {
                        socket.getOutputStream().write((new Date() + ": hello world").getBytes());
                        Thread.sleep(2000);
                    } catch (Exception e) {
                    }
                }
            } catch (IOException e) {
            }
        }).start();
    }
}
