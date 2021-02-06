package com.jasper.litebase.parser;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlLexer;
import com.alibaba.druid.sql.parser.Token;
import com.jasper.litebase.sql.parser.SQLParser;
import org.junit.Test;

public class ParserTest {
    @Test
    public void test() {
        SQLStatement statement = SQLParser.parse("select version()");
        SQLStatement statement1 = SQLParser.parse("show variables like '%123%'");
        System.out.println(statement);
        System.out.println(statement1);

        MySqlLexer lexer = new MySqlLexer("rollback");
        String s = lexer.stringVal();
    }
}
