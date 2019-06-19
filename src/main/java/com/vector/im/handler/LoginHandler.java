package com.vector.im.handler;

import com.vector.im.app.App;
import com.vector.im.entity.IMHeader;
import com.vector.im.im.IMClient;
import com.vector.im.util.IMUtil;
import com.vector.lover.proto.Packet;
import com.vector.lover.proto.system.IMSystem;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author: vector.huang
 * @date: 2019/06/13 12:48
 */
public class LoginHandler extends SimpleChannelInboundHandler<IMHeader> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);

        System.out.println("连接成功，准备登录");

        //连接成功，立马登录
        var login = IMSystem.LoginReq.newBuilder();
        //1
//        login.setToken("462e6819-9ca4-46d3-8e09-60e747113518");
        //2
        login.setToken("c3afbb71-4a66-44ec-bc79-af6dadfd96b2");

        var req = IMUtil.newHeader(Packet.ServiceId.SYSTEM, IMSystem.CommandId.SYSTEM_LOGIN, login);
        ctx.writeAndFlush(req);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        System.out.println("连接断开了");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, IMHeader msg) throws Exception {

        if (msg.getServiceId() == Packet.ServiceId.SYSTEM_VALUE
                && msg.getServiceId() == IMSystem.CommandId.SYSTEM_LOGIN_VALUE) {

            var loginResp = IMSystem.LoginResp.parseFrom(msg.getBody());
            if (loginResp.getStatus() == Packet.Status.OK) {
                //登录成功
                System.out.println("登录成功：" + loginResp.getMaxMsgId());

                IMClient.instance().setMaxMsgId(loginResp.getMaxMsgId());

                ctx.pipeline().replace(this,"imHandler",new PacketChannelHandler());

            } else {
                System.out.println("登录失败：" + loginResp.getMsg());
            }

        }

    }
}
