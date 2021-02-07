package com.jasper.litebase.engine.api;

import com.jasper.litebase.engine.domain.ResultSet;

public abstract class EngineApi {
    public static EngineApi getInstance() {
        return null;
    }

    public abstract ResultSet listSchemas();

    public abstract ResultSet query(String schema, String table, String whereClause);
}
