package com.vector.im.test;

import com.vector.im.im.ThreadSocket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.SynchronousQueue;

/**
 * author: vector.huang
 * date：2016/4/20 22:08
 */
public class Test {

    public static void main(String[] args) {
        List<ThreadSocket> sockets = new ArrayList<>(1000);
        int iMax = 10;
        int jMax = 100;

        for(int i = 0 ; i< iMax;i++){
//            new Thread(()->{
                for(int j =0;j<jMax;j++){
                    ThreadSocket socket = new ThreadSocket();
                    socket.connect("127.0.0.1",8080);
                    sockets.add(socket);
                }
//            }).start();
        }
        while (true){
        }
    }
}
