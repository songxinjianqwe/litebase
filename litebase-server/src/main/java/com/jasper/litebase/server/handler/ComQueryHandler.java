package com.jasper.litebase.server.handler;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.jasper.litebase.server.connection.BackendConnection;
import com.jasper.litebase.server.handler.impl.SelectHandler;
import com.jasper.litebase.server.handler.impl.ShowHandler;
import com.jasper.litebase.sql.parser.SQLParser;
import com.jasper.litebase.sql.parser.enumeration.QueryType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ComQueryHandler {
    private static Map<QueryType, ComQueryHandler> HANDLERS = new ConcurrentHashMap<>();

    static {
        HANDLERS.put(QueryType.SELECT, new SelectHandler());
        HANDLERS.put(QueryType.SHOW, new ShowHandler());
    }

    public static void query(BackendConnection c, String sql) {
        QueryType queryType = SQLParser.parseQueryType(sql);
        HANDLERS.get(queryType).handle(c, sql);
    }

    public abstract void handle(BackendConnection c, String sql);

    protected abstract QueryType operation();
}
