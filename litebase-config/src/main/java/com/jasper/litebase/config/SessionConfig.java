package com.jasper.litebase.config;

import com.jasper.litebase.config.util.CharsetUtil;
import sun.security.krb5.Config;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SessionConfig {

    private long idleTimeout = 8 * 3600 * 1000L;

    // 原则: 数据库编码控制使用dbCharset来处理的，设计到Java相关的字符串编码解码采用charset来表示
    private String dbCharset = "utf8";
    private String charset = "utf8";
    private int charsetIndex = CharsetUtil.getDBIndex(charset);

    private ConfigEntry characterSetClient = new ConfigEntry("character_set_client", "utf8");
    private ConfigEntry characterSetConnection = new ConfigEntry("character_set_connection", "utf8");
    private ConfigEntry characterSetResults = new ConfigEntry("character_set_results", "utf8");
    private ConfigEntry characterSetServer = new ConfigEntry("character_set_server", "utf8");
    private ConfigEntry initConnect = new ConfigEntry("init_connect", "");
    private ConfigEntry interactiveTimeout = new ConfigEntry("interactive_timeout", "172800");
    private ConfigEntry lowerCaseTableNames = new ConfigEntry("lower_case_table_names", "1");
    private ConfigEntry maxAllowedPacket = new ConfigEntry("max_allowed_packet", "16777216");
    private ConfigEntry netBufferLength = new ConfigEntry("net_buffer_length", "8192");
    private ConfigEntry netWriteTimeout = new ConfigEntry("net_write_timeout", "60");
    private ConfigEntry queryCacheSize = new ConfigEntry("query_cache_size", "0");
    private ConfigEntry queryCacheType = new ConfigEntry("query_cache_type", "OFF");
    private ConfigEntry sqlMode = new ConfigEntry("sql_mode", "STRICT_TRANS_TABLES");
    private ConfigEntry systemTimeZone = new ConfigEntry("system_time_zone", "CST");
    private ConfigEntry timeZone = new ConfigEntry("time_zone", "SYSTEM");
    private ConfigEntry txIsolation = new ConfigEntry("tx_isolation", "REPEATABLE-READ");
    private ConfigEntry waitTimeout = new ConfigEntry("wait_timeout", "172800");

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

    public boolean setCharset(String charset) {
        int ci = CharsetUtil.getDBIndex(charset);
        if (ci > 0) {
            this.charset = CharsetUtil.getCharset(ci);
            this.dbCharset = charset;
            this.charsetIndex = ci;
            return true;
        } else {
            return false;
        }
    }

    public int getCharsetIndex() {
        return charsetIndex;
    }

    public boolean setCharsetIndex(int charsetIndex) {
        String charset = CharsetUtil.getDbCharset(charsetIndex);
        if (charset != null) {
            this.dbCharset = charset;
            this.charset = CharsetUtil.getCharset(charsetIndex);
            this.charsetIndex = charsetIndex;
            return true;
        } else {
            return false;
        }
    }

    public List<ConfigEntry> listAll() {
        Field[] fields = SessionConfig.class.getDeclaredFields();
        return Arrays.stream(fields).filter(f -> f.getType() == ConfigEntry.class).map(f -> {
            f.setAccessible(true);
            try {
                return (ConfigEntry) f.get(this);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }
}
