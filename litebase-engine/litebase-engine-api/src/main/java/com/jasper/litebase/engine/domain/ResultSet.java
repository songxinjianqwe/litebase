package com.jasper.litebase.engine.domain;

public class ResultSet {
    private ResultSetMetaData resultSetMetaData;

    public boolean next() {
        return false;
    }

    public Object getObject(int columnIndex) {
        return null;
    }

    public ResultSetMetaData getResultSetMetaData() {
        return resultSetMetaData;
    }
}
