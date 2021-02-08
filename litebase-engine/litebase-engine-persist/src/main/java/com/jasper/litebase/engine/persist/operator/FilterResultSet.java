package com.jasper.litebase.engine.persist.operator;

import com.jasper.litebase.engine.domain.ResultSet;
import com.jasper.litebase.engine.domain.ResultSetMetaData;

public class FilterResultSet implements ResultSet {
    private String whereClause;
    private ResultSet resultSet;

    public FilterResultSet(String whereClause, ResultSet resultSet) {
        this.whereClause = whereClause;
        this.resultSet = resultSet;
    }

    @Override
    public boolean next() {
        return resultSet.next();
    }

    @Override
    public Object getObject(int columnIndex) {
        return resultSet.getObject(columnIndex);
    }

    @Override
    public ResultSetMetaData getResultSetMetaData() {
        return resultSet.getResultSetMetaData();
    }
}
