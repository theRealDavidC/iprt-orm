package com.iprt.orm.core;

import java.util.ArrayList;
import java.util.List;

public class EntityMetadata {
    private Class<?> entityClass;
    private String tableName;
    private List<ColumnMetadata> columns;
    private ColumnMetadata idColumn;

    public EntityMetadata(Class<?> entityClass, String tableName, List<ColumnMetadata> columns) {
        this.entityClass = entityClass;
        this.tableName = tableName;
        this.columns = columns;
        for (ColumnMetadata col : columns) {
            if (col.isId()) {
                this.idColumn = col;
                break;
            }
        }
    }

    public Class<?> getEntityClass() { return entityClass; }
    public String getTableName() { return tableName; }
    public List<ColumnMetadata> getColumns() { return columns; }
    public ColumnMetadata getIdColumn() { return idColumn; }

    public List<ColumnMetadata> getNonTransientColumns() {
        List<ColumnMetadata> result = new ArrayList<>();
        for (ColumnMetadata col : columns) {
            if (!col.isTransient()) {
                result.add(col);
            }
        }
        return result;
    }

    public List<ColumnMetadata> getNonIdColumns() {
        List<ColumnMetadata> result = new ArrayList<>();
        for (ColumnMetadata col : columns) {
            if (!col.isId() && !col.isTransient()) {
                result.add(col);
            }
        }
        return result;
    }
}
