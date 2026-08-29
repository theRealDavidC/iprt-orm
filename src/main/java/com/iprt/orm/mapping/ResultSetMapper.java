package com.iprt.orm.mapping;

import com.iprt.orm.core.ColumnMetadata;
import com.iprt.orm.core.EntityMetadata;
import com.iprt.orm.util.ReflectionUtil;
import java.sql.ResultSet;
import java.util.List;

public class ResultSetMapper {

    public static <T> T mapRow(ResultSet rs, EntityMetadata meta, Class<T> clazz) {
        try {
            Object instance = ReflectionUtil.createInstance(clazz);
            for (ColumnMetadata col : meta.getNonTransientColumns()) {
                Class<?> javaType = col.getField().getType();
                Object value = TypeMapper.getResultSetValue(rs, col.getColumnName(), javaType);
                ReflectionUtil.setFieldValue(instance, col.getField(), value);
            }
            return clazz.cast(instance);
        } catch (Exception e) {
            throw new RuntimeException("Failed to map row", e);
        }
    }
}
