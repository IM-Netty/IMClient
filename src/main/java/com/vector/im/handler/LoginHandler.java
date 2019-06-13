package com.vector.im.handler;

import com.vector.im.entity.IMMessage;
import com.vector.im.util.IMUtil;
import com.vector.lover.proto.Packet;
import com.vector.lover.proto.system.IMSystem;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author: vector.huang
 * @date: 2019/06/13 12:48
 */
public class LoginHandler extends SimpleChannelInboundHandler<IMMessage> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);

        System.out.println("连接成功，准备登录");

        //连接成功，立马登录
        var login = IMSystem.LoginReq.newBuilder();
        login.setToken("d00026c0-caf5-408b-8b9e-c1f713cd732d");

        var req = IMUtil.newHeader(Packet.ServiceId.SYSTEM, IMSystem.CommandId.SYSTEM_LOGIN, login);
        ctx.writeAndFlush(req);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        System.out.println("连接断开了");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, IMMessage msg) throws Exception {

        if (msg.getServiceId() == Packet.ServiceId.SYSTEM_VALUE
                && msg.getServiceId() == IMSystem.CommandId.SYSTEM_LOGIN_VALUE) {

            var loginResp = IMSystem.LoginResp.parseFrom(msg.getBody());
            if (loginResp.getStatus() == Packet.Status.OK) {
                //登录成功
                System.out.println("登录成功");

                ctx.pipeline().replace(this,"imHandler",new PacketChannelHandler());

            } else {
                System.out.println("登录失败：" + loginResp.getMsg());
            }

        }

    }
}
