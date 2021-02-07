package com.jasper.litebase.server.connection;

import com.jasper.litebase.config.SessionConfig;
import com.jasper.litebase.config.util.LiteBaseStringUtil;
import com.jasper.litebase.config.util.RandomUtil;
import com.jasper.litebase.server.handler.ComQueryHandler;
import com.jasper.litebase.server.protocol.MySQLPacketResolver;
import com.jasper.litebase.server.protocol.MySQLPacket;
import com.jasper.litebase.server.protocol.PacketCodec;
import com.jasper.litebase.server.protocol.client.AuthPacket;
import com.jasper.litebase.config.constant.Capabilities;
import com.jasper.litebase.config.constant.ErrorCode;
import com.jasper.litebase.config.constant.Versions;
import com.jasper.litebase.server.protocol.server.ErrorPacket;
import com.jasper.litebase.server.protocol.server.HandshakePacket;
import com.jasper.litebase.server.protocol.server.QuitPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.socket.SocketChannel;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BackendConnection implements Connection {
    private static final Logger LOGGER = LoggerFactory.getLogger(BackendConnection.class);
    private static final byte[] AUTH_OK = new byte[] { 7, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0 };

    protected SocketChannel socketChannel;
    protected ByteBufAllocator allocator;

    private AtomicLong querySeq = new AtomicLong();

    // schema
    protected String schema;
    protected String user;
    protected byte[] seed;

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

    /** 建联之后立即发送 */
    @Override
    public void handshake() {
        // 生成认证数据
        byte[] rand1 = RandomUtil.randomBytes(8);
        byte[] rand2 = RandomUtil.randomBytes(12);

        // 保存认证数据
        byte[] seed = new byte[rand1.length + rand2.length];
        System.arraycopy(rand1, 0, seed, 0, rand1.length);
        System.arraycopy(rand2, 0, seed, rand1.length, rand2.length);

        this.seed = seed;

        // 发送握手数据包
        HandshakePacket packet = new HandshakePacket();
        packet.packetId = 0;
        packet.protocolVersion = Versions.PROTOCOL_VERSION;
        packet.serverVersion = Versions.SERVER_VERSION;
        packet.threadId = connectionId;
        packet.seed = rand1;
        packet.serverCapabilities = getServerCapabilities();
        packet.serverCharsetIndex = (byte) (sessionConfig.getCharsetIndex() & 0xff);
        packet.serverStatus = 2;
        packet.restOfScrambleBuff = rand2;
        packet.writeBack(this);
    }

    @Override
    public void auth(byte[] data) {
        // check quit packet
        if (data.length == QuitPacket.QUIT.length && data[4] == MySQLPacket.COM_QUIT) {
            close();
            return;
        }

        AuthPacket auth = PacketCodec.decode(AuthPacket.class, data);
        LOGGER.info("user logging in, user:{}, schema:{}", auth.user, auth.database);
        this.user = auth.user;
        this.schema = auth.database;
        this.sessionConfig.setCharsetIndex(auth.charsetIndex);
        LOGGER.info("{} user '{}' login success", this, user);
        ByteBuf buffer = allocate();
        buffer.writeBytes(AUTH_OK);
        writeBack(buffer);

        // // check user
        // if (!checkUser(auth.user, source.getHost())) {
        // failure(ErrorCode.ER_ACCESS_DENIED_ERROR, "Access denied for user '" + auth.user +
        // "'");
        // return;
        // }
        //
        // // check password
        // if (!checkPassword(auth.password, auth.user)) {
        // failure(ErrorCode.ER_ACCESS_DENIED_ERROR, "Access denied for user '" + auth.user +
        // "'");
        // return;
        // }
        //
        // // check schema
        // switch (checkSchema(auth.database, auth.user)) {
        // case ErrorCode.ER_BAD_DB_ERROR:
        // failure(ErrorCode.ER_BAD_DB_ERROR, "Unknown database '" + auth.database +
        // "'");
        // break;
        // case ErrorCode.ER_DBACCESS_DENIED_ERROR:
        // String s = "Access denied for user '" + auth.user + "' to database '" +
        // auth.database + "'";
        // failure(ErrorCode.ER_DBACCESS_DENIED_ERROR, s);
        // break;
        // default:
        // success(auth);
        // }
    }

    protected int getServerCapabilities() {
        int flag = 0;
        flag |= Capabilities.CLIENT_LONG_PASSWORD;
        flag |= Capabilities.CLIENT_FOUND_ROWS;
        flag |= Capabilities.CLIENT_LONG_FLAG;
        flag |= Capabilities.CLIENT_CONNECT_WITH_DB;
        // flag |= Capabilities.CLIENT_NO_SCHEMA;
        // flag |= Capabilities.CLIENT_COMPRESS;
        flag |= Capabilities.CLIENT_ODBC;
        // flag |= Capabilities.CLIENT_LOCAL_FILES;
        flag |= Capabilities.CLIENT_IGNORE_SPACE;
        flag |= Capabilities.CLIENT_PROTOCOL_41;
        flag |= Capabilities.CLIENT_INTERACTIVE;
        // flag |= Capabilities.CLIENT_SSL;
        flag |= Capabilities.CLIENT_IGNORE_SIGPIPE;
        flag |= Capabilities.CLIENT_TRANSACTIONS;
        // flag |= ServerDefs.CLIENT_RESERVED;
        flag |= Capabilities.CLIENT_SECURE_CONNECTION;
        return flag;
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
        MySQLPacketResolver mm = new MySQLPacketResolver(data);
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
        ComQueryHandler.query(this, sql, querySeq.incrementAndGet());
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

    @Override
    public String toString() {
        return "BackendConnection{" + "connectionId=" + connectionId + ", schema='" + schema + '\'' + ", user='" + user
                + '\'' + ", remoteHost='" + remoteHost + '\'' + ", remotePort=" + remotePort + ", localPort="
                + localPort + '}';
    }
}
