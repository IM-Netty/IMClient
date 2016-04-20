package com.vector.im;

import com.vector.im.app.App;
import com.vector.im.manager.IMMessageManager;
import com.vector.im.manager.IMUserManager;

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

                int exec = Integer.parseInt(idContent[0]);

                switch (exec){
                    case 0:
                        IMUserManager.onlineUserReq(1);
                        break;

                    default:
                        IMMessageManager.sendSingleMsgReq(exec, idContent[1]);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}



