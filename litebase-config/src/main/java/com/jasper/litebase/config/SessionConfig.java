package com.jasper.litebase.config;

import com.jasper.litebase.config.util.CharsetUtil;
import sun.security.krb5.Config;

public class SessionConfig {

    private long idleTimeout = 8 * 3600 * 1000L;

    // 原则: 数据库编码控制使用dbCharset来处理的，设计到Java相关的字符串编码解码采用charset来表示
    private String dbCharset = "utf8";
    private String charset = "utf8";
    private int charsetIndex = CharsetUtil.getDBIndex(charset);

    private ConfigEntry characterSetClient = new ConfigEntry("character_set_client", "utf8");

    // VARIABLES.put("character_set_connection", "utf8");
    // VARIABLES.put("character_set_results", "utf8");
    // VARIABLES.put("character_set_server", "utf8");
    // VARIABLES.put("init_connect", "");
    // VARIABLES.put("interactive_timeout", "172800");
    // VARIABLES.put("lower_case_table_names", "1");
    // VARIABLES.put("max_allowed_packet", "16777216");
    // VARIABLES.put("net_buffer_length", "8192");
    // VARIABLES.put("net_write_timeout", "60");
    // VARIABLES.put("query_cache_size", "0");
    // VARIABLES.put("query_cache_type", "OFF");
    // VARIABLES.put("sql_mode", "STRICT_TRANS_TABLES");
    // VARIABLES.put("system_time_zone", "CST");
    // VARIABLES.put("time_zone", "SYSTEM");
    // VARIABLES.put("tx_isolation", "REPEATABLE-READ");
    // VARIABLES.put("wait_timeout", "172800");

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
}
