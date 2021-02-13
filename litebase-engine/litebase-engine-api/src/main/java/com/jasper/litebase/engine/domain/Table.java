package com.jasper.litebase.engine.domain;

import com.jasper.litebase.engine.api.DMLTemplate;

import java.util.List;

public interface Table {
    void init();

    TableDefinition getTableDefinition();

    void close();

    ResultSet query(ExecutionContext context, List<String> selectItem, String whereClause);

    int dml(ExecutionContext context, DMLTemplate t);
}
