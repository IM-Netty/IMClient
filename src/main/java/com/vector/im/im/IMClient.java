package com.vector.im.im;

import com.vector.im.config.Config;
import io.netty.channel.Channel;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * author: vector.huang
 * date：2016/4/20 21:42
 */
public class IMClient {

    /**
     * 客户端的版本号
     */
    public int version = 1;
    /**
     * 登录后的msgId
     */
    private AtomicLong maxMsgId;

    private static IMClient instance;
    private static Lock lock = new ReentrantLock();

    private ThreadSocket threadSocket;
    private IMClient() {
    }

    public static IMClient instance() {
        if (instance == null) {
            lock.lock();
            if (instance == null) {
                instance = new IMClient();
            }
            lock.unlock();
            lock = null;
        }
        return instance;
    }

    public void connect() {
        threadSocket = new ThreadSocket();
        threadSocket.setOnChannelActiveListener(ctx -> {
            System.out.println("连接的业务服务器可以开始发送请求了");
        });
        threadSocket.connect(Config.LOGIN_HOST, Config.LOGIN_PORT);
    }

    public void close() {
        System.out.println("业务服务器开始关闭...");
        threadSocket.close();
    }

    public Channel channel() {
        return threadSocket.channel();
    }

    /**
     * 获取下一个可用的msgId
     * 例如，初始化为11，调用之后返回12
     */
    public long getUsableMsgId() {
        return maxMsgId.incrementAndGet();
    }

    public void setMaxMsgId(long usableMsgId) {
        this.maxMsgId = new AtomicLong(usableMsgId);
    }

    public boolean isLogin() {
        return maxMsgId != null;
    }

}
