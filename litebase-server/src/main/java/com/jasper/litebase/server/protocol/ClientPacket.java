package com.jasper.litebase.server.protocol;

public abstract class ClientPacket extends MySQLPacket {
  public abstract void resolve(byte[] data);
}
