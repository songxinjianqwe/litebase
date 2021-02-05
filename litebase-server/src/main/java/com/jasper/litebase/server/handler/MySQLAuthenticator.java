package com.jasper.litebase.server.handler;

import com.jasper.litebase.server.connection.BackendConnection;
import com.jasper.litebase.server.protocol.packet.MySQLPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MySQLAuthenticator extends SimpleChannelInboundHandler<MySQLPacket> {
    private BackendConnection c;

    public MySQLAuthenticator(BackendConnection c) {
        this.c = c;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MySQLPacket msg)  {
        // check quit packet
        if (data.length == QuitPacket.QUIT.length && data[4] == MySQLPacket.COM_QUIT) {
            source.close();
            return;
        }

        AuthPacket auth = new AuthPacket();
        auth.read(data);

        // check user
        if (!checkUser(auth.user, source.getHost())) {
            failure(ErrorCode.ER_ACCESS_DENIED_ERROR, "Access denied for user '" + auth.user + "'");
            return;
        }

        // check password
        if (!checkPassword(auth.password, auth.user)) {
            failure(ErrorCode.ER_ACCESS_DENIED_ERROR, "Access denied for user '" + auth.user + "'");
            return;
        }

        // check schema
        switch (checkSchema(auth.database, auth.user)) {
            case ErrorCode.ER_BAD_DB_ERROR:
                failure(ErrorCode.ER_BAD_DB_ERROR, "Unknown database '" + auth.database + "'");
                break;
            case ErrorCode.ER_DBACCESS_DENIED_ERROR:
                String s = "Access denied for user '" + auth.user + "' to database '" + auth.database + "'";
                failure(ErrorCode.ER_DBACCESS_DENIED_ERROR, s);
                break;
            default:
                success(auth);
        }
    }
}
