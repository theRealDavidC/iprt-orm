package com.iprt.orm.core;

import com.iprt.orm.annotation.*;
import com.iprt.orm.util.NamingUtil;
import com.iprt.orm.util.ReflectionUtil;
import java.lang.reflect.Field;
import java.util.*;

public class EntityRegistry {
    private Map<Class<?>, EntityMetadata> registry = new HashMap<>();

    public void register(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(Table.class)) {
            throw new RuntimeException("Class is not annotated with @Table: "
                + clazz.getName());
        }

        Table table = clazz.getAnnotation(Table.class);
        String tableName = table.name().isBlank()
            ? NamingUtil.toTableName(clazz.getSimpleName())
            : table.name();

        List<ColumnMetadata> columns = new ArrayList<>();

        for (Field field : ReflectionUtil.getAllFields(clazz)) {
            Column col = field.getAnnotation(Column.class);
            String columnName = (col != null && !col.name().isBlank())
                ? col.name()
                : NamingUtil.toSnakeCase(field.getName());

            boolean isId = field.isAnnotationPresent(Id.class);
            boolean isAutoIncrement = field.isAnnotationPresent(AutoIncrement.class);
            boolean isTransient = field.isAnnotationPresent(Transient.class);
            boolean nullable = col != null ? col.nullable() : true;
            boolean unique = col != null ? col.unique() : false;
            int length = col != null ? col.length() : 255;

            columns.add(new ColumnMetadata(field, columnName, "", nullable, unique,
                length, isId, isAutoIncrement, isTransient));
        }

        EntityMetadata metadata = new EntityMetadata(clazz, tableName, columns);
        registry.put(clazz, metadata);
    }

    public EntityMetadata getMetadata(Class<?> clazz) {
        EntityMetadata metadata = registry.get(clazz);
        if (metadata == null) {
            throw new RuntimeException("Class not registered: " + clazz.getName());
        }
        return metadata;
    }

    public Collection<EntityMetadata> getAll() {
        return registry.values();
    }
}
