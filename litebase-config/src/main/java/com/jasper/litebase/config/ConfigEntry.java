package com.jasper.litebase.config;

public class ConfigEntry {
    private final String name;
    private String value;

    public ConfigEntry(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
