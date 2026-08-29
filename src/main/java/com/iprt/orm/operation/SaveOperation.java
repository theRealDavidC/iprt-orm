package com.iprt.orm.operation;

import com.iprt.orm.core.ColumnMetadata;
import com.iprt.orm.core.EntityMetadata;
import com.iprt.orm.mapping.TypeMapper;
import com.iprt.orm.sql.SqlGenerator;
import com.iprt.orm.util.ReflectionUtil;
import java.sql.*;
import java.util.List;

public class SaveOperation {

    public void save(Object entity, EntityMetadata meta, Connection conn) {
        try {
            String sql = SqlGenerator.generateInsert(meta);
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            List<ColumnMetadata> columns = meta.getNonIdColumns();
            for (int i = 0; i < columns.size(); i++) {
                ColumnMetadata col = columns.get(i);
                int index = i + 1;
                Object value = ReflectionUtil.getFieldValue(entity, col.getField());
                TypeMapper.setStatementValue(ps, index, value);
            }
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                long generatedId = keys.getLong(1);
                ReflectionUtil.setFieldValue(entity, meta.getIdColumn().getField(), generatedId);
            }
        } catch (Exception e) {
            throw new RuntimeException("Save operation failed", e);
        }
    }
}
