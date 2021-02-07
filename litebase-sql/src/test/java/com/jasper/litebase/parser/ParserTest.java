package com.jasper.litebase.parser;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowVariantsStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlLexer;
import com.alibaba.druid.sql.parser.Token;
import com.jasper.litebase.sql.parser.SQLParser;
import org.junit.Test;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ParserTest {
    @Test
    public void test() {
        {
            SQLStatement statement = SQLParser.parse("select version()");
            expected(statement, SQLSelectStatement.class);
        }
        {
            SQLStatement statement = SQLParser.parse("show variables like '%123%'");
            expected(statement, MySqlShowVariantsStatement.class);
        }
        {
            SQLStatement statement = SQLParser.parse("select database()");
            expected(statement, SQLSelectStatement.class);
        }
        {
            SQLStatement statement = SQLParser.parse("show variables where variable_name like '%123%'");
            expected(statement, SQLSelectStatement.class);
        }
    }

    private void expected(SQLStatement statement, Class cls) {
        System.out.println(statement.getClass().getName());
        assertSame(statement.getClass(), cls);
    }
}
