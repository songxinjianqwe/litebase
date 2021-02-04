package com.jasper.litebase.server.handler;

import com.jasper.litebase.server.protocol.domain.MySQLPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class SQLCommandHandler extends SimpleChannelInboundHandler<MySQLPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MySQLPacket msg) {

    }
}
