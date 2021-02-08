package com.jasper.litebase.engine.persist.handler;

import com.jasper.litebase.engine.domain.ExecutionContext;
import com.jasper.litebase.engine.domain.ResultSet;

public interface QueryHandler {
    ResultSet query(ExecutionContext context, String schema, String table, String whereClause);
}
