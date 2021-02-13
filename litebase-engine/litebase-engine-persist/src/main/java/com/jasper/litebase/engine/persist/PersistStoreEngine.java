package com.jasper.litebase.engine.persist;

import com.jasper.litebase.engine.api.DMLTemplate;
import com.jasper.litebase.engine.api.StoreEngine;
import com.jasper.litebase.engine.domain.*;
import com.jasper.litebase.engine.persist.handler.QueryHandler;
import com.jasper.litebase.engine.persist.handler.impl.CommonQueryHandler;
import com.jasper.litebase.engine.persist.handler.impl.ShowVariablesQueryHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersistStoreEngine extends StoreEngine {
    private Map<String, Map<String, QueryHandler>> handlers = new HashMap<>();
    private QueryHandler commonQueryHandler = new CommonQueryHandler();

    @Override
    public void init() {
        registerQueryHandler("performance_schema", "session_variables", new ShowVariablesQueryHandler());
    }

    @Override
    public int dml(ExecutionContext context, DMLTemplate t, Table table) {
        return t.execute();
    }

    @Override
    public ResultSet query(ExecutionContext context, Table table, List<String> selectItems, String whereClause) {
        TableDefinition tableDefinition = table.getTableDefinition();
        String schema = tableDefinition.getSchema();
        String tableName = tableDefinition.getTable();
        if (!handlers.containsKey(schema) || !handlers.get(schema).containsKey(tableName)) {
            return commonQueryHandler.query(context, schema, tableName, selectItems, whereClause);
        }
        return handlers.get(schema).get(tableName).query(context, schema, tableName, selectItems, whereClause);
    }

    private void registerQueryHandler(String schema, String table, QueryHandler handler) {
        if (!handlers.containsKey(schema)) {
            handlers.put(schema, new HashMap<>());
        }
        Map<String, QueryHandler> tableToHandler = handlers.get(schema);
        tableToHandler.put(table, handler);
    }
}
