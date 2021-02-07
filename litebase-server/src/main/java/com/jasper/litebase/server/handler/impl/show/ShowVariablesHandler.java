package com.jasper.litebase.server.handler.impl.show;

import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowVariantsStatement;
import com.jasper.litebase.engine.api.EngineApi;
import com.jasper.litebase.engine.domain.ResultSet;
import com.jasper.litebase.server.connection.BackendConnection;
import com.jasper.litebase.server.handler.ComQueryHandler;

public class ShowVariablesHandler extends ComQueryHandler<MySqlShowVariantsStatement> {

    @Override
    public void handle(BackendConnection c, String sql, MySqlShowVariantsStatement statement) {
        super.writeBackResultSet(c, sql, statement);
    }

    @Override
    protected ResultSet getResultSet(BackendConnection c, String sql, MySqlShowVariantsStatement statement) {
        String where = null;
        if (statement.getWhere() != null) {
            where = statement.getWhere().toString();
            // predicate = element -> EvalWhere.match(statement.getWhere().toString().replaceAll("Variable_name", "?"),
            // Collections.singletonList(element.get(0)));
        } else if (statement.getLike() != null) {
            where = "variable_name like " + statement.getLike().toString();
            // predicate = element -> EvalWhere.match("? like " + statement.getLike().toString(),
            // Collections.singletonList(element.get(0)));
            // predicate = element -> {
            // String input = (String) element.get(0);
            // String expr = statement.getLike().toString();
            // expr = expr.toLowerCase(); // ignoring locale for now
            // expr = expr.replace(".", "\\."); // "\\" is escaped to "\" (thanks, Alan M)
            // // ... escape any other potentially problematic characters here
            // expr = expr.replace("?", ".");
            // expr = expr.replace("%", ".*");
            // input = input.toLowerCase();
            // return input.matches(expr);
            // };
        }
        return EngineApi.getInstance().query("performance_schema", "session_variables", where);
    }
}
