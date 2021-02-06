package com.jasper.litebase.server.protocol.packet;

public abstract class ClientPacket extends MySQLPacket {
    public abstract void resolve(byte[] data);
}
