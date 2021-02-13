package com.jasper.litebase.engine.api;

import com.jasper.litebase.engine.domain.ExecutionContext;
import com.jasper.litebase.engine.domain.ResultSet;
import com.jasper.litebase.engine.domain.Table;

import java.util.List;

public abstract class StoreEngine {

    public abstract void init();

    public abstract ResultSet query(ExecutionContext context, Table table, List<String> selectItem, String whereClause);

    public abstract int dml(ExecutionContext context, DMLTemplate t, Table table);
}
