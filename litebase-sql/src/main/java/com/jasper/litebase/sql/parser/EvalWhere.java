package com.jasper.litebase.sql.parser;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.visitor.SQLEvalVisitorUtils;
import com.alibaba.druid.util.JdbcConstants;

import java.util.List;

public class EvalWhere {
    public static boolean match(String expr, List<Object> parameters) {
        Object eval = SQLEvalVisitorUtils.evalExpr(JdbcConstants.MYSQL, expr, parameters);
        if (eval instanceof Boolean) {
            return (boolean) eval;
        } else {
            throw new IllegalArgumentException("eval failed, expr: " + expr + ", parameters: " + parameters);
        }
    }
}
