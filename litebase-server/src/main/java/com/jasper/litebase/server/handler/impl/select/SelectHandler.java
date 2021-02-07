package com.jasper.litebase.server.handler.impl.select;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.jasper.litebase.config.util.LiteBaseStringUtil;
import com.jasper.litebase.server.connection.BackendConnection;
import com.jasper.litebase.server.handler.ComQueryHandler;
import com.jasper.litebase.server.protocol.constant.Fields;
import com.jasper.litebase.server.protocol.server.EOFPacket;
import com.jasper.litebase.server.protocol.server.FieldPacket;
import com.jasper.litebase.server.protocol.server.ResultSetHeaderPacket;
import com.jasper.litebase.server.protocol.server.RowDataPacket;
import com.jasper.litebase.server.protocol.util.PacketUtil;
import io.netty.buffer.ByteBuf;

public class SelectHandler extends ComQueryHandler<SQLSelectStatement> {

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
    public void handle(BackendConnection c, String sql, SQLSelectStatement statement) {
        if (sql.contains("DATABASE") || sql.contains("database")) {
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
}
