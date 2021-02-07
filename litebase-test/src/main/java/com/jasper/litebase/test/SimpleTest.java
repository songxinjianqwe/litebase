package com.jasper.litebase.test;

import com.jasper.litebase.server.Starter;
import java.sql.*;
import org.junit.Test;

public class SimpleTest {
    static {
        Starter.go();
    }

    @Test
    public void test() throws ClassNotFoundException, SQLException, InterruptedException {
        Thread.sleep(1000);
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost:9306/information_schema";
        String username = "root";
        String password = "123456";
        Connection conn = DriverManager.getConnection(url, username, password);
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery("select database()");
        ResultSetMetaData metaData = resultSet.getMetaData();
        for (int i = 0; i < metaData.getColumnCount(); i++) {
            String columnClassName = metaData.getColumnClassName(i);
            String columnName = metaData.getColumnName(i);
            int columnType = metaData.getColumnType(i);
            String columnLabel = metaData.getColumnLabel(i);
            System.out.println(columnClassName);
            System.out.println(columnName);
            System.out.println(columnType);
            System.out.println(columnLabel);
        }
        while (resultSet.next()) {
            String col1 = resultSet.getString(1);
            System.out.println(col1);
        }
    }
}
