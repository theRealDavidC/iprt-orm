package com.iprt.orm.core;

import java.lang.reflect.Field;

public class ColumnMetadata {
    private Field field;
    private String columnName;
    private String sqlType;
    private boolean nullable;
    private boolean unique;
    private int length;
    private boolean isId;
    private boolean isAutoIncrement;
    private boolean isTransient;

    public ColumnMetadata(Field field, String columnName,
                          String sqlType, boolean nullable, boolean unique,
                          int length, boolean isId, boolean isAutoIncrement, boolean isTransient) {
        this.field = field;
        this.columnName = columnName;
        this.sqlType = sqlType;
        this.nullable = nullable;
        this.unique = unique;
        this.length = length;
        this.isId = isId;
        this.isAutoIncrement = isAutoIncrement;
        this.isTransient = isTransient;
    }

    public Field getField() { return field; }
    public String getColumnName() { return columnName; }
    public String getSqlType() { return sqlType; }
    public boolean isNullable() { return nullable; }
    public boolean isUnique() { return unique; }
    public int getLength() { return length; }
    public boolean isId() { return isId; }
    public boolean isAutoIncrement() { return isAutoIncrement; }
    public boolean isTransient() { return isTransient; }
}
