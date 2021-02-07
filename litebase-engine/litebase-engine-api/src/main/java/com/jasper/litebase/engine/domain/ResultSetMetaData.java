package com.jasper.litebase.engine.domain;

import java.util.List;

public class ResultSetMetaData {
    private List<Field> fields;

    public ResultSetMetaData(List<Field> fields) {
        this.fields = fields;
    }

    public int getFieldCount() {
        return fields.size();
    }

    public List<Field> getFields() {
        return fields;
    }
}
