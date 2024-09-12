package com.querydsl.core.types;

import com.querydsl.core.types.dsl.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SimpleDTOProjection<T> extends FactoryExpressionBase<T> {
    private final List<Expression<?>> expressions;
    private final Constructor<? extends T> constructor;

    public SimpleDTOProjection(Class<? extends T> type, EntityPathBase<?> entity) {
        super(type);
        this.expressions = generateExpressions(type, entity);
        this.constructor = findMatchingConstructor(type);
    }

    private List<Expression<?>> generateExpressions(Class<? extends T> dtoType, EntityPathBase<?> entity) {
        List<Expression<?>> expressions = new ArrayList<>();
        for (Field field : dtoType.getDeclaredFields()) {
            String fieldName = field.getName();
            try {
                Field entityField = entity.getClass().getField(fieldName);
                if (entityField != null) {
                    Object value = entityField.get(entity);
                    if (value instanceof Expression<?>) {
                        expressions.add((Expression<?>) value);
                    }
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                // Ignore if the field is not present in the entity
            }
        }
        return expressions;
    }

    private Constructor<? extends T> findMatchingConstructor(Class<? extends T> type) {
        for (Constructor<?> constructor : type.getDeclaredConstructors()) {
            if (constructor.getParameterTypes().length == expressions.size()) {
                return (Constructor<? extends T>) constructor;
            }
        }
        throw new RuntimeException("No matching constructor found for " + type.getSimpleName());
    }

    @Override
    public T newInstance(Object... args) {
        try {
            return constructor.newInstance(args);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create instance of " + getType().getSimpleName(), e);
        }
    }

    @Override
    public List<Expression<?>> getArgs() {
        return expressions;
    }

    @Override
    public <R, C> R accept(Visitor<R, C> v, C context) {
        return null;
    }
    public static <T> SimpleDTOProjection<T> fields(Class<? extends T> type, EntityPathBase<?> entity) {
        return new SimpleDTOProjection<>(type, entity);
    }
}