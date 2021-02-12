package com.jasper.litebase.engine.api.impl;

import com.alibaba.fastjson.JSON;
import com.jasper.litebase.config.GlobalConfig;
import com.jasper.litebase.engine.api.SchemaTableApi;
import com.jasper.litebase.engine.domain.SchemaDefinition;
import com.jasper.litebase.engine.domain.Table;
import com.jasper.litebase.engine.domain.TableDefinition;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultSchemaTableApi implements SchemaTableApi {
    private Map<String, Table> tableDefCache = new ConcurrentHashMap<>();
    private static final String SCHEMA_DEF_FILENAME = "db.opt";
    private static final String DATA_BASE_DIR = "data";
    private static final String TABLE_DEFINITION_SUFFIX = ".frm";
    private static final String TABLE_DATA_SUFFIX = ".ibd";
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSchemaTableApi.class);

    private String getCacheKey(TableDefinition tableDefinition) {
        return getCacheKey(tableDefinition.getSchema(), tableDefinition.getTable());
    }

    private String getCacheKey(String schema, String table) {
        return schema + "#" + table;
    }

    @Override
    public void createTable(TableDefinition tableDefinition) {
        try {
            GlobalConfig globalConfig = GlobalConfig.getInstance();
            File defFile = Paths.get(globalConfig.getBaseDir(), DATA_BASE_DIR, tableDefinition.getSchema(),
                    tableDefinition.getTable() + TABLE_DEFINITION_SUFFIX).toFile();
            if (defFile.exists() || defFile.isDirectory()) {
                throw new IllegalStateException(
                        "table already exists for def file exists: " + JSON.toJSONString(tableDefinition));
            }
            if (!defFile.createNewFile()) {
                throw new IllegalStateException("create def file failed: " + JSON.toJSONString(tableDefinition));
            }
            String def = JSON.toJSONString(tableDefinition);
            IOUtils.write(def, new FileOutputStream(defFile), globalConfig.getCharset());
            File dataFile = Paths.get(globalConfig.getBaseDir(), DATA_BASE_DIR, tableDefinition.getSchema(),
                    tableDefinition.getTable() + TABLE_DATA_SUFFIX).toFile();
            if (dataFile.exists() || dataFile.isDirectory()) {
                throw new IllegalStateException(
                        "table already exists for data file exists: " + JSON.toJSONString(tableDefinition));
            }
            if (!dataFile.createNewFile()) {
                throw new IllegalStateException("create data file failed: " + JSON.toJSONString(tableDefinition));
            }
            Table table = new Table(tableDefinition, new FileInputStream(dataFile).getChannel());
            tableDefCache.put(getCacheKey(tableDefinition), table);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
        LOGGER.info("create table succeeded: {}", JSON.toJSONString(tableDefinition));
    }

    @Override
    public void dropTable(TableDefinition tableDefinition) {
        GlobalConfig globalConfig = GlobalConfig.getInstance();
        File defFile = Paths.get(globalConfig.getBaseDir(), DATA_BASE_DIR, tableDefinition.getSchema(),
                tableDefinition.getTable() + TABLE_DEFINITION_SUFFIX).toFile();
        if (defFile.exists()) {
            if (!defFile.delete()) {
                throw new IllegalArgumentException("drop table def file failed: " + JSON.toJSONString(tableDefinition));
            }
        } else {
            LOGGER.warn("table def file {} not found, do not need to drop", JSON.toJSONString(tableDefinition));
        }
        File dataFile = Paths.get(globalConfig.getBaseDir(), DATA_BASE_DIR, tableDefinition.getSchema(),
                tableDefinition.getTable() + TABLE_DATA_SUFFIX).toFile();
        if (dataFile.exists()) {
            if (!dataFile.delete()) {
                throw new IllegalArgumentException(
                        "drop table data file failed: " + JSON.toJSONString(tableDefinition));
            }
        } else {
            LOGGER.warn("table data file {} not found, do not need to drop", JSON.toJSONString(tableDefinition));
        }
        LOGGER.info("drop table succeeded: {}", JSON.toJSONString(tableDefinition));
        tableDefCache.remove(getCacheKey(tableDefinition));
    }

    @Override
    public Table openTable(String schema, String table) {
        String cacheKey = getCacheKey(schema, table);
        try {
            if (tableDefCache.containsKey(cacheKey)) {
                return tableDefCache.get(cacheKey);
            }
            // 这块代码对同一个table而言是有可能重入的，以第一个结束的为准，后面结束的将自己对应的channel再关掉
            GlobalConfig globalConfig = GlobalConfig.getInstance();
            File defFile = Paths.get(globalConfig.getBaseDir(), DATA_BASE_DIR, schema, table + TABLE_DEFINITION_SUFFIX)
                    .toFile();
            if (!defFile.exists() || defFile.isDirectory()) {
                throw new IllegalStateException("table def file not found or directory: " + schema + "#" + table);
            }
            File dataFile = Paths.get(globalConfig.getBaseDir(), DATA_BASE_DIR, schema, table + TABLE_DATA_SUFFIX)
                    .toFile();
            if (!dataFile.exists() || dataFile.isDirectory()) {
                throw new IllegalStateException("table data file not found or directory: " + schema + "#" + table);
            }
            String def = IOUtils.toString(new FileReader(defFile));
            TableDefinition tableDefinition = JSON.parseObject(def, TableDefinition.class);
            LOGGER.info("open table succeeded: {}", JSON.toJSONString(tableDefinition));
            Table result = new Table(tableDefinition, new FileInputStream(dataFile).getChannel());
            // 自己放成功的话返回的是null。否则返回的是map中的值
            if (tableDefCache.putIfAbsent(cacheKey, result) != null) {
                result.close();
            }
            return result;
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    @Override
    public void closeTable(Table table) {
        Table t = tableDefCache.remove(getCacheKey(table.getTableDefinition()));
        if (t != null) {
            t.close();
        }
    }

    @Override
    public void createSchema(SchemaDefinition schemaDefinition) {
        try {
            GlobalConfig globalConfig = GlobalConfig.getInstance();
            File schemaDir = Paths.get(globalConfig.getBaseDir(), DATA_BASE_DIR, schemaDefinition.getName()).toFile();
            if (schemaDir.exists() || schemaDir.isFile()) {
                throw new IllegalStateException(
                        "schema already exists or file: " + JSON.toJSONString(schemaDefinition));
            }
            if (!schemaDir.mkdirs()) {
                throw new IllegalStateException("create schema dir failed: " + JSON.toJSONString(schemaDefinition));
            }
            File schemaDefFile = Paths
                    .get(globalConfig.getBaseDir(), DATA_BASE_DIR, schemaDefinition.getName(), SCHEMA_DEF_FILENAME)
                    .toFile();
            if (!schemaDefFile.createNewFile()) {
                throw new IllegalStateException("create schema def file failed");
            }
            String def = JSON.toJSONString(schemaDefinition);
            IOUtils.write(def, new FileOutputStream(schemaDefFile), globalConfig.getCharset());
            LOGGER.info("create schema succeeded: {}", JSON.toJSONString(schemaDefinition));
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    @Override
    public SchemaDefinition openSchema(String schema) {
        try {
            GlobalConfig globalConfig = GlobalConfig.getInstance();
            File schemaDefFile = Paths.get(globalConfig.getBaseDir(), DATA_BASE_DIR, schema, SCHEMA_DEF_FILENAME)
                    .toFile();
            if (!schemaDefFile.exists()) {
                throw new IllegalStateException("schema not exists: " + schema);
            }
            String def = IOUtils.toString(new FileReader(schemaDefFile));
            SchemaDefinition schemaDefinition = JSON.parseObject(def, SchemaDefinition.class);
            LOGGER.info("open schema succeeded: {}", JSON.toJSONString(schemaDefinition));
            return schemaDefinition;
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    @Override
    public void dropSchema(SchemaDefinition schemaDefinition) {
        GlobalConfig globalConfig = GlobalConfig.getInstance();
        File schemaDir = Paths.get(globalConfig.getBaseDir(), DATA_BASE_DIR, schemaDefinition.getName()).toFile();
        if (schemaDir.exists()) {
            if (!schemaDir.delete()) {
                throw new IllegalStateException("delete schema failed: " + JSON.toJSONString(schemaDefinition));
            }
        } else {
            LOGGER.warn("schema not found, do not need to drop: " + schemaDefinition);
        }
        LOGGER.info("drop schema succeeded: {}", JSON.toJSONString(schemaDefinition));
    }
}
