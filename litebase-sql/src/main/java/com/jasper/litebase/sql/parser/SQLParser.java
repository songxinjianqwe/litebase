package com.jasper.litebase.sql.parser;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlLexer;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.parser.Lexer;
import com.alibaba.druid.sql.parser.Token;
import com.jasper.litebase.sql.parser.enumeration.QueryType;

public class SQLParser {

    public static QueryType parseQueryType(String sql) {
        MySqlLexer mySqlLexer = new MySqlLexer(sql);
        mySqlLexer.skipFirstHintsOrMultiCommentAndNextToken();
        Token token = mySqlLexer.token();
        QueryType queryType;
        if(token != null) {
            queryType = QueryType.getByToken(token);
        } else {
            queryType = QueryType.valueOf(mySqlLexer.stringVal().toUpperCase());
        }
        if(queryType == null) {
            throw new IllegalArgumentException("unsupported sql :" + sql);
        }
        return queryType;
    }

    public static SQLStatement parse(String sql) {
        MySqlStatementParser parser = new MySqlStatementParser(sql);
        return parser.parseStatement();
    }
}
