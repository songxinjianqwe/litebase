package com.jasper.litebase.engine.domain;

public interface ResultSet {
    boolean next();

    Object getObject(int columnIndex);

    ResultSetMetaData getResultSetMetaData();
}
