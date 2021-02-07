package com.jasper.litebase.server.handler;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowVariantsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlStatement;
import com.jasper.litebase.config.util.LiteBaseStringUtil;
import com.jasper.litebase.engine.domain.Field;
import com.jasper.litebase.engine.domain.ResultSet;
import com.jasper.litebase.engine.domain.ResultSetMetaData;
import com.jasper.litebase.server.connection.BackendConnection;
import com.jasper.litebase.server.handler.impl.select.SelectHandler;
import com.jasper.litebase.server.handler.impl.show.ShowVariablesHandler;
import com.jasper.litebase.server.protocol.server.EOFPacket;
import com.jasper.litebase.server.protocol.server.FieldPacket;
import com.jasper.litebase.server.protocol.server.ResultSetHeaderPacket;
import com.jasper.litebase.server.protocol.server.RowDataPacket;
import com.jasper.litebase.server.protocol.util.PacketUtil;
import com.jasper.litebase.sql.parser.SQLParser;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public abstract class ComQueryHandler<T extends SQLStatement> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ComQueryHandler.class);

    private static Map<Class<? extends SQLStatement>, ComQueryHandler<?>> HANDLERS = new HashMap<>();

    static {
        HANDLERS.put(MySqlStatement.class, new SelectHandler());
        HANDLERS.put(MySqlShowVariantsStatement.class, new ShowVariablesHandler());
    }

    public static void query(BackendConnection c, String sql, Long queryId) {
        LOGGER.info("starting to execute COM_QUERY CMD, sql: {}, queryId:{}, conn: {}", sql, queryId, c);
        SQLStatement statement = SQLParser.parse(sql);
        Class<? extends SQLStatement> statementClass = statement.getClass();
        ComQueryHandler handler = HANDLERS.get(statementClass);
        if (handler == null) {
            throw new IllegalArgumentException("handler not found for " + statementClass.getName());
        }
        handler.handle(c, sql, statement);
    }

    public abstract void handle(BackendConnection c, String sql, T statement);

    protected void writeBackResultSet(BackendConnection c, String sql, T statement) {
        ByteBuf buffer = c.allocate();
        byte packetId = 0;

        ResultSet resultSet = getResultSet(c, sql, statement);
        ResultSetMetaData resultSetMetaData = resultSet.getResultSetMetaData();

        int fieldCount = resultSetMetaData.getFieldCount();
        List<Field> fields = resultSetMetaData.getFields();

        // header
        ResultSetHeaderPacket header = PacketUtil.getHeader(fieldCount);
        header.packetId = ++packetId;
        header.writeToBuffer(buffer);

        // fields
        for (Field field : fields) {
            FieldPacket fieldPacket = PacketUtil.getField(field.getName(), field.getType());
            fieldPacket.packetId = ++packetId;
            fieldPacket.writeToBuffer(buffer);
        }

        // eof
        EOFPacket eofPacket = new EOFPacket();
        eofPacket.packetId = ++packetId;
        eofPacket.writeToBuffer(buffer);

        // rows
        RowDataPacket row = new RowDataPacket(fieldCount);
        while (resultSet.next()) {
            row.fieldValues.clear();
            for (int i = 0; i < fieldCount; i++) {
                Object fieldValue = resultSet.getObject(i);
                if (fieldValue instanceof byte[]) {
                    row.add((byte[]) fieldValue);
                } else if (fieldValue instanceof String) {
                    row.add(LiteBaseStringUtil.encode((String) fieldValue, c.getSessionConfig().getCharset()));
                } else {
                    // TODO
                }
            }
            row.packetId = ++packetId;
            row.writeToBuffer(buffer);
        }

        // last eof
        EOFPacket lastEofPacket = new EOFPacket();
        lastEofPacket.packetId = ++packetId;
        lastEofPacket.writeToBuffer(buffer);

        c.writeBack(buffer);
    }

    protected ResultSet getResultSet(BackendConnection c, String sql, T statement) {
        return null;
    }
}
