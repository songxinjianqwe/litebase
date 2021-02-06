package com.jasper.litebase.server.handler;

import com.jasper.litebase.server.connection.BackendConnection;
import com.jasper.litebase.server.protocol.constant.ErrorCode;
import com.jasper.litebase.server.protocol.packet.PacketCodec;
import com.jasper.litebase.server.protocol.packet.client.AuthPacket;
import com.jasper.litebase.server.protocol.packet.MySQLPacket;
import com.jasper.litebase.server.protocol.packet.server.OkPacket;
import com.jasper.litebase.server.protocol.packet.server.QuitPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MySQLAuthenticator extends SimpleChannelInboundHandler<byte[]> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MySQLAuthenticator.class);
    private static final byte[] AUTH_OK = new byte[]{7, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0};
    private BackendConnection c;

    public MySQLAuthenticator(BackendConnection c) {
        this.c = c;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, byte[] data) {
        // check quit packet
        if (data.length == QuitPacket.QUIT.length && data[4] == MySQLPacket.COM_QUIT) {
            c.close();
            return;
        }

        AuthPacket auth = PacketCodec.decode(AuthPacket.class, data);
        LOGGER.info("user{}, pwd:{}, schema:{}", auth.user, auth.password, auth.database);
        ByteBuf buffer = c.allocate();
        buffer.writeBytes(AUTH_OK);
        c.writeBack(buffer);
//        // check user
//        if (!checkUser(auth.user, source.getHost())) {
//            failure(ErrorCode.ER_ACCESS_DENIED_ERROR, "Access denied for user '" + auth.user + "'");
//            return;
//        }
//
//        // check password
//        if (!checkPassword(auth.password, auth.user)) {
//            failure(ErrorCode.ER_ACCESS_DENIED_ERROR, "Access denied for user '" + auth.user + "'");
//            return;
//        }
//
//        // check schema
//        switch (checkSchema(auth.database, auth.user)) {
//            case ErrorCode.ER_BAD_DB_ERROR:
//                failure(ErrorCode.ER_BAD_DB_ERROR, "Unknown database '" + auth.database + "'");
//                break;
//            case ErrorCode.ER_DBACCESS_DENIED_ERROR:
//                String s = "Access denied for user '" + auth.user + "' to database '" + auth.database + "'";
//                failure(ErrorCode.ER_DBACCESS_DENIED_ERROR, s);
//                break;
//            default:
//                success(auth);
//        }
    }
}
