package com.iprt.orm.operation;

import com.iprt.orm.core.EntityMetadata;
import com.iprt.orm.mapping.TypeMapper;
import com.iprt.orm.sql.SqlGenerator;
import com.iprt.orm.util.ReflectionUtil;
import java.sql.*;

public class DeleteOperation {

    public void delete(Object entity, EntityMetadata meta, Connection conn) {
        try {
            String sql = SqlGenerator.generateDelete(meta);
            PreparedStatement ps = conn.prepareStatement(sql);
            Object idValue = ReflectionUtil.getFieldValue(entity, meta.getIdColumn().getField());
            TypeMapper.setStatementValue(ps, 1, idValue);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("No rows deleted, entity not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Delete operation failed", e);
        }
    }
}
