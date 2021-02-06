/*
 * Copyright 1999-2012 Alibaba Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jasper.litebase.server.protocol.server;

import com.jasper.litebase.server.protocol.MySQLPacket;
import io.netty.buffer.ByteBuf;

/** @author xianmao.hexm */
public class QuitPacket extends MySQLPacket {
  public static final byte[] QUIT = new byte[] {1, 0, 0, 0, 1};

  @Override
  public void writeToBuffer(ByteBuf buffer) {
    buffer.writeBytes(QUIT);
  }

  @Override
  public int calcPacketSize() {
    return 1;
  }

  @Override
  protected String getPacketInfo() {
    return "MySQL Quit Packet";
  }
}
