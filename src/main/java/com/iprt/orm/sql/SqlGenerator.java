package com.iprt.orm.sql;

import com.iprt.orm.core.ColumnMetadata;
import com.iprt.orm.core.EntityMetadata;
import com.iprt.orm.mapping.TypeMapper;
import java.util.List;

public class SqlGenerator {

    public static String generateCreateTable(EntityMetadata meta) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ");
        sb.append(meta.getTableName());
        sb.append(" (");
        List<ColumnMetadata> columns = meta.getNonTransientColumns();
        for (int i = 0; i < columns.size(); i++) {
            ColumnMetadata col = columns.get(i);
            sb.append(col.getColumnName());
            sb.append(" ");
            if (col.isId() && col.isAutoIncrement()) {
                sb.append("BIGSERIAL PRIMARY KEY");
            } else {
                sb.append(TypeMapper.getSqlType(col.getField().getType()));
                if (!col.isNullable()) sb.append(" NOT NULL");
                if (col.isUnique()) sb.append(" UNIQUE");
            }
            if (i < columns.size() - 1) sb.append(", ");
        }
        sb.append(")");
        return sb.toString();
    }

    public static String generateInsert(EntityMetadata meta) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ");
        sb.append(meta.getTableName());
        sb.append(" (");
        List<ColumnMetadata> columns = meta.getNonIdColumns();
        for (int i = 0; i < columns.size(); i++) {
            sb.append(columns.get(i).getColumnName());
            if (i < columns.size() - 1) sb.append(", ");
        }
        sb.append(") VALUES (");
        for (int i = 0; i < columns.size(); i++) {
            sb.append("?");
            if (i < columns.size() - 1) sb.append(", ");
        }
        sb.append(")");
        return sb.toString();
    }

    public static String generateSelectById(EntityMetadata meta) {
        return "SELECT * FROM " + meta.getTableName()
            + " WHERE " + meta.getIdColumn().getColumnName() + " = ?";
    }

    public static String generateSelectAll(EntityMetadata meta) {
        return "SELECT * FROM " + meta.getTableName();
    }

    public static String generateUpdate(EntityMetadata meta) {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ");
        sb.append(meta.getTableName());
        sb.append(" SET ");
        List<ColumnMetadata> columns = meta.getNonIdColumns();
        for (int i = 0; i < columns.size(); i++) {
            sb.append(columns.get(i).getColumnName());
            sb.append(" = ?");
            if (i < columns.size() - 1) sb.append(", ");
        }
        sb.append(" WHERE ");
        sb.append(meta.getIdColumn().getColumnName());
        sb.append(" = ?");
        return sb.toString();
    }

    public static String generateDelete(EntityMetadata meta) {
        return "DELETE FROM " + meta.getTableName()
            + " WHERE " + meta.getIdColumn().getColumnName() + " = ?";
    }
}
