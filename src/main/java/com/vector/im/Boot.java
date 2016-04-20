package com.vector.im;

import com.vector.im.app.App;
import com.vector.im.manager.IMMessageManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import sun.misc.Signal;
import sun.misc.SignalHandler;

/**
 * author: vector.huang
 * date：2016/4/18 1:15
 */
public class Boot extends Thread implements SignalHandler {

    private void boot() {
        App.instance().create();
    }

    @Override
    public void handle(Signal signal) {
        App.instance().destroy();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        new Boot().boot();
        Thread.sleep(1000);
        while (true) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String content = reader.readLine();
                String[] idContent = content.split("-");
                IMMessageManager.sendSingleMsgReq(Integer.parseInt(idContent[0]), idContent[1]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}



