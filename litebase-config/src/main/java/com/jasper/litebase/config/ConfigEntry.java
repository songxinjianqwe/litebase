package com.jasper.litebase.config;

public class ConfigEntry<T> {
    private final String name;
    private T value;
    private boolean immutable = true;

    public ConfigEntry(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        if (immutable) {
            throw new IllegalArgumentException("cannot modify a immutable global config entry");
        }
        this.value = value;
    }
}
