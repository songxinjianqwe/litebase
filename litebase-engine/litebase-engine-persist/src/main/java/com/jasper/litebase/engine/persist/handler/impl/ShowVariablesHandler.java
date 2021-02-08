package com.jasper.litebase.engine.persist.handler.impl;

import com.jasper.litebase.config.ConfigEntry;
import com.jasper.litebase.config.constant.Fields;
import com.jasper.litebase.engine.domain.ExecutionContext;
import com.jasper.litebase.engine.domain.Field;
import com.jasper.litebase.engine.domain.ResultSet;
import com.jasper.litebase.engine.domain.ResultSetMetaData;
import com.jasper.litebase.engine.persist.handler.QueryHandler;
import com.jasper.litebase.engine.persist.operator.DefaultResultSet;
import com.jasper.litebase.engine.persist.operator.FilterResultSet;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ShowVariablesHandler implements QueryHandler {
    private static final List<Field> FIELDS = new ArrayList<>();

    static {
        FIELDS.add(new Field("VARIABLE_NAME", Fields.FIELD_TYPE_VAR_STRING));
        FIELDS.add(new Field("VALUE", Fields.FIELD_TYPE_VAR_STRING));
    }

    @Override
    public ResultSet query(ExecutionContext context, String schema, String table, String whereClause) {
        ResultSetMetaData resultSetMetaData = new ResultSetMetaData(FIELDS);
        List<ConfigEntry> configEntries = context.getSessionConfig().listAll();
        List<List<Object>> resultList = configEntries.stream().map(c -> {
            List<Object> entry = new ArrayList<>();
            entry.add(c.getName());
            entry.add(c.getValue());
            return entry;
        }).collect(Collectors.toList());
        DefaultResultSet basicResultSet = new DefaultResultSet(resultSetMetaData, resultList.iterator());
        if (StringUtils.isEmpty(whereClause)) {
            return basicResultSet;
        } else {
            return new FilterResultSet(whereClause, basicResultSet);
        }
    }
}
