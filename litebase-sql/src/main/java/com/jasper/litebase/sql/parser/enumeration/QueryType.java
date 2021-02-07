package com.jasper.litebase.sql.parser.enumeration;

import com.alibaba.druid.sql.parser.Token;

import java.util.HashMap;
import java.util.Map;

public enum QueryType {
    BEGIN(Token.BEGIN), COMMIT(Token.COMMIT), DELETE(Token.DELETE), INSERT(Token.INSERT), REPLACE(
            Token.REPLACE), ROLLBACK(null), SELECT(Token.SELECT), SET(
                    Token.SET), SHOW(Token.SHOW), START(Token.START), UPDATE(Token.UPDATE), USE(Token.USE),;

    private Token token;

    QueryType(Token token) {
        this.token = token;
    }

    private static Map<Token, QueryType> MAPPING = new HashMap<>();

    static {
        for (QueryType q : values()) {
            MAPPING.put(q.token, q);
        }
    }

    public static QueryType getByToken(Token token) {
        return MAPPING.get(token);
    }
}
