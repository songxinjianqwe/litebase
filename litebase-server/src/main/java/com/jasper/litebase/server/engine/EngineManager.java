package com.jasper.litebase.server.engine;

import com.jasper.litebase.config.EngineType;
import com.jasper.litebase.config.GlobalConfig;
import com.jasper.litebase.engine.api.StoreEngine;

import java.util.HashMap;
import java.util.Map;

public class EngineManager {
    private static Map<EngineType, StoreEngine> ENGINES = new HashMap<>();

    public static synchronized StoreEngine getInstance(EngineType engineType) {
        if (!ENGINES.containsKey(engineType)) {
            try {
                Class<?> aClass = Class.forName(engineType.getClassName());
                StoreEngine engine = (StoreEngine) aClass.newInstance();
                engine.init();
                ENGINES.put(engineType, engine);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return ENGINES.get(engineType);
    }
}
