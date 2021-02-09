package com.jasper.litebase.engine.api;

import com.jasper.litebase.engine.domain.ExecutionContext;
import com.jasper.litebase.engine.domain.ResultSet;
import com.jasper.litebase.engine.domain.TableDefinition;

import java.util.List;
import java.util.Map;

public abstract class StoreEngine {

    public abstract void init();

    public abstract ResultSet query(ExecutionContext context, TableDefinition tableDefinition, List<String> selectItem,
            String whereClause);

    public abstract int insert(ExecutionContext context, TableDefinition tableDefinition, List<Object> values);

    public abstract int update(ExecutionContext context, TableDefinition tableDefinition,
            Map<String, Object> updateItems, String whereClause);

    public abstract int delete(ExecutionContext context, TableDefinition tableDefinition, String whereClause);
}
