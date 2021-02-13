package com.jasper.litebase.engine.domain;

import java.io.File;
import java.io.FileInputStream;
import java.nio.channels.FileChannel;

public class Table {
    private TableDefinition tableDefinition;
    private File defFile;
    private File dataFile;
    private FileChannel channel;

    public Table(TableDefinition tableDefinition) {
        this.tableDefinition = tableDefinition;
    }

    public void init() {
        try {
            channel = new FileInputStream(dataFile).getChannel();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public TableDefinition getTableDefinition() {
        return tableDefinition;
    }

    public FileChannel getChannel() {
        return channel;
    }

    public void close() {
        try {
            channel.close();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}
