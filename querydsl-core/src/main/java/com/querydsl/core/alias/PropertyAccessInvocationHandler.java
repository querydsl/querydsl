/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.core.alias;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import com.google.common.collect.ImmutableList;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ParametrizedExpression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.util.BeanUtils;
import com.querydsl.core.util.ReflectionUtils;

/**
 * PropertyAccessInvocationHandler is the main InvocationHandler class for the
 * CGLIB alias proxies
 *
 * @author tiwe
 */
public class PropertyAccessInvocationHandler implements MethodInterceptor {

    private static final int RETURN_VALUE = 42;

    private final Expression<?> hostExpression;

    private final AliasFactory aliasFactory;

    private final Map<Object, Expression<?>> propToExpr = new HashMap<Object, Expression<?>>();

    private final Map<Object, Object> propToObj = new HashMap<Object, Object>();

    private final PathFactory pathFactory;

    private final TypeSystem typeSystem;

    public PropertyAccessInvocationHandler(Expression<?> host, AliasFactory aliasFactory,
            PathFactory pathFactory, TypeSystem typeSystem) {
        this.hostExpression = host;
        this.aliasFactory = aliasFactory;
        this.pathFactory = pathFactory;
        this.typeSystem = typeSystem;
    }

    //CHECKSTYLE:OFF
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
    //CHECKSTYLE:ON
        Object rv = null;

        MethodType methodType = MethodType.get(method);

