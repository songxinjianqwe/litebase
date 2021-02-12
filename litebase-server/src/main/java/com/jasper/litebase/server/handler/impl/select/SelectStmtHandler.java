package com.jasper.litebase.server.handler.impl.select;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.jasper.litebase.config.constant.FieldType;
import com.jasper.litebase.engine.api.SchemaTableApi;
import com.jasper.litebase.engine.domain.ExecutionContext;
import com.jasper.litebase.engine.domain.ResultSet;
import com.jasper.litebase.engine.domain.Table;
import com.jasper.litebase.engine.domain.TableDefinition;
import com.jasper.litebase.server.connection.BackendConnection;
import com.jasper.litebase.server.engine.EngineManager;
import com.jasper.litebase.server.handler.ComQueryHandler;
import com.jasper.litebase.server.protocol.server.EOFPacket;
import com.jasper.litebase.server.protocol.server.FieldPacket;
import com.jasper.litebase.server.protocol.server.ResultSetHeaderPacket;
import com.jasper.litebase.server.protocol.util.PacketUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SelectStmtHandler extends ComQueryHandler<SQLSelectStatement> {

    private static final int FIELD_COUNT = 1;
    private static final ResultSetHeaderPacket header = PacketUtil.getHeader(FIELD_COUNT);
    private static final FieldPacket[] fields = new FieldPacket[FIELD_COUNT];
    private static final EOFPacket eof = new EOFPacket();

    static {
        byte packetId = 0;
        header.packetId = ++packetId;
        fields[0] = PacketUtil.getField("DATABASE()", FieldType.FIELD_TYPE_VAR_STRING.getCode());
        fields[0].packetId = ++packetId;
        eof.packetId = ++packetId;
    }

    public SelectStmtHandler(SchemaTableApi schemaTableApi) {
        super(schemaTableApi);
    }

    @Override
    protected ResultSet doQuery(BackendConnection c, Long queryId, String sql, SQLSelectStatement statement) {
        List<SQLSelectItem> selectList = statement.getSelect().getQueryBlock().getSelectList();
        SQLObject schema = statement.getSelect().getQueryBlock().getFrom().getParent();
        String tableName = statement.getSelect().getQueryBlock().getFrom().getAlias();
        String schemaName = schema.toString();
        Table table = schemaTableApi.openTable(schemaName, tableName);
        return EngineManager.getInstance(table.getTableDefinition().getEngineType()).query(
                new ExecutionContext(queryId, c.getSessionConfig()), table,
                selectList.stream().map(SQLSelectItem::getAlias).collect(Collectors.toList()),
                statement.getSelect().getQueryBlock().getWhere().toString());
    }

    // if (sql.contains("DATABASE") || sql.contains("database")) {
    // ByteBuf buffer = c.allocate();
    // header.writeToBuffer(buffer);
    // for (FieldPacket field : fields) {
    // field.writeToBuffer(buffer);
    // }
    // eof.writeToBuffer(buffer);
    // byte packetId = eof.packetId;
    // RowDataPacket row = new RowDataPacket(FIELD_COUNT);
    // row.add(LiteBaseStringUtil.encode(c.getSchema(), c.getSessionConfig().getCharset()));
    // row.packetId = ++packetId;
    // row.writeToBuffer(buffer);
    // EOFPacket lastEof = new EOFPacket();
    // lastEof.packetId = ++packetId;
    // lastEof.writeToBuffer(buffer);
    // c.writeBack(buffer);
    // }
}
