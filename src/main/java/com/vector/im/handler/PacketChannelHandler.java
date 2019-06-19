package com.vector.im.handler;

import com.vector.im.entity.IMHeader;
import com.vector.im.manager.IMMessageManager;
import com.vector.lover.proto.Packet;
import com.vector.lover.proto.chat.Chat;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Channel 处理器
 * author: vector.huang
 * date：2016/4/18 19:25
 */
public class PacketChannelHandler extends SimpleChannelInboundHandler<IMHeader> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, IMHeader msg) throws Exception {

        if (msg.getServiceId() == Packet.ServiceId.CHAT_VALUE) {

            switch (msg.getCommandId()) {
                case Chat.CommandId.CHAT_MSG_VALUE:
                    //响应了
                    IMMessageManager.msgResp(msg);
                    break;
                case Chat.CommandId.CHAT_MSG_OUT_VALUE:
                    //发送消息过来了
                    IMMessageManager.msgOut(msg);
                    break;
                default:
                    System.out.println("无法处理的消息：" + msg.getServiceId() + "-" + msg.getCommandId());
                    break;
            }
        }
    }
}
