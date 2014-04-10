package com.mysema.query.types;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;
import com.mysema.query.Tuple;
import com.mysema.query.dml.StoreClause;
import com.mysema.query.types.Expression;
import com.mysema.query.types.MappingProjection;
import com.mysema.query.types.Path;

public class FieldMappingProjection<T> extends MappingProjection<T> {

    private static final long serialVersionUID = 7439382693284095441L;

    private static final Logger log = LoggerFactory.getLogger(FieldMappingProjection.class);
    
    private final Map<Path<?>, Field> pathToField;
    
    public FieldMappingProjection(Class<? super T> type, Expression<?>... args) {
        super(type, args);
        pathToField = pathToField(getType(), getArgs());
    }

    public FieldMappingProjection(Class<? super T> type, Expression<?>[]... args) {
        super(type, args);
        pathToField = pathToField(getType(), getArgs());
    }
    
    @Override
    protected final T map(Tuple row) {
        T object = null;
        if (!isNull(row)) {
            object = create(row);
            setFields(row, object);
            afterPropertiesSet(row, object);
        }
        return object;
    }

    private void setFields(Tuple row, T object) throws Error {
        for (Map.Entry<Path<?>, Field> entry : pathToField.entrySet()) {
            Path<?> path = entry.getKey();
            Field field = entry.getValue();
            Object value = row.get(path);

            if (value != null) {
                try {
                    field.set(object, value);
                } catch (IllegalAccessException e) {
                    throw new Error(e);
                } catch (IllegalArgumentException e) {
                    typeMismatch(path, e);
                }
            }
        }
    }
    
    public <S extends StoreClause<S>> S populateStoreClause(T object, S store) {
        for (Map.Entry<Path<?>, Field> entry : pathToField.entrySet()) {
            @SuppressWarnings("unchecked")
            Path<Object> path = (Path<Object>) entry.getKey();
            Field field = entry.getValue();
            try {
                Object value = field.get(object);
                if (value == null) {
                    store.setNull(path);
                } else {
                    store.set(path, value);
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new Error(e);
            }
        }
        return store;
    }
    
    protected void afterPropertiesSet(Tuple row, T object) {
        return;
    }
    
    protected boolean isNull(Tuple row) {
        return false;
    }

    protected void typeMismatch(Expression<?> expr, IllegalArgumentException e) {
        log.warn("An exception was throw when reading " + expr, e);
    }

    protected T create(Tuple row) {
        try {
            return getType().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new Error(e);
        }
    }
    

    private static Map<Path<?>, Field> pathToField(Class<?> type, List<Expression<?>> args) {
        ImmutableMap.Builder<Path<?>, Field> pathToField = ImmutableMap.builder();
        for (Expression<?> expr : args) {
            if (expr instanceof Path) {
                Path<?> path = (Path<?>) expr;
                String name = path.getMetadata().getName();
                try {
                    Field field = type.getField(name);
                    if (typeMatches(path, field)) {
                        pathToField.put(path, field);
                    } else {
                        typeMismatch(expr, field);
                    }
                } catch (NoSuchFieldException e) {
                    fieldNotFound(expr, type);
                }
            }
        }
        return pathToField.build();
    }

    private static boolean typeMatches(Path<?> path, Field field) {
        return path.getType().equals(field.getType());
    }
    
    private static void fieldNotFound(Expression<?> expr, Class<?> actualType) {
        log.info("Field {} not found from {}", expr, actualType);
    }

    private static void typeMismatch(Expression<?> expr, Field field) {
        log.info("Type of {} doesn't match field {}", expr, field);
    }

}
