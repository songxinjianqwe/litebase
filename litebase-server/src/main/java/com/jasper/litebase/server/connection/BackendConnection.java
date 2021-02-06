package com.jasper.litebase.server.connection;

import com.jasper.litebase.config.SessionConfig;
import com.jasper.litebase.server.protocol.codec.MySQLMessage;
import com.jasper.litebase.server.protocol.constant.ErrorCode;
import com.jasper.litebase.server.protocol.packet.server.ErrorPacket;
import com.jasper.litebase.server.util.LiteBaseStringUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.socket.SocketChannel;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;

public class BackendConnection implements Connection {
    protected SocketChannel socketChannel;
    protected ByteBufAllocator allocator;
    // schema
    protected String schema;

    // session config
    protected SessionConfig sessionConfig;


    // meta
    protected long connectionId;
    protected String remoteHost;
    protected int remotePort;
    protected int localPort;

    public BackendConnection(SocketChannel socketChannel, SessionConfig sessionConfig, long id) {
        this.socketChannel = socketChannel;
        this.sessionConfig = sessionConfig;
        this.connectionId = id;
        this.allocator = socketChannel.alloc();
        this.remoteHost = socketChannel.remoteAddress().getAddress().getHostAddress();
        this.remotePort = socketChannel.remoteAddress().getPort();
        this.localPort = socketChannel.localAddress().getPort();
    }

    @Override
    public void writeBack(ByteBuf buf) {
        socketChannel.writeAndFlush(buf);
    }

    @Override
    public ByteBuf allocate() {
        return allocator.buffer();
    }

    @Override
    public String getSchema() {
        return schema;
    }

    @Override
    public SessionConfig getSessionConfig() {
        return sessionConfig;
    }

    @Override
    public void heartbeat(byte[] data) {

    }

    @Override
    public void kill(byte[] data) {

    }

    @Override
    public void close() {

    }

    @Override
    public void ping() {

    }

    @Override
    public void query(byte[] data) {
        // 取得语句
        MySQLMessage mm = new MySQLMessage(data);
        mm.position(5);
        String sql;
        String charset = sessionConfig.getCharset();
        try {
            // 使用指定的编码来读取数据
            sql = mm.readString(charset);
        } catch (UnsupportedEncodingException e) {
            writeErrMessage(ErrorCode.ER_UNKNOWN_CHARACTER_SET, "Unknown charset '" + charset + "'");
            return;
        }
        if (StringUtils.isEmpty(sql)) {
            writeErrMessage(ErrorCode.ER_NOT_ALLOWED_COMMAND, "Empty SQL");
            return;
        }

        // 执行查询
        
    }

    @Override
    public void initDB(byte[] data) {

    }

    @Override
    public void writeErrMessage(int errno, String msg) {
        writeErrMessage((byte) 1, errno, msg);
    }

    @Override
    public void writeErrMessage(byte id, int errno, String msg) {
        ErrorPacket err = new ErrorPacket();
        err.packetId = id;
        err.errno = errno;
        err.message = LiteBaseStringUtil.encode(msg, sessionConfig.getCharset());
        err.writeBack(this);
    }
}
