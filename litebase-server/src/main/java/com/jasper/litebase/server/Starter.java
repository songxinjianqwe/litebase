package com.jasper.litebase.server;

import com.jasper.litebase.config.GlobalConfig;

public class Starter {
  private static LiteBaseServer server;

  public static void main(String[] args) {
    go();
  }

  public static synchronized void go() {
    if (server == null) {
      server = new LiteBaseServer(new GlobalConfig());
      server.run();
    }
  }
}
