package com.vector.im.handler;

import com.vector.im.entity.IMHeader;
import com.vector.im.im.IMClient;
import com.vector.im.manager.IMMessageManager;
import com.vector.lover.proto.Packet;
import com.vector.lover.proto.chat.Chat;
import com.vector.lover.proto.system.IMSystem;
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

        if (msg.getServiceId() == Packet.ServiceId.SYSTEM_VALUE) {
            switch (msg.getCommandId()) {
                case IMSystem.CommandId.SYSTEM_LOGOUT_VALUE:
                    //退出登录了
                    IMClient.instance().setMaxMsgId(null);
                    var logout = IMSystem.LogoutOut.parseFrom(msg.getBody());
                    System.out.println("退出登录了：" + logout.getStatus());
                    break;
                default:
                    break;
            }
        }
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
