package com.jasper.litebase.config;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class GlobalConfig {
    private ConfigEntry<Integer> port = new ConfigEntry<>("port", 9306);
    private ConfigEntry<String> baseDir = new ConfigEntry<>("base_dir", "./");
    private ConfigEntry<Charset> charset = new ConfigEntry<>("charset", StandardCharsets.UTF_8);
    private ConfigEntry<Long> pageSize = new ConfigEntry<>("page_size", 4 * 1024L);
    private ConfigEntry<Long> bufferPoolSize = new ConfigEntry<>("buffer_pool_size", 4 * 1024 * 1024L);

    private GlobalConfig() {
    }

    private static final GlobalConfig INSTANCE = new GlobalConfig();

    public static GlobalConfig getInstance() {
        return INSTANCE;
    }

    public int getPort() {
        return port.getValue();
    }

    public String getBaseDir() {
        return baseDir.getValue();
    }

    public void setBaseDir(String baseDir) {
        this.baseDir.setValue(baseDir);
    }

    public Charset getCharset() {
        return charset.getValue();
    }

    public Long getPageSize() {
        return pageSize.getValue();
    }

    public Long getBufferPoolSize() {
        return bufferPoolSize.getValue();
    }
}
