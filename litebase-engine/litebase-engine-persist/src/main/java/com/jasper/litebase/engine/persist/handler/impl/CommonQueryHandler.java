package com.jasper.litebase.engine.persist.handler.impl;

import com.jasper.litebase.engine.domain.ExecutionContext;
import com.jasper.litebase.engine.domain.ResultSet;
import com.jasper.litebase.engine.persist.handler.QueryHandler;

import java.util.List;

public class CommonQueryHandler implements QueryHandler {

    @Override
    public ResultSet query(ExecutionContext context, String schema, String table, List<String> selectItems,
            String whereClause) {
        return null;
    }
}
