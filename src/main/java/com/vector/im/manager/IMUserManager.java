package com.vector.im.manager;

import com.vector.im.constant.ProtocolConstant;
import com.vector.im.entity.Packet;
import com.vector.im.im.IMClient;
import com.vector.im.im.ThreadSocket;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

/**
 * author: vector.huang
 * date：2016/4/18 22:28
 */
public class IMUserManager {

    public static void loginReq(String username, String password) {
        Channel channel = IMClient.instance().channel();
        ByteBuf body = channel.alloc().buffer();
        byte[] uBytes = username.getBytes();
        byte[] pBytes = password.getBytes();

        body.writeInt(uBytes.length)
                .writeBytes(uBytes)
                .writeInt(pBytes.length)
                .writeBytes(pBytes);

        Packet packet = new Packet(body.readableBytes() + 12, ProtocolConstant.SID_USER
                , ProtocolConstant.CID_USER_LOGIN_REQ, body);
        channel.writeAndFlush(packet);

    }

    public static void loginResp(ByteBuf body) {
        int id = body.readInt();
        IMClient.instance().setUserId(id);
        System.out.println("登陆成功，用户Id 为 " + id);
        IMTestManager.testReq("Test 通过");
    }


    public static void onlineUserReq(int reqType) {
        Channel channel = IMClient.instance().channel();
        ByteBuf body = channel.alloc().buffer();

        body.writeInt(reqType);

        Packet packet = new Packet(body.readableBytes() + 12
                , ProtocolConstant.SID_USER
                , ProtocolConstant.CID_USER_ONLINE_REQ, body);
        channel.writeAndFlush(packet);

    }

    public static void onlineUserResp(ByteBuf body) {
        int total = body.readInt();
        System.out.println("在线用户总数："+total);

        for (int i = 0; i < total; i++) {
            int userId = body.readInt();
            String username = body.readBytes(body.readInt()).toString(Charset.defaultCharset());
            System.out.println(userId +" : "+username);
        }
    }

}
