package com.jasper.litebase.engine.domain;

import com.jasper.litebase.config.EngineType;

import java.util.List;

public class TableDefinition {
    private IndexDefinition primaryIndex;
    private List<IndexDefinition> secondaryIndexs;
    private List<Field> fields;
    private String schema;
    private String table;
    private EngineType engineType;

    public IndexDefinition getPrimaryIndex() {
        return primaryIndex;
    }

    public void setPrimaryIndex(IndexDefinition primaryIndex) {
        this.primaryIndex = primaryIndex;
    }

    public List<IndexDefinition> getSecondaryIndexs() {
        return secondaryIndexs;
    }

    public void setSecondaryIndexs(List<IndexDefinition> secondaryIndexs) {
        this.secondaryIndexs = secondaryIndexs;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public void setEngineType(EngineType engineType) {
        this.engineType = engineType;
    }

    public List<Field> getFields() {
        return fields;
    }

    public String getSchema() {
        return schema;
    }

    public String getTable() {
        return table;
    }

    public EngineType getEngineType() {
        return engineType;
    }
}
