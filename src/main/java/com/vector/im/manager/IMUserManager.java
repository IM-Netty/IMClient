package com.vector.im.manager;

import com.vector.im.im.IMClient;

import io.netty.buffer.ByteBuf;

/**
 * author: vector.huang
 * date：2016/4/18 22:28
 */
public class IMUserManager {

    public static void receiveUserInfo(ByteBuf body){
        int id = body.readInt();
        IMClient.instance().setId(id);
        System.out.println("登陆成功，用户Id 为 "+id);
        IMTestManager.testReq("Test 通过");
    }

}
