package com.jasper.litebase.config;

public class SessionConfig {

    private long idleTimeout;

    // 原则: 数据库编码控制使用dbCharset来处理的，设计到Java相关的字符串编码解码采用charset来表示
    private String dbCharset;
    private String charset;

    public long getIdleTimeout() {
        return idleTimeout;
    }

    public void setIdleTimeout(long idleTimeout) {
        this.idleTimeout = idleTimeout;
    }

    public String getDbCharset() {
        return dbCharset;
    }

    public void setDbCharset(String dbCharset) {
        this.dbCharset = dbCharset;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }
}
