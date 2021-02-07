package com.jasper.litebase.engine.domain;

public class Field {
    private String name;
    private int type;

    public Field(String name, int type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }
}
