package com.jasper.litebase.engine.persist;

import com.jasper.litebase.engine.api.EngineApi;
import com.jasper.litebase.engine.domain.ResultSet;
import com.jasper.litebase.engine.persist.handler.QueryHandler;

import java.util.HashMap;
import java.util.Map;

public class PersistEngine extends EngineApi {
    private Map<String, Map<String, QueryHandler>> HANDLERS = new HashMap<>();

    @Override
    public ResultSet listSchemas() {
        return null;
    }

    @Override
    public ResultSet query(String schema, String table, String whereClause) {
        return HANDLERS.get(schema).get(table).query(schema, table, whereClause);
    }
}
