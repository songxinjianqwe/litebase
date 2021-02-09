package com.jasper.litebase.server.handler.impl.ddl;

import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.jasper.litebase.config.EngineType;
import com.jasper.litebase.engine.api.SchemaTableApi;
import com.jasper.litebase.engine.domain.TableDefinition;
import com.jasper.litebase.server.connection.BackendConnection;
import com.jasper.litebase.server.handler.ComQueryHandler;

import java.util.List;

public class CreateTableStmtHandler extends ComQueryHandler<SQLCreateTableStatement> {
    public CreateTableStmtHandler(SchemaTableApi schemaTableApi) {
        super(schemaTableApi);
    }

    @Override
    protected void handle(BackendConnection c, Long queryId, String sql, SQLCreateTableStatement statement) {
        List<SQLColumnDefinition> columnDefinitions = statement.getColumnDefinitions();
        List<String> primaryKeyNames = statement.getPrimaryKeyNames();
        String simpleName = statement.getTableSource().getName().getSimpleName();
        TableDefinition tableDefinition = new TableDefinition();
        tableDefinition.setSchema(c.getSchema());
        tableDefinition.setTable(simpleName);
        tableDefinition.setEngineType(EngineType.PERSIST);
        tableDefinition.setPrimaryIndex(null);
        tableDefinition.setSecondaryIndexs(null);
        schemaTableApi.createTable(tableDefinition);
    }
}
