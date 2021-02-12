package com.jasper.litebase.engine.domain;

import java.nio.channels.FileChannel;

public class Table {
    private TableDefinition tableDefinition;
    private FileChannel channel;

    public Table(TableDefinition tableDefinition, FileChannel channel) {
        this.tableDefinition = tableDefinition;
        this.channel = channel;
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
