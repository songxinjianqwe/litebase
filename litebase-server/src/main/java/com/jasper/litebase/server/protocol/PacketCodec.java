package com.jasper.litebase.server.protocol;

public class PacketCodec {

    public static <T extends ClientPacket> T decode(Class<T> type, byte[] data) {
        try {
            T result = type.newInstance();
            result.resolve(data);
            return result;
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}
