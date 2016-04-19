package com.vector.im.app;

import com.vector.im.im.IMClient;

/**
 * author: vector.huang
 * date：2016/4/18 19:51
 */
public class App {

    private static App instance =  new App();

    private App(){}

    public static App instance(){
        return instance;
    }

    public void create(){
        try {
            IMClient.instance().login("127.0.0.1",8080);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("连接失败了...");
        }
    }

    public void destroy(){
        IMClient.instance().logout();
    }

}
