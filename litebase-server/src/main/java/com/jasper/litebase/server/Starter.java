package com.jasper.litebase.server;

import com.jasper.litebase.config.GlobalConfig;

import java.net.UnknownHostException;

public class Starter {
    public static void main(String[] args) throws UnknownHostException, InterruptedException {
        new LiteBaseServer(new GlobalConfig()).run();
    }
}
