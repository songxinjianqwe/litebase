package com.jasper.litebase.server.connection;

import com.jasper.litebase.config.SessionConfig;
import io.netty.buffer.ByteBuf;

public interface Connection {
    int PACKET_HEADER_SIZE = 4;
    int MAX_PACKET_SIZE = 16 * 1024 * 1024;

    ByteBuf allocate();

    String getSchema();

    SessionConfig getSessionConfig();

    void writeBack(ByteBuf buffer);

    void heartbeat(byte[] data);

    void kill(byte[] data);

    void close();

    void ping();

    void query(byte[] data);

    void initDB(byte[] data);

    void writeErrMessage(int errno, String msg);

    void writeErrMessage(byte id, int errno, String msg);
}
