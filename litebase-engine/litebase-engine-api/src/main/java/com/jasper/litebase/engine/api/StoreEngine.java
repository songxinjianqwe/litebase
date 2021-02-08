package com.jasper.litebase.engine.api;

import com.jasper.litebase.config.EngineType;
import com.jasper.litebase.engine.domain.ExecutionContext;
import com.jasper.litebase.engine.domain.ResultSet;

import java.util.HashMap;
import java.util.Map;

public abstract class StoreEngine {

    public abstract void init();

    public abstract ResultSet listSchemas();

    public abstract ResultSet query(ExecutionContext context, String schema, String table, String whereClause);
}
