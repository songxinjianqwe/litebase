package com.jasper.litebase.server.handler.impl.show;

import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowTableStatusStatement;
import com.jasper.litebase.engine.api.SchemaTableApi;
import com.jasper.litebase.engine.domain.ExecutionContext;
import com.jasper.litebase.engine.domain.ResultSet;
import com.jasper.litebase.engine.domain.TableDefinition;
import com.jasper.litebase.server.connection.BackendConnection;
import com.jasper.litebase.server.engine.EngineManager;
import com.jasper.litebase.server.handler.ComQueryHandler;

import java.util.Arrays;
import java.util.Collections;

public class ShowTablesStmtHandler extends ComQueryHandler<MySqlShowTableStatusStatement> {
    public ShowTablesStmtHandler(SchemaTableApi schemaTableApi) {
        super(schemaTableApi);
    }

    @Override
    protected ResultSet doQuery(BackendConnection c, Long queryId, String sql,
            MySqlShowTableStatusStatement statement) {
        String schema = "information_schema";
        String table = "tables";
        String where = null;
        if (statement.getWhere() != null) {
            where = statement.getWhere().toString();
        } else if (statement.getLike() != null) {
            where = "table_name like " + statement.getLike().toString();
        }
        TableDefinition tableDefinition = schemaTableApi.openTable(schema, table);
        return EngineManager.getInstance(tableDefinition.getEngineType()).query(
                new ExecutionContext(queryId, c.getSessionConfig()), tableDefinition,
                Collections.singletonList("Tables_in_" + schema), where);
    }
}
