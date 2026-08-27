package com.iprt.orm.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class ReflectionUtil {

    public static List<Field> getAllFields(Class<?> clazz) {
        return Arrays.asList(clazz.getDeclaredFields());
    }

    public static Object getFieldValue(Object instance, Field field) throws Exception {
        field.setAccessible(true);
        return field.get(instance);
    }

    public static void setFieldValue(Object instance, Field field, Object value) throws Exception {
        field.setAccessible(true);
        field.set(instance, value);
    }

    public static Object createInstance(Class<?> clazz) throws Exception {
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
    }
}
