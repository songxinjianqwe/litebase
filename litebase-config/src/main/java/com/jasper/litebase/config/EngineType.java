package com.jasper.litebase.config;

public enum EngineType {
    MEMORY("123"), PERSIST("com.jasper.litebase.engine.persist.PersistStoreEngine");

    private String className;

    EngineType(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }
}
