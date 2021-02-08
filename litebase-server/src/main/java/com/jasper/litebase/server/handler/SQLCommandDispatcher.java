package com.jasper.litebase.server.handler;

import com.jasper.litebase.server.connection.BackendConnection;
import com.jasper.litebase.server.protocol.MySQLPacket;
import com.jasper.litebase.config.constant.ErrorCode;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SQLCommandDispatcher extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(SQLCommandDispatcher.class);
    protected BackendConnection c;
    protected boolean authenticated = false;

    public SQLCommandDispatcher(BackendConnection c) {
        this.c = c;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("execute sql failed, caused by: ", cause);
        c.writeErrMessage(0, cause.getMessage());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf buf = (ByteBuf) msg;
        // TODO将其改为直接从ByteBuf中读数据，这样就可以减少一次拷贝
        byte[] data = new byte[buf.writerIndex()];
        buf.getBytes(0, data);
        if (!authenticated) {
            c.auth(data);
            authenticated = true;
            return;
        }
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
