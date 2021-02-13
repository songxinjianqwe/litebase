package com.jasper.litebase.server.handler.impl.show;

import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowTableStatusStatement;
import com.jasper.litebase.engine.api.SchemaTableApi;
import com.jasper.litebase.engine.domain.*;
import com.jasper.litebase.server.connection.BackendConnection;
import com.jasper.litebase.engine.api.impl.EngineManager;
import com.jasper.litebase.server.handler.ComQueryHandler;

import java.util.Collections;

public class ShowTablesStmtHandler extends ComQueryHandler<MySqlShowTableStatusStatement> {
    public ShowTablesStmtHandler(SchemaTableApi schemaTableApi) {
        super(schemaTableApi);
    }

    @Override
    protected ResultSet doQuery(BackendConnection c, Long queryId, String sql,
            MySqlShowTableStatusStatement statement) {
        String schema = "information_schema";
        String tableName = "tables";
        String where = null;
        if (statement.getWhere() != null) {
            where = statement.getWhere().toString();
        } else if (statement.getLike() != null) {
            where = "table_name like " + statement.getLike().toString();
        }
        Table table = schemaTableApi.openTable(schema, tableName);
        return table.query(new ExecutionContext(queryId, c.getSessionConfig()),
                Collections.singletonList("Tables_in_" + schema), where);
    }
}