        if (methodType == MethodType.GETTER) {
            String ptyName = propertyNameForGetter(method);
            Class<?> ptyClass = method.getReturnType();
            Type genericType = method.getGenericReturnType();

            if (propToObj.containsKey(ptyName)) {
                rv = propToObj.get(ptyName);
            } else {
                PathMetadata<String> pm = createPropertyPath((Path<?>) hostExpression, ptyName);
                rv = newInstance(ptyClass, genericType, proxy, ptyName, pm);
            }
            aliasFactory.setCurrent(propToExpr.get(ptyName));

        } else if (methodType == MethodType.SCALA_GETTER) {
            String ptyName = method.getName();
            Class<?> ptyClass = method.getReturnType();
            Type genericType = method.getGenericReturnType();

            if (propToObj.containsKey(ptyName)) {
                rv = propToObj.get(ptyName);
            } else {
                PathMetadata<String> pm = createPropertyPath((Path<?>) hostExpression, ptyName);
                rv = newInstance(ptyClass, genericType, proxy, ptyName, pm);
            }
            aliasFactory.setCurrent(propToExpr.get(ptyName));

        } else if (methodType == MethodType.LIST_ACCESS || methodType == MethodType.SCALA_LIST_ACCESS) {
            // TODO : manage cases where the argument is based on a property invocation
            Object propKey = ImmutableList.of(MethodType.LIST_ACCESS, args[0]);
            if (propToObj.containsKey(propKey)) {
                rv = propToObj.get(propKey);
            } else {
                PathMetadata<Integer> pm = createListAccessPath((Path<?>) hostExpression, (Integer) args[0]);
                Class<?> elementType = ((ParametrizedExpression<?>) hostExpression).getParameter(0);
                rv = newInstance(elementType, elementType, proxy, propKey, pm);
            }
            aliasFactory.setCurrent(propToExpr.get(propKey));

        } else if (methodType == MethodType.MAP_ACCESS || methodType == MethodType.SCALA_MAP_ACCESS) {
            Object propKey = ImmutableList.of(MethodType.MAP_ACCESS, args[0]);
            if (propToObj.containsKey(propKey)) {
                rv = propToObj.get(propKey);
            } else {
                PathMetadata<?> pm = createMapAccessPath((Path<?>)hostExpression, args[0]);
                Class<?> valueType = ((ParametrizedExpression<?>) hostExpression).getParameter(1);
                rv = newInstance(valueType, valueType, proxy, propKey, pm);
            }
            aliasFactory.setCurrent(propToExpr.get(propKey));

        } else if (methodType == MethodType.TO_STRING) {
            rv = hostExpression.toString();

        } else if (methodType == MethodType.HASH_CODE) {
            rv = hostExpression.hashCode();

        } else if (methodType == MethodType.GET_MAPPED_PATH) {
            rv = hostExpression;

        } else {
            throw new IllegalArgumentException(
                    "Invocation of " + method.getName() +
                    " with types " + Arrays.asList(method.getParameterTypes()) + " not supported");
        }
        return rv;
    }

    @SuppressWarnings({ "unchecked"})
    @Nullable
    protected <T> T newInstance(Class<T> type, Type genericType, Object parent, Object propKey, 
            PathMetadata<?> metadata) {
        Expression<?> path;
        Object rv;

        if (String.class.equals(type)) {
            path = pathFactory.createStringPath(metadata);
            // null is used as a return value to block method invocations on Strings
            rv = null;

        } else if (Integer.class.equals(type) || int.class.equals(type)) {
            path = pathFactory.createNumberPath(Integer.class, metadata);
            rv = Integer.valueOf(RETURN_VALUE);

        } else if (Byte.class.equals(type) || byte.class.equals(type)) {
            path = pathFactory.createNumberPath(Byte.class, metadata);
            rv = Byte.valueOf((byte)RETURN_VALUE);

        } else if (java.util.Date.class.equals(type)) {
            path = pathFactory.createDateTimePath((Class)type, metadata);
            rv = new Date();

        } else if (java.sql.Timestamp.class.equals(type)) {
            path = pathFactory.createDateTimePath((Class)type, metadata);
            rv = new Timestamp(System.currentTimeMillis());

        } else if (java.sql.Date.class.equals(type)) {
            path = pathFactory.createDatePath((Class)type, metadata);
            rv = new java.sql.Date(System.currentTimeMillis());

        } else if (java.sql.Time.class.equals(type)) {
            path = pathFactory.createTimePath((Class)type, metadata);
            rv = new java.sql.Time(System.currentTimeMillis());

        } else if (Long.class.equals(type) || long.class.equals(type)) {
            path = pathFactory.createNumberPath(Long.class, metadata);
            rv = Long.valueOf(RETURN_VALUE);

        } else if (Short.class.equals(type) || short.class.equals(type)) {
            path = pathFactory.createNumberPath(Short.class, metadata);
            rv = Short.valueOf((short) RETURN_VALUE);

        } else if (Double.class.equals(type) || double.class.equals(type)) {
            path = pathFactory.createNumberPath(Double.class, metadata);
            rv = Double.valueOf(RETURN_VALUE);

        } else if (Float.class.equals(type) || float.class.equals(type)) {
            path = pathFactory.createNumberPath(Float.class, metadata);
            rv = Float.valueOf(RETURN_VALUE);

        } else if (BigInteger.class.equals(type)) {
            path = pathFactory.createNumberPath((Class)type, metadata);
            rv = BigInteger.valueOf(RETURN_VALUE);

        } else if (BigDecimal.class.equals(type)) {
            path = pathFactory.createNumberPath((Class)type, metadata);
            rv = BigDecimal.valueOf(RETURN_VALUE);

        } else if (Boolean.class.equals(type) || boolean.class.equals(type)) {
            path = pathFactory.createBooleanPath(metadata);
            rv = Boolean.TRUE;

        } else if (typeSystem.isMapType(type)) {
            Class<Object> keyType = (Class)ReflectionUtils.getTypeParameter(genericType, 0);
            Class<Object> valueType = (Class)ReflectionUtils.getTypeParameter(genericType, 1);
            path = pathFactory.createMapPath(keyType, valueType, metadata);
            rv = aliasFactory.createAliasForProperty(type, parent, path);

        } else if (typeSystem.isListType(type)) {
            Class<Object> elementType = (Class)ReflectionUtils.getTypeParameter(genericType, 0);
            path = pathFactory.createListPath(elementType, metadata);
            rv = aliasFactory.createAliasForProperty(type, parent, path);

        } else if (typeSystem.isSetType(type)) {
            Class<?> elementType = ReflectionUtils.getTypeParameterAsClass(genericType, 0);
            path = pathFactory.createSetPath(elementType, metadata);
            rv = aliasFactory.createAliasForProperty(type, parent, path);

        } else if (typeSystem.isCollectionType(type)) {
            Class<?> elementType = ReflectionUtils.getTypeParameterAsClass(genericType, 0);
            path = pathFactory.createCollectionPath(elementType, metadata);
            rv = aliasFactory.createAliasForProperty(type, parent, path);

        } else if (Enum.class.isAssignableFrom(type)) {
            path = pathFactory.createEnumPath((Class)type, metadata);
            rv = type.getEnumConstants()[0];

        } else if (type.isArray()) {
            path = pathFactory.createArrayPath((Class)type, metadata);
            rv = Array.newInstance(type.getComponentType(), 5);

        } else {
            if (Number.class.isAssignableFrom(type)) {
                path = pathFactory.createNumberPath((Class)type, metadata);
            } else if (Comparable.class.isAssignableFrom(type)) {
                path = pathFactory.createComparablePath((Class)type, metadata);
            } else {
                path = pathFactory.createEntityPath(type, metadata);
            }
            if (!Modifier.isFinal(type.getModifiers())) {
                rv = aliasFactory.createAliasForProperty(type, parent, path);
            } else {
                rv = null;
            }
        }
        propToObj.put(propKey, rv);
        propToExpr.put(propKey, path);
        return (T) rv;
    }

    protected String propertyNameForGetter(Method method) {
        String name = method.getName();
        name = name.startsWith("is") ? name.substring(2) : name.substring(3);
        return BeanUtils.uncapitalize(name);
    }

    protected PathMetadata<String> createPropertyPath(Path<?> path, String propertyName) {
        return PathMetadataFactory.forProperty(path, propertyName);
    }

    protected PathMetadata<Integer> createListAccessPath(Path<?> path, Integer index) {
        return PathMetadataFactory.forListAccess(path, index);
    }

    protected PathMetadata<?> createMapAccessPath(Path<?> path, Object key) {
        return PathMetadataFactory.forMapAccess(path, key);
    }

}