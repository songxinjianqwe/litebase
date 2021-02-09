package com.jasper.litebase.engine.persist;

import com.jasper.litebase.engine.api.StoreEngine;
import com.jasper.litebase.engine.domain.ExecutionContext;
import com.jasper.litebase.engine.domain.ResultSet;
import com.jasper.litebase.engine.domain.TableDefinition;
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
    public ResultSet query(ExecutionContext context, TableDefinition tableDefinition, List<String> selectItems,
            String whereClause) {
        String schema = tableDefinition.getSchema();
        String table = tableDefinition.getTable();
        if (!handlers.containsKey(schema) || !handlers.get(schema).containsKey(table)) {
            return commonQueryHandler.query(context, schema, table, selectItems, whereClause);
        }
        return handlers.get(schema).get(table).query(context, schema, table, selectItems, whereClause);
    }

    @Override
    public int insert(ExecutionContext context, TableDefinition tableDefinition, List<Object> values) {
        return 0;
    }

    @Override
    public int update(ExecutionContext context, TableDefinition tableDefinition, Map<String, Object> updateItems,
            String whereClause) {
        return 0;
    }

    @Override
    public int delete(ExecutionContext context, TableDefinition tableDefinition, String whereClause) {
        return 0;
    }

    private void registerQueryHandler(String schema, String table, QueryHandler handler) {
        if (!handlers.containsKey(schema)) {
            handlers.put(schema, new HashMap<>());
        }
        Map<String, QueryHandler> tableToHandler = handlers.get(schema);
        tableToHandler.put(table, handler);
    }
}
