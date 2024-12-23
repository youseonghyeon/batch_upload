package com.example.excel.controller;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.List;

@Slf4j
public class ReflectionMapper {

    /**
     * List<String> 데이터를 클래스 필드에 매핑하여 인스턴스를 반환
     *
     * @param clazz  대상 클래스 타입
     * @param values 값 리스트
     * @return 매핑된 객체 인스턴스
     */
    public static <T> T mapValuesToInstance(Class<T> clazz, List<String> values) {
        if (values == null || values.isEmpty()) {
            throw new IllegalArgumentException("Values should not be null or empty");
        }

        T instance = createInstance(clazz);
        Field[] fields = clazz.getDeclaredFields();
        int valueIndex = 0;

        for (Field field : fields) {
            if (shouldSkipField(field)) {
                continue; // static 또는 final 필드는 무시
            }

            if (valueIndex >= values.size()) {
                log.warn("Value list size [{}] is smaller than the number of fields [{}]", values.size(), fields.length);
                break; // 값 리스트가 부족하면 중단
            }

            setFieldValue(instance, field, values.get(valueIndex));
            valueIndex++;
        }

        return instance;
    }

    private static <T> T createInstance(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create an instance of " + clazz.getName(), e);
        }
    }

    private static boolean shouldSkipField(Field field) {
        int modifiers = field.getModifiers();
        return java.lang.reflect.Modifier.isStatic(modifiers) || java.lang.reflect.Modifier.isFinal(modifiers);
    }

    private static void setFieldValue(Object instance, Field field, String value) {
        try {
            field.setAccessible(true); // private 필드 접근 허용
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            log.error("Failed to set field [{}] with value [{}]", field.getName(), value, e);
            throw new RuntimeException("Failed to set field " + field.getName(), e);
        }
    }
}
