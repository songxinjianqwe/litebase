package com.jasper.litebase.engine.persist.operator;

import com.jasper.litebase.engine.domain.ResultSet;
import com.jasper.litebase.engine.domain.ResultSetMetaData;

import java.util.Iterator;
import java.util.List;

public class DefaultResultSet implements ResultSet {
    private ResultSetMetaData resultSetMetaData;
    private Iterator<List<Object>> iterator;
    private List<Object> current;

    public DefaultResultSet(ResultSetMetaData resultSetMetaData, Iterator<List<Object>> iterator) {
        this.resultSetMetaData = resultSetMetaData;
        this.iterator = iterator;
    }

    @Override
    public boolean next() {
        if (iterator.hasNext()) {
            current = iterator.next();
            return true;
        } else {
            current = null;
            return false;
        }
    }

    @Override
    public Object getObject(int columnIndex) {
        return current.get(columnIndex - 1);
    }

    @Override
    public ResultSetMetaData getResultSetMetaData() {
        return resultSetMetaData;
    }
}
