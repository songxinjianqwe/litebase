package com.jasper.litebase.config;

import com.jasper.litebase.config.util.CharsetUtil;

public class SessionConfig {

  private long idleTimeout = 8 * 3600 * 1000L;

  // 原则: 数据库编码控制使用dbCharset来处理的，设计到Java相关的字符串编码解码采用charset来表示
  private String dbCharset = "utf8";
  private String charset = "utf8";
  private int charsetIndex = CharsetUtil.getDBIndex(charset);

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
