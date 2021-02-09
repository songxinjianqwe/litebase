package com.jasper.litebase.server.handler.impl.ddl;

import com.alibaba.druid.sql.ast.statement.SQLDropDatabaseStatement;
import com.jasper.litebase.engine.api.SchemaTableApi;
import com.jasper.litebase.engine.domain.SchemaDefinition;
import com.jasper.litebase.server.connection.BackendConnection;
import com.jasper.litebase.server.handler.ComQueryHandler;

public class DropDataBaseStmtHandler extends ComQueryHandler<SQLDropDatabaseStatement> {
    public DropDataBaseStmtHandler(SchemaTableApi schemaTableApi) {
        super(schemaTableApi);
    }

    @Override
    protected void handle(BackendConnection c, Long queryId, String sql, SQLDropDatabaseStatement statement) {
        SchemaDefinition schemaDefinition = schemaTableApi.openSchema(statement.getDatabaseName());
        schemaTableApi.dropSchema(schemaDefinition);
    }
}
