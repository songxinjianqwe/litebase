package com.jasper.litebase.config;

public class GlobalConfig {
    private int port = 9306;
    private EngineType engineType = EngineType.PERSIST;

    public int getPort() {
        return port;
    }

    public EngineType getEngineType() {
        return engineType;
    }
}
