/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.HashSet;
import java.util.Set;

import com.mysema.query.types.SubQuery;
import com.mysema.query.types.alias.ASimple;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.Path;
import com.mysema.query.types.path.PathMetadata;

/**
 * Projection is a general safe projection from tables and subqueries. Use the
 * projection class by subclassing it and adding the used columns like this :
 * 
 * <pre>
 * class EmployeeProjection extends Projection {
 *     public EmployeeProjection(String entityName) {
 *         super(entityName);
 *     }
 * 
 *     Expr&lt;Integer&gt; id;
 *     Expr&lt;String&gt; firstname;
 *     Expr&lt;Integer&gt; superiorId;
 * }
 * </pre>
 * 
 * @author tiwe
 * @version $Id$
 */
// TODO : move this to querydsl-core
public class Projection extends PEntity<Projection> {

    private Set<String> fieldNames = new HashSet<String>();

    private String entityName;

    private static Method _numberMethod, _comparableMethod;

    static {
        try {
            _numberMethod = PEntity.class.getDeclaredMethod("_number",
                    String.class, Class.class);
            _comparableMethod = PEntity.class.getDeclaredMethod("_comparable",
                    String.class, Class.class);
            _numberMethod.setAccessible(true);
            _comparableMethod.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Create a new Projection with the given name
     * 
     * @param entityName
     */
    public Projection(String entityName) {
        super(Projection.class, entityName, PathMetadata
                .forVariable(entityName));
        Class<?> cl = getClass();
        try {
            while (!cl.equals(Projection.class)) {
                for (Field field : cl.getDeclaredFields()) {
                    if (Expr.class.isAssignableFrom(field.getType())
                            && field.getGenericType() instanceof ParameterizedType) {
                        field.setAccessible(true);
                        handleField(field);
                    }
                }
                cl = cl.getSuperclass();
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        this.entityName = entityName;
    }

    /**
     * Add the given field into the projection
     * 
     * @param field
     * @throws Exception
     */
    private void handleField(Field field) throws Exception {
        ParameterizedType type = (ParameterizedType) field.getGenericType();
        Class<?> exprType = (Class<?>) type.getActualTypeArguments()[0];
        Expr<?> fieldVal;
        if (Boolean.class.isAssignableFrom(exprType)) {
            fieldVal = _boolean(field.getName());
        } else if (Number.class.isAssignableFrom(exprType)) {
            fieldVal = (Expr<?>) _numberMethod.invoke(this, field.getName(),
                    exprType);
        } else if (String.class.isAssignableFrom(exprType)) {
            fieldVal = _string(field.getName());
        } else if (Comparable.class.isAssignableFrom(exprType)) {
            fieldVal = (Expr<?>) _comparableMethod.invoke(this,
                    field.getName(), exprType);
        } else {
            fieldVal = _simple(field.getName(), exprType);
        }

        if (field.getType().isAssignableFrom(fieldVal.getClass())) {
            fieldNames.add(field.getName());
            field.set(this, fieldVal);
        } else {
            // unsupported type
        }
    }

    public String getName() {
        return entityName;
    }

    /**
     * Project the given SubQuery and create an alias to the projection
     * 
     * @param expr
     * @return
     */
    public <A> ASimple<A> from(SubQuery<Object, A> expr) {
        return new ASimple<A>(expr, entityName);
    }

    /**
     * Project the given Entity expression and create an alias to the projection
     * 
     * @param expr
     * @return
     */
    @SuppressWarnings("unchecked")
    public ASimple<Object[]> from(PEntity<?> expr) {
        Set<Expr<?>> fields = new HashSet<Expr<?>>();
        try {
            Class<?> cl = expr.getClass();
            for (String fieldName : fieldNames) {
                try {
                    Field field = cl.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    Expr<?> fieldVal = (Expr<?>) field.get(expr);
                    // if the field is a path, then use an column alias, if
                    // necessary
                    if (fieldVal instanceof Path) {
                        String columnName = ((Path<?>) fieldVal).getMetadata()
                                .toString();
                        if (!columnName.equals(fieldName)) {
                            fieldVal = new ASimple(fieldVal, fieldName);
                        }
                    }
                    fields.add(fieldVal);
                } catch (NoSuchFieldException e) {
                    String error = "No such field in expr : " + fieldName;
                    throw new RuntimeException(error, e);
                }
            }
            return new ASimple<Object[]>(SQLGrammar.select(
                    fields.toArray(new Expr[fields.size()])).from(expr),
                    entityName);

        } catch (IllegalAccessException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (SecurityException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
