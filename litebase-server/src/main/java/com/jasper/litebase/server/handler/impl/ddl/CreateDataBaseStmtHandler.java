package com.jasper.litebase.server.handler.impl.ddl;

import com.alibaba.druid.sql.ast.statement.SQLCreateDatabaseStatement;
import com.jasper.litebase.config.EngineType;
import com.jasper.litebase.config.GlobalConfig;
import com.jasper.litebase.engine.api.SchemaTableApi;
import com.jasper.litebase.engine.domain.SchemaDefinition;
import com.jasper.litebase.server.connection.BackendConnection;
import com.jasper.litebase.server.handler.ComQueryHandler;

public class CreateDataBaseStmtHandler extends ComQueryHandler<SQLCreateDatabaseStatement> {
    public CreateDataBaseStmtHandler(SchemaTableApi schemaTableApi) {
        super(schemaTableApi);
    }

    @Override
    protected void handle(BackendConnection c, Long queryId, String sql, SQLCreateDatabaseStatement statement) {
        GlobalConfig globalConfig = GlobalConfig.getInstance();
        SchemaDefinition schemaDefinition = new SchemaDefinition();
        schemaDefinition.setName(statement.getDatabaseName());
        schemaDefinition.setCharset(globalConfig.getCharset().toString());
        schemaDefinition.setEngineType(EngineType.PERSIST);
        schemaTableApi.createSchema(schemaDefinition);
    }
}
