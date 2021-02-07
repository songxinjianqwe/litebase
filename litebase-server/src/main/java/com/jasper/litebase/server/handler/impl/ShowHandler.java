package com.jasper.litebase.server.handler.impl;

import com.jasper.litebase.server.connection.BackendConnection;
import com.jasper.litebase.server.handler.ComQueryHandler;
import com.jasper.litebase.sql.parser.enumeration.QueryType;

public class ShowHandler extends ComQueryHandler {
    @Override
    protected QueryType operation() {
        return QueryType.SHOW;
    }

    @Override
    public void handle(BackendConnection c, String sql) {

    }
}
