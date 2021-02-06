package com.jasper.litebase.server.handler;

import com.jasper.litebase.server.connection.BackendConnection;
import com.jasper.litebase.server.protocol.constant.ErrorCode;
import com.jasper.litebase.server.protocol.packet.MySQLPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class SQLCommandDispatcher extends SimpleChannelInboundHandler<byte[]> {
    protected BackendConnection c;

    public SQLCommandDispatcher(BackendConnection c) {
        this.c = c;
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, byte[] data) {
        switch (data[4]) {
            case MySQLPacket.COM_INIT_DB:
                c.initDB(data);
                break;
            case MySQLPacket.COM_QUERY:
                c.query(data);
                break;
            case MySQLPacket.COM_PING:
                c.ping();
                break;
            case MySQLPacket.COM_QUIT:
                c.close();
                break;
            case MySQLPacket.COM_PROCESS_KILL:
                c.kill(data);
                break;
            case MySQLPacket.COM_HEARTBEAT:
                c.heartbeat(data);
                break;
            default:
                c.writeErrMessage(ErrorCode.ER_UNKNOWN_COM_ERROR, "Unknown command");
        }
    }
}
