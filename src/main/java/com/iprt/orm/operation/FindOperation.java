package com.iprt.orm.operation;

import com.iprt.orm.core.EntityMetadata;
import com.iprt.orm.mapping.ResultSetMapper;
import com.iprt.orm.mapping.TypeMapper;
import com.iprt.orm.sql.SqlGenerator;
import java.sql.*;
import java.util.*;

public class FindOperation {

    public <T> Optional<T> findById(Class<T> clazz, Object id, EntityMetadata meta, Connection conn) {
        try {
            String sql = SqlGenerator.generateSelectById(meta);
            PreparedStatement ps = conn.prepareStatement(sql);
            TypeMapper.setStatementValue(ps, 1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                T result = ResultSetMapper.mapRow(rs, meta, clazz);
                return Optional.of(result);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new RuntimeException("Operation findById failed", e);
        }
    }

    public <T> List<T> findAll(Class<T> clazz, EntityMetadata meta, Connection conn) {
        try {
            String sql = SqlGenerator.generateSelectAll(meta);
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            List<T> results = new ArrayList<>();
            while (rs.next()) {
                T result = ResultSetMapper.mapRow(rs, meta, clazz);
                results.add(result);
            }
            return results;
        } catch (Exception e) {
            throw new RuntimeException("Operation findAll failed", e);
        }
    }
}
