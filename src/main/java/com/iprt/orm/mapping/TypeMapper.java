package com.iprt.orm.mapping;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TypeMapper {

    public static String getSqlType(Class<?> javaType) {
        if (javaType == null) {
            throw new IllegalArgumentException("Java type cannot be null");
        }
        if (javaType == String.class) return "VARCHAR(255)";
        if (javaType == Long.class || javaType == long.class) return "BIGINT";
        if (javaType == Integer.class || javaType == int.class) return "INTEGER";
        if (javaType == Double.class || javaType == double.class) return "DOUBLE PRECISION";
        if (javaType == Boolean.class || javaType == boolean.class) return "BOOLEAN";
        if (javaType == LocalDateTime.class) return "TIMESTAMP";
        if (javaType == LocalDate.class) return "DATE";
        throw new RuntimeException("Unsupported type: " + javaType.getName());
    }

    public static void setStatementValue(PreparedStatement stmt, int index, Object value)
            throws SQLException {
        if (value == null) {
            stmt.setNull(index, Types.VARCHAR);
        } else if (value instanceof String v) {
            stmt.setString(index, v);
        } else if (value instanceof Long v) {
            stmt.setLong(index, v);
        } else if (value instanceof Integer v) {
            stmt.setInt(index, v);
        } else if (value instanceof Double v) {
            stmt.setDouble(index, v);
        } else if (value instanceof Boolean v) {
            stmt.setBoolean(index, v);
        } else if (value instanceof LocalDateTime v) {
            stmt.setTimestamp(index, Timestamp.valueOf(v));
        } else if (value instanceof LocalDate v) {
            stmt.setDate(index, Date.valueOf(v));
        } else {
            throw new IllegalArgumentException(
                "Unsupported type: " + value.getClass().getName()
            );
        }
    }

    public static Object getResultSetValue(ResultSet rs, String columnName, Class<?> javaType)
            throws SQLException {
        if (javaType == String.class) {
            return rs.getString(columnName);
        } else if (javaType == Long.class || javaType == long.class) {
            return rs.getLong(columnName);
        } else if (javaType == Integer.class || javaType == int.class) {
            return rs.getInt(columnName);
        } else if (javaType == Double.class || javaType == double.class) {
            return rs.getDouble(columnName);
        } else if (javaType == Boolean.class || javaType == boolean.class) {
            return rs.getBoolean(columnName);
        } else if (javaType == LocalDateTime.class) {
            Timestamp ts = rs.getTimestamp(columnName);
            return ts != null ? ts.toLocalDateTime() : null;
        } else if (javaType == LocalDate.class) {
            Date d = rs.getDate(columnName);
            return d != null ? d.toLocalDate() : null;
        } else {
            throw new IllegalArgumentException(
                "Unsupported type: " + javaType.getName()
            );
        }
    }
}
