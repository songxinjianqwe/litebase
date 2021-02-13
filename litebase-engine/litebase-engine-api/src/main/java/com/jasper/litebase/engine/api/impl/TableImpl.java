package com.jasper.litebase.engine.api.impl;

import com.jasper.litebase.engine.api.DMLTemplate;
import com.jasper.litebase.engine.api.StoreEngine;
import com.jasper.litebase.engine.domain.ExecutionContext;
import com.jasper.litebase.engine.domain.ResultSet;
import com.jasper.litebase.engine.domain.Table;
import com.jasper.litebase.engine.domain.TableDefinition;

import java.io.File;
import java.io.FileInputStream;
import java.nio.channels.FileChannel;
import java.util.List;

public class TableImpl implements Table {
    private TableDefinition tableDefinition;
    private File defFile;
    private File dataFile;
    private FileChannel channel;
    private StoreEngine engine;

    public TableImpl(TableDefinition tableDefinition) {
        this.tableDefinition = tableDefinition;
    }

    @Override
    public void init() {
        try {
            channel = new FileInputStream(dataFile).getChannel();
            engine = EngineManager.getInstance(tableDefinition.getEngineType());
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    @Override
    public TableDefinition getTableDefinition() {
        return tableDefinition;
    }

    public FileChannel getChannel() {
        return channel;
    }

    @Override
    public void close() {
        try {
            channel.close();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    @Override
    public ResultSet query(ExecutionContext context, List<String> selectItem, String whereClause) {
        return engine.query(context, this, selectItem, whereClause);
    }

    @Override
    public int dml(ExecutionContext context, DMLTemplate t) {
        return engine.dml(context, t, this);
    }
}
