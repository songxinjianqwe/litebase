package com.jasper.litebase.server.handler.impl.show;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.*;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowVariantsStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitorAdapter;
import com.jasper.litebase.config.util.LiteBaseStringUtil;
import com.jasper.litebase.server.connection.BackendConnection;
import com.jasper.litebase.server.handler.ComQueryHandler;
import com.jasper.litebase.server.protocol.constant.Fields;
import com.jasper.litebase.sql.parser.EvalWhere;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ShowVariablesHandler extends ComQueryHandler<MySqlShowVariantsStatement> {
    private static final LinkedHashMap<String, Integer> FIELDS = new LinkedHashMap<>();

    static {
        FIELDS.put("VARIABLE_NAME", Fields.FIELD_TYPE_VAR_STRING);
        FIELDS.put("VALUE", Fields.FIELD_TYPE_VAR_STRING);
    }

    @Override
    public void handle(BackendConnection c, String sql, MySqlShowVariantsStatement statement) {
        super.writeBackResultSet(c, sql, statement);
    }

    @Override
    protected int getFieldCount() {
        return FIELDS.size();
    }

    @Override
    protected LinkedHashMap<String, Integer> getFields() {
        return FIELDS;
    }

    @Override
    protected List<List<Object>> getRows(BackendConnection c, String sql, MySqlShowVariantsStatement statement) {
        Predicate<List<Object>> predicate;
        if (statement.getWhere() != null) {
            predicate = element -> EvalWhere.match(statement.getWhere().toString().replaceAll("Variable_name", "?"),
                    Collections.singletonList(element.get(0)));
        } else if (statement.getLike() != null) {
            predicate = element -> EvalWhere.match("? like " + statement.getLike().toString(),
                    Collections.singletonList(element.get(0)));
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
        } else {
            predicate = e -> true;
        }
        return VARIABLES.entrySet().stream().map(e -> {
            List<Object> ele = new ArrayList<>();
            ele.add(e.getKey());
            ele.add(e.getValue());
            return ele;
        }).filter(predicate).collect(Collectors.toList());
    }

    private static final Map<String, String> VARIABLES = new HashMap<>();

    static {
        VARIABLES.put("character_set_client", "utf8");
        VARIABLES.put("character_set_connection", "utf8");
        VARIABLES.put("character_set_results", "utf8");
        VARIABLES.put("character_set_server", "utf8");
        VARIABLES.put("init_connect", "");
        VARIABLES.put("interactive_timeout", "172800");
        VARIABLES.put("lower_case_table_names", "1");
        VARIABLES.put("max_allowed_packet", "16777216");
        VARIABLES.put("net_buffer_length", "8192");
        VARIABLES.put("net_write_timeout", "60");
        VARIABLES.put("query_cache_size", "0");
        VARIABLES.put("query_cache_type", "OFF");
        VARIABLES.put("sql_mode", "STRICT_TRANS_TABLES");
        VARIABLES.put("system_time_zone", "CST");
        VARIABLES.put("time_zone", "SYSTEM");
        VARIABLES.put("tx_isolation", "REPEATABLE-READ");
        VARIABLES.put("wait_timeout", "172800");
    }
}
