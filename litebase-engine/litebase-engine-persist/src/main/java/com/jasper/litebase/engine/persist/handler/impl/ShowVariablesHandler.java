package com.jasper.litebase.engine.persist.handler.impl;

import com.jasper.litebase.config.constant.Fields;
import com.jasper.litebase.engine.domain.Field;
import com.jasper.litebase.engine.domain.ResultSet;
import com.jasper.litebase.engine.domain.ResultSetMetaData;
import com.jasper.litebase.engine.persist.handler.QueryHandler;

import java.util.ArrayList;
import java.util.List;

public class ShowVariablesHandler implements QueryHandler {
    private static final List<Field> FIELDS = new ArrayList<>();

    static {
        FIELDS.add(new Field("VARIABLE_NAME", Fields.FIELD_TYPE_VAR_STRING));
        FIELDS.add(new Field("VALUE", Fields.FIELD_TYPE_VAR_STRING));
    }

    @Override
    public ResultSet query(String schema, String table, String whereClause) {
        ResultSetMetaData resultSetMetaData = new ResultSetMetaData(FIELDS);
        return null;
    }
}
