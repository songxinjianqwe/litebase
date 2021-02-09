package com.jasper.litebase.engine.domain;

import com.jasper.litebase.config.EngineType;

public class SchemaDefinition {
    private String name;
    private EngineType engineType;
    private String charset;

    public void setName(String name) {
        this.name = name;
    }

    public void setEngineType(EngineType engineType) {
        this.engineType = engineType;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getName() {
        return name;
    }

    public EngineType getEngineType() {
        return engineType;
    }

    public String getCharset() {
        return charset;
    }
}
