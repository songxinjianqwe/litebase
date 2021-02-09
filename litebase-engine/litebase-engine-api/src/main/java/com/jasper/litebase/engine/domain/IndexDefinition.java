package com.jasper.litebase.engine.domain;

import java.util.List;

public class IndexDefinition {
    private IndexType type;
    private List<Field> fields;

    public IndexDefinition(IndexType type, List<Field> fields) {
        this.type = type;
        this.fields = fields;
    }
}
