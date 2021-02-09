package com.jasper.litebase.engine.persist.bufferpool;

import com.jasper.litebase.config.GlobalConfig;
import com.jasper.litebase.engine.persist.domain.unit.Page;

import java.nio.Buffer;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class BufferPool {
    private BufferPool() {
    }

    private static final BufferPool INSTANCE = new BufferPool();

    public static BufferPool getInstance() {
        return INSTANCE;
    }

    private long maxBufferEntrySize = GlobalConfig.getInstance().getBufferPoolSize()
            / GlobalConfig.getInstance().getPageSize();

    /**
     * tableId -> page
     */
    private Map<Integer, Page> lruCache = new LinkedHashMap<Integer, Page>(16, 0.75f, true) {
        protected boolean removeEldestEntry(Map.Entry eldest) {
            return size() > maxBufferEntrySize;
        }
    };

    public Page load(Integer tableId) {
        Page page = new Page();
        lruCache.put(tableId, page);
        return page;
    }
}
