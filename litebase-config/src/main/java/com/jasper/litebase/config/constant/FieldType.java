package com.jasper.litebase.config.constant;

public enum FieldType {
    FIELD_TYPE_DECIMAL(0), FIELD_TYPE_TINY(1), FIELD_TYPE_SHORT(2), FIELD_TYPE_LONG(3), FIELD_TYPE_FLOAT(
            4), FIELD_TYPE_DOUBLE(5), FIELD_TYPE_NULL(6), FIELD_TYPE_TIMESTAMP(7), FIELD_TYPE_LONGLONG(
                    8), FIELD_TYPE_INT24(9), FIELD_TYPE_DATE(10), FIELD_TYPE_TIME(11), FIELD_TYPE_DATETIME(
                            12), FIELD_TYPE_YEAR(13), FIELD_TYPE_NEWDATE(14), FIELD_TYPE_VARCHAR(15), FIELD_TYPE_BIT(
                                    16), FIELD_TYPE_NEW_DECIMAL(246), FIELD_TYPE_ENUM(247), FIELD_TYPE_SET(
                                            248), FIELD_TYPE_TINY_BLOB(249), FIELD_TYPE_MEDIUM_BLOB(
                                                    250), FIELD_TYPE_LONG_BLOB(251), FIELD_TYPE_BLOB(
                                                            252), FIELD_TYPE_VAR_STRING(253), FIELD_TYPE_STRING(
                                                                    254), FIELD_TYPE_GEOMETRY(255),;

    private int code;

    FieldType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
