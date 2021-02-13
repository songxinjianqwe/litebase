package com.jasper.litebase.engine.api;

import com.jasper.litebase.engine.api.impl.DefaultSchemaTableApi;
import com.jasper.litebase.engine.domain.SchemaDefinition;
import com.jasper.litebase.engine.domain.Table;
import com.jasper.litebase.engine.domain.TableDefinition;

public interface SchemaTableApi {
    SchemaTableApi INSTANCE = new DefaultSchemaTableApi();

    static SchemaTableApi getInstance() {
        return INSTANCE;
    }

    void createTable(TableDefinition tableDefinition);

    void dropTable(TableDefinition tableDefinition);

    Table openTable(String schema, String table);

    void closeTable(Table tableImpl);

    void createSchema(SchemaDefinition schemaDefinition);

    SchemaDefinition openSchema(String schema);

    void dropSchema(SchemaDefinition schemaDefinition);
}
