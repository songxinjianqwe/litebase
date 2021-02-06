package com.jasper.litebase.server.handler.impl;

import com.jasper.litebase.server.connection.BackendConnection;
import com.jasper.litebase.server.handler.ComQueryHandler;
import com.jasper.litebase.server.protocol.codec.util.PacketUtil;
import com.jasper.litebase.server.protocol.constant.Fields;
import com.jasper.litebase.server.protocol.packet.server.EOFPacket;
import com.jasper.litebase.server.protocol.packet.server.FieldPacket;
import com.jasper.litebase.server.protocol.packet.server.ResultSetHeaderPacket;
import com.jasper.litebase.server.protocol.packet.server.RowDataPacket;
import com.jasper.litebase.server.util.LiteBaseStringUtil;
import com.jasper.litebase.sql.parser.enumeration.QueryType;
import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;

public class SelectHandler extends ComQueryHandler {

    private static final int FIELD_COUNT = 1;
    private static final ResultSetHeaderPacket header = PacketUtil.getHeader(FIELD_COUNT);
    private static final FieldPacket[] fields = new FieldPacket[FIELD_COUNT];
    private static final EOFPacket eof = new EOFPacket();

    static {
        byte packetId = 0;
        header.packetId = ++packetId;
        fields[0] = PacketUtil.getField("DATABASE()", Fields.FIELD_TYPE_VAR_STRING);
        fields[0].packetId = ++packetId;
        eof.packetId = ++packetId;
    }

    @Override
    public void handle(BackendConnection c, String sql) {
        if(sql.contains("DATABASE") || sql.contains("database")) {
            ByteBuf buffer = c.allocate();
            header.writeToBuffer(buffer);
            for (FieldPacket field : fields) {
                field.writeToBuffer(buffer);
            }
            eof.writeToBuffer(buffer);
            byte packetId = eof.packetId;
            RowDataPacket row = new RowDataPacket(FIELD_COUNT);
            row.add(LiteBaseStringUtil.encode(c.getSchema(), c.getSessionConfig().getCharset()));
            row.packetId = ++packetId;
            row.writeToBuffer(buffer);
            EOFPacket lastEof = new EOFPacket();
            lastEof.packetId = ++packetId;
            lastEof.writeToBuffer(buffer);
            c.writeBack(buffer);
        }
    }

    @Override
    protected QueryType operation() {
        return QueryType.SELECT;
    }
}
