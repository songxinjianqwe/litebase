package com.jasper.litebase.engine.persist;

import com.jasper.litebase.engine.api.StoreEngine;
import com.jasper.litebase.engine.domain.ExecutionContext;
import com.jasper.litebase.engine.domain.ResultSet;
import com.jasper.litebase.engine.persist.handler.QueryHandler;
import com.jasper.litebase.engine.persist.handler.impl.ShowVariablesHandler;

import java.util.HashMap;
import java.util.Map;

public class PersistStoreEngine extends StoreEngine {
    private Map<String, Map<String, QueryHandler>> handlers = new HashMap<>();

    private void register(String schema, String table, QueryHandler handler) {
        if (!handlers.containsKey(schema)) {
            handlers.put(schema, new HashMap<>());
        }
        Map<String, QueryHandler> tableToHandler = handlers.get(schema);
        tableToHandler.put(table, handler);
    }

    @Override
    public void init() {
        register("performance_schema", "session_variables", new ShowVariablesHandler());
    }

    @Override
    public ResultSet listSchemas() {
        return null;
    }

    @Override
    public ResultSet query(ExecutionContext context, String schema, String table, String whereClause) {
        return handlers.get(schema).get(table).query(context, schema, table, whereClause);
    }
}
