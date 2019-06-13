package com.vector.im.entity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author: vector.huang
 * @date: 2019/06/11 09:26
 */
public class IMMessage {

    //固定的 4*5=20
    public static final int HEAD_LENGTH = 20;

    //版本需要一致才会处理，默认为0
    private int version;
    //指令Id - 组合成一个cmdId
    private short serviceId;
    private short commandId;
    //也不知道干嘛的，暂时不用传递
    private int seq;

    private byte[] body;


    public IMMessage() {
    }

    public IMMessage(short serviceId, short commandId, byte[] body) {
        this.serviceId = serviceId;
        this.commandId = commandId;
        this.body = body;
    }

    public static IMMessage parseFrom(byte[] bytes) {

        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);

        var msg = new IMMessage();

        readInt(inputStream);
        msg.setVersion(readInt(inputStream));
        msg.setServiceId(readShort(inputStream));
        msg.setCommandId(readShort(inputStream));
        msg.setSeq(readInt(inputStream));
        readInt(inputStream);
        msg.setBody(inputStream.readAllBytes());

        return msg;
    }


    public byte[] toByteArray() {

        ByteArrayOutputStream stream =
                new ByteArrayOutputStream(HEAD_LENGTH + body.length);

        writeInt(HEAD_LENGTH, stream);
        writeInt(version, stream);
        writeShort(serviceId, stream);
        writeShort(commandId, stream);
        writeInt(seq, stream);
        writeInt(body.length, stream);

        stream.writeBytes(body);

        return stream.toByteArray();
    }

    private static void writeInt(int value, ByteArrayOutputStream out) {
        out.write((value >> 24) & 0xFF);
        out.write((value >> 16) & 0xFF);
        out.write((value >> 8) & 0xFF);
        out.write((value) & 0xFF);
    }

    private static void writeShort(int value, ByteArrayOutputStream out) {
        out.write((value >> 8) & 0xFF);
        out.write((value) & 0xFF);
    }

    private static int readInt(ByteArrayInputStream in) {

        return ((in.read() & 0xFF) << 24)
                | ((in.read() & 0xFF) << 16)
                | ((in.read() & 0xFF) << 8)
                | (in.read() & 0xFF);

    }

    private static short readShort(ByteArrayInputStream in) {

        return (short) (((in.read() & 0xFF) << 8)
                | (in.read() & 0xFF));
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public short getServiceId() {
        return serviceId;
    }

    public void setServiceId(short serviceId) {
        this.serviceId = serviceId;
    }

    public short getCommandId() {
        return commandId;
    }

    public void setCommandId(short commandId) {
        this.commandId = commandId;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

}
