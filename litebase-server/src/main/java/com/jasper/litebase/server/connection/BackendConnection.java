package com.jasper.litebase.server.connection;

import com.jasper.litebase.config.SessionConfig;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.socket.SocketChannel;

public class BackendConnection {
    protected SocketChannel socketChannel;
    protected SessionConfig session;
    protected ByteBufAllocator allocator;
    protected static final int PACKET_HEADER_SIZE = 4;
    protected static final int MAX_PACKET_SIZE = 16 * 1024 * 1024;

    public BackendConnection(SocketChannel socketChannel, SessionConfig session) {
        this.socketChannel = socketChannel;
        this.session = session;
        this.allocator = socketChannel.alloc();
    }

    public void writeBack(ByteBuf buf) {
        socketChannel.writeAndFlush(buf);
    }

    public int getPacketHeaderSize() {
        return PACKET_HEADER_SIZE;
    }

    public ByteBuf allocate() {
        return allocator.buffer();
    }
}
