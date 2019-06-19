package com.vector.im;

import com.vector.im.app.App;
import com.vector.im.manager.IMMessageManager;
import sun.misc.Signal;
import sun.misc.SignalHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
                String[] idContent = content.split("-", 2);

                int exec = Integer.parseInt(idContent[0]);
                switch (exec){
                    case 1:
                        //发送消息
                        String[] contents = idContent[1].split("-", 2);
                        IMMessageManager.sendSingleMsgReq(Integer.parseInt(contents[0]), contents[1]);
                        break;
                    default:
                        System.out.println("不支持的操作");
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}



