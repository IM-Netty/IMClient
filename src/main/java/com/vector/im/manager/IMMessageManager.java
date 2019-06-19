package com.vector.im.manager;

import com.google.protobuf.InvalidProtocolBufferException;
import com.vector.im.entity.IMHeader;
import com.vector.im.im.IMClient;
import com.vector.im.util.IMUtil;
import com.vector.lover.proto.Packet;
import com.vector.lover.proto.chat.Chat;

/**
 * 发送和接收消息
 * author: vector.huang
 * date：2016/4/18 22:06
 */
public class IMMessageManager {

    public static void sendSingleMsgReq(int to, String content) {

        var msgReq = Chat.MsgReq.newBuilder();
        msgReq.setMsgId(IMClient.instance().getUsableMsgId());
        msgReq.setTo(to);
        msgReq.setContent(content);
        msgReq.setChatType(Chat.ChatType.SINGLE);
        msgReq.setMsgType(Chat.MessageType.TEXT);

        var header = IMUtil.newHeader(Packet.ServiceId.CHAT, Chat.CommandId.CHAT_MSG, msgReq);

        IMClient.instance().channel().writeAndFlush(header);
    }

    /**
     * 发送消息只有的响应
     */
    public static void msgResp(IMHeader header) throws InvalidProtocolBufferException {
        var resp = Chat.MsgResp.parseFrom(header.getBody());
        if (resp.getStatus() == Packet.Status.OK) {
            System.out.println("消息发送成功");
        } else {
            System.out.println("消息发送失败：" + resp.getMsg());
        }
    }

    public static void msgOut(IMHeader header) throws InvalidProtocolBufferException {

        var msgOut = Chat.MsgOut.parseFrom(header.getBody());
        int from = msgOut.getFrom();
        String content = msgOut.getContent();

        System.out.println(from +" 说："+content);
    }

}
