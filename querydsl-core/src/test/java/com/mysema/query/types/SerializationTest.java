package com.mysema.query.types;

import static com.mysema.testutil.Serialization.serialize;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.junit.Test;
import org.reflections.Reflections;

import com.google.common.collect.Maps;
import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.QueryMetadata;
import com.mysema.query.group.GroupBy;
import com.mysema.query.group.GroupExpression;
import com.mysema.query.support.Expressions;
import com.mysema.query.types.path.*;
import com.mysema.testutil.Serialization;

public class SerializationTest {

    public enum Gender { MALE, FEMALE }

    @Test
    public void Expressions() throws Exception {
        Map<Class<?>, Object> args = Maps.newHashMap();
        args.put(Object.class, "obj");
        args.put(BeanPath.class, new EntityPathBase<Object>(Object.class, "obj"));
        args.put(Class.class, Integer.class);
        args.put(Class[].class, new Class[]{Object.class, Object.class});
        args.put(java.util.Date.class, new java.util.Date(0));
        args.put(java.sql.Date.class, new java.sql.Date(0));
        args.put(java.sql.Time.class, new java.sql.Time(0));
        args.put(java.sql.Timestamp.class, new java.sql.Timestamp(0));
        args.put(Expression.class, new EnumPath<Gender>(Gender.class, "e"));
        args.put(Expression[].class, new Expression<?>[]{
                new EnumPath<Gender>(Gender.class, "e"), Expressions.stringPath("s")});
        args.put(FactoryExpression.class, Projections.tuple(Expressions.stringPath("str")));
        args.put(GroupExpression.class, GroupBy.avg(Expressions.numberPath(Integer.class, "num")));
        args.put(Number.class, 1);
        args.put(Operator.class, Ops.AND);
        args.put(Path.class, Expressions.stringPath("str"));
        args.put(PathBuilderValidator.class, PathBuilderValidator.DEFAULT);
        args.put(PathMetadata.class, PathMetadataFactory.forVariable("obj"));
        args.put(PathInits.class, PathInits.DEFAULT);
        args.put(Predicate.class, Expressions.path(Object.class, "obj").isNull());
        args.put(QueryMetadata.class, new DefaultQueryMetadata());
        args.put(String.class, "obj");

        Reflections reflections = new Reflections();
        Set<Class<? extends Expression>> types = reflections.getSubTypesOf(Expression.class);
        for (Class<?> type : types) {
            if (!type.isInterface() && !type.isMemberClass() && !Modifier.isAbstract(type.getModifiers())) {
                for (Constructor<?> c : type.getConstructors()) {
                    Object[] parameters = new Object[c.getParameterTypes().length];
                    for (int i = 0; i < c.getParameterTypes().length; i++) {
                        parameters[i] = Objects.requireNonNull(args.get(c.getParameterTypes()[i]),
                                c.getParameterTypes()[i].getName());
                    }
                    c.setAccessible(true);
                    Object o = c.newInstance(parameters);
                    assertEquals(o, Serialization.serialize(o));
                }
            }
        }
    }

    @Test
    public void Order() {
        OrderSpecifier<?> order = new OrderSpecifier<String>(Order.ASC, Expressions.stringPath("str"));
        assertEquals(order, Serialization.serialize(order));
    }

    @Test
    public void Roundtrip() throws Exception {
        PathImpl path = new PathImpl(Object.class, "entity");
        SimplePath path2 = new SimplePath(Object.class, "entity");
        assertEquals(path, serialize(path));
        assertEquals(path2, serialize(path2));
        assertEquals(path2.isNull(), serialize(path2.isNull()));
        assertEquals(path.hashCode(), serialize(path).hashCode());
        assertEquals(path2.hashCode(), serialize(path2).hashCode());
        assertEquals(path2.isNull().hashCode(), serialize(path2.isNull()).hashCode());
    }

}
