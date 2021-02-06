package com.jasper.litebase.server.util;

import java.io.UnsupportedEncodingException;

public class LiteBaseStringUtil {
    public static byte[] encode(String src, String charset) {
        if (src == null) {
            return null;
        }
        if (charset == null) {
            return src.getBytes();
        }
        try {
            return src.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            return src.getBytes();
        }
    }
}
