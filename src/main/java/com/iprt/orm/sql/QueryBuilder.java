package com.iprt.orm.sql;

import com.iprt.orm.core.EntityMetadata;
import com.iprt.orm.mapping.ResultSetMapper;
import com.iprt.orm.mapping.TypeMapper;
import java.sql.*;
import java.util.*;

public class QueryBuilder {
    private Class<?> clazz;
    private EntityMetadata meta;
    private Connection conn;
    private List<Object[]> conditions;
    private String orderByColumn;
    private String orderByDirection;
    private int limit;
    private int offset;

    public QueryBuilder(Class<?> clazz, EntityMetadata meta, Connection conn) {
        this.clazz = clazz;
        this.meta = meta;
        this.conn = conn;
        this.conditions = new ArrayList<>();
        this.limit = -1;
        this.offset = -1;
    }

    public QueryBuilder where(String column, String operator, Object value) {
        conditions.add(new Object[]{column, operator, value});
        return this;
    }

    public QueryBuilder orderBy(String column, String direction) {
        this.orderByColumn = column;
        this.orderByDirection = direction;
        return this;
    }

    public QueryBuilder limit(int limit) {
        this.limit = limit;
        return this;
    }

    public QueryBuilder offset(int offset) {
        this.offset = offset;
        return this;
    }

    public String buildSql() {
        StringBuilder sb = new StringBuilder(SqlGenerator.generateSelectAll(meta));
        if (!conditions.isEmpty()) {
            sb.append(" WHERE ");
            for (int i = 0; i < conditions.size(); i++) {
                Object[] condition = conditions.get(i);
                sb.append(condition[0]).append(" ").append(condition[1]).append(" ?");
                if (i < conditions.size() - 1) sb.append(" AND ");
            }
        }
        if (orderByColumn != null) {
            sb.append(" ORDER BY ").append(orderByColumn).append(" ").append(orderByDirection);
        }
        if (limit != -1) sb.append(" LIMIT ").append(limit);
        if (offset != -1) sb.append(" OFFSET ").append(offset);
        return sb.toString();
    }

    public <T> List<T> execute() {
        try {
            String sql = buildSql();
            PreparedStatement ps = conn.prepareStatement(sql);
            for (int i = 0; i < conditions.size(); i++) {
                Object value = conditions.get(i)[2];
                TypeMapper.setStatementValue(ps, i + 1, value);
            }
            ResultSet rs = ps.executeQuery();
            List<T> results = new ArrayList<>();
            while (rs.next()) {
                results.add((T) ResultSetMapper.mapRow(rs, meta, clazz));
            }
            return results;
        } catch (Exception e) {
            throw new RuntimeException("Query execution failed", e);
        }
    }
}
