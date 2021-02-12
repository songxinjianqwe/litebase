package com.jasper.litebase.server.handler.impl.ddl;

import com.alibaba.druid.sql.ast.statement.SQLDropTableStatement;
import com.jasper.litebase.engine.api.SchemaTableApi;
import com.jasper.litebase.engine.domain.Table;
import com.jasper.litebase.server.connection.BackendConnection;
import com.jasper.litebase.server.handler.ComQueryHandler;

public class DropTableStmtHandler extends ComQueryHandler<SQLDropTableStatement> {
    public DropTableStmtHandler(SchemaTableApi schemaTableApi) {
        super(schemaTableApi);
    }

    @Override
    protected void handle(BackendConnection c, Long queryId, String sql, SQLDropTableStatement statement) {
        Table t = schemaTableApi.openTable(c.getSchema(), statement.getTableSources().get(0).getTableName());
        schemaTableApi.dropTable(t.getTableDefinition());
    }
}
