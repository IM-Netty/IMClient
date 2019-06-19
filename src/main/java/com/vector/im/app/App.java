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
        IMClient.instance().connect();
    }

    public void destroy(){

    }

}
