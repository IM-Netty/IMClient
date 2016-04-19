package com.vector.im.im;

import com.vector.im.handler.ByteToPacketCodec;
import com.vector.im.handler.PacketChannelHandler;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * author: vector.huang
 * date：2016/4/18 19:42
 */
public class IMClient {

    private EventLoopGroup worker;
    private ChannelFuture channelFuture;
    private int id;

    private static IMClient instance;
    private static Lock lock = new ReentrantLock();

    private IMClient(){}
    public static IMClient instance(){
        if(instance == null){
            lock.lock();
            if(instance == null){
                instance = new IMClient();
            }
            lock.unlock();
            lock = null;
        }
        return instance;
    }

    public void login(String host, int port) throws InterruptedException {
        System.out.println("开始连接 "+host+":"+port);
        worker = new NioEventLoopGroup();
        Bootstrap boot = new Bootstrap();
        boot.group(worker);

        boot.channel(NioSocketChannel.class);

        boot.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024*1024,0,4,-4,0,false));
                ch.pipeline().addLast(new ByteToPacketCodec());
                ch.pipeline().addLast(new PacketChannelHandler());
            }
        });
        boot.option(ChannelOption.SO_KEEPALIVE, true);
        channelFuture = boot.connect(host, port).sync();
        System.out.println("连接成功...");
    }

    public void logout(){
        if(channelFuture != null && channelFuture.channel() !=null){
            if(channelFuture.channel().isOpen()){
                try {
                    channelFuture.channel().closeFuture().sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        worker.shutdownGracefully();
    }

    public Channel channel(){
        if(channelFuture == null){
            return null;
        }
        return channelFuture.channel();
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
