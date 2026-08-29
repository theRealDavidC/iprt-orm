package com.iprt.orm.operation;

import com.iprt.orm.core.ColumnMetadata;
import com.iprt.orm.core.EntityMetadata;
import com.iprt.orm.mapping.TypeMapper;
import com.iprt.orm.sql.SqlGenerator;
import com.iprt.orm.util.ReflectionUtil;
import java.sql.*;
import java.util.List;

public class UpdateOperation {

    public void update(Object entity, EntityMetadata meta, Connection conn) {
        try {
            String sql = SqlGenerator.generateUpdate(meta);
            PreparedStatement ps = conn.prepareStatement(sql);
            List<ColumnMetadata> columns = meta.getNonIdColumns();
            for (int i = 0; i < columns.size(); i++) {
                ColumnMetadata col = columns.get(i);
                Object value = ReflectionUtil.getFieldValue(entity, col.getField());
                TypeMapper.setStatementValue(ps, i + 1, value);
            }
            Object idValue = ReflectionUtil.getFieldValue(entity, meta.getIdColumn().getField());
            TypeMapper.setStatementValue(ps, columns.size() + 1, idValue);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("No rows updated, entity not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Update operation failed", e);
        }
    }
}
