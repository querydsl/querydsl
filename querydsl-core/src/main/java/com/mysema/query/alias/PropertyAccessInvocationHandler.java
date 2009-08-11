/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.alias;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.apache.commons.lang.StringUtils;

import com.mysema.query.types.expr.ECollection;
import com.mysema.query.types.expr.EMap;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PBoolean;
import com.mysema.query.types.path.PComparable;
import com.mysema.query.types.path.PDate;
import com.mysema.query.types.path.PDateTime;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PEntityCollection;
import com.mysema.query.types.path.PEntityList;
import com.mysema.query.types.path.PEntityMap;
import com.mysema.query.types.path.PList;
import com.mysema.query.types.path.PMap;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PSimple;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PTime;
import com.mysema.query.types.path.Path;
import com.mysema.query.types.path.PathMetadata;

/**
 * PropertyAccessInvocationHandler is the main InvocationHandler class for the
 * CGLIB alias proxies
 * 
 * @author tiwe
 * @version $Id$
 */
class PropertyAccessInvocationHandler implements MethodInterceptor {

    private final Expr<?> path;

    private final AliasFactory aliasFactory;

    private final Map<Object, Expr<?>> propToExpr = new HashMap<Object, Expr<?>>();

    private final Map<Object, Object> propToObj = new HashMap<Object, Object>();

    public PropertyAccessInvocationHandler(Expr<?> path, AliasFactory aliasFactory) {
        this.path = path;
        this.aliasFactory = aliasFactory;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    private Class<?> getTypeParameter(Type type, int index) {
        if (type instanceof ParameterizedType) {
            ParameterizedType ptype = (ParameterizedType) type;
            Type[] targs = ptype.getActualTypeArguments();
            if (targs[index] instanceof WildcardType) {
                WildcardType wildcardType = (WildcardType) targs[index];
                return (Class<?>) wildcardType.getUpperBounds()[0];
            } else if (targs[index] instanceof TypeVariable) {
                return (Class<?>) ((TypeVariable) targs[index]).getGenericDeclaration();
            } else if (targs[index] instanceof ParameterizedType) {
                return (Class<?>) ((ParameterizedType) targs[index]).getRawType();
            } else {
                try {
                    return (Class<?>) targs[index];
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Object rv = null;

        if (isGetter(method)) {
            String ptyName = propertyNameForGetter(method);
            Class<?> ptyClass = method.getReturnType();
            Type genericType = method.getGenericReturnType();

            if (propToObj.containsKey(ptyName)) {
                rv = propToObj.get(ptyName);
            } else {
                PathMetadata<String> pm = PathMetadata.forProperty((Path<?>) path, ptyName);
                rv = newInstance(ptyClass, genericType, proxy, ptyName, pm);
            }
            aliasFactory.setCurrent(propToExpr.get(ptyName));

//        } else if (isSizeAccessor(method)) {
//            Object propKey = "_size";
//            if (propToObj.containsKey(propKey)) {
//                rv = propToObj.get(propKey);
//            } else {
//                PathMetadata<Integer> pm = PathMetadata.forSize((PCollection<?>) path);
//                rv = newInstance(Integer.class, Integer.class, proxy, propKey, pm);
//            }
//            aliasFactory.setCurrent(propToExpr.get(propKey));

        } else if (isListElementAccess(method)) {
            // TODO : manage cases where the argument is based on a property invocation
            Object propKey = Arrays.asList("_get_list", args[0]);
            if (propToObj.containsKey(propKey)) {
                rv = propToObj.get(propKey);
            } else {
                PathMetadata<Integer> pm = PathMetadata.forListAccess((PList<?>) path, (Integer) args[0]);
                Class<?> elementType = ((ECollection<?>) path).getElementType();
                if (elementType != null) {
                    rv = newInstance(elementType, elementType, proxy, propKey, pm);
                } else {
                    rv = newInstance(method.getReturnType(), method.getGenericReturnType(), proxy, propKey, pm);
                }
            }
            aliasFactory.setCurrent(propToExpr.get(propKey));

        } else if (isMapElementAccess(method)) {
            Object propKey = Arrays.asList("_get_map", args[0]);
            if (propToObj.containsKey(propKey)) {
                rv = propToObj.get(propKey);
            } else {
                PathMetadata<?> pm = PathMetadata.forMapAccess((PMap<?, ?>) path, args[0]);
                Class<?> valueType = ((EMap<?, ?>) path).getValueType();
                if (valueType != null) {
                    rv = newInstance(valueType, valueType, proxy, propKey, pm);
                } else {
                    rv = newInstance(method.getReturnType(), method.getGenericReturnType(), proxy, propKey, pm);
                }
            }
            aliasFactory.setCurrent(propToExpr.get(propKey));

        } else if (isToString(method)) {
            rv = path.toString();

        } else if (isHashCode(method)) {
            rv = path.hashCode();

        } else if (isGetMappedPath(method)) {
            rv = path;

        } else {
            throw new IllegalArgumentException("Invocation of " + method.getName() + " not supported");
        }
        return rv;
    }

    private boolean isToString(Method method) {
        return checkMethod(method, "toString", 0, String.class);
    }

    private boolean isGetMappedPath(Method method) {
        return checkMethod(method, "__mappedPath", 0, PEntity.class);
    }

    private boolean isListElementAccess(Method method) {
        return method.getName().equals("get")
                && method.getParameterTypes().length == 1
                && method.getParameterTypes()[0].equals(int.class);
    }

    private boolean isHashCode(Method method) {
        return checkMethod(method, "hashCode", 0, int.class);
    }

    private boolean isMapElementAccess(Method method) {
        return checkMethod(method, "get", 1, Object.class);
    }

    private boolean isGetter(Method method) {
        if (method.getParameterTypes().length == 0) {
            if (method.getName().startsWith("get")) {
                return !method.getReturnType().equals(void.class);
            } else if (method.getName().startsWith("is")) {
                return (method.getReturnType().equals(boolean.class) || method.getReturnType().equals(Boolean.class));
            }
        }
        return false;
    }

//    private boolean isSizeAccessor(Method method) {
//        return checkMethod(method, "size", 0, int.class);
//    }

    private boolean checkMethod(Method method, String name, int paramCount, Class<?> returnType) {
        if (!method.getName().equals(name))
            return false;
        if (!(method.getParameterTypes().length == paramCount))
            return false;
        if (!method.getReturnType().equals(returnType))
            return false;
        return true;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    private <T> T newInstance(Class<T> type, Type genericType, Object parent, Object propKey, PathMetadata<?> pm) {
        Expr<?> path;
        T rv;

        if (String.class.equals(type)) {
            path = new PString(pm);
            // null is used as a return value to block method invocations on Strings
            rv = null;

        } else if (Integer.class.equals(type) || int.class.equals(type)) {
            path = new PNumber<Integer>(Integer.class, pm);
            rv = (T) Integer.valueOf(42);

        } else if (java.util.Date.class.equals(type)) {
            path = new PDateTime<Date>(Date.class, pm);
            rv = (T) new Date();
            
        } else if (java.sql.Timestamp.class.equals(type)) {
            path = new PDateTime<Timestamp>(Timestamp.class, pm);
            rv = (T) new Timestamp(System.currentTimeMillis());
            
        } else if (java.sql.Date.class.equals(type)) {
            path = new PDate<java.sql.Date>(java.sql.Date.class, pm);
            rv = (T) new java.sql.Date(System.currentTimeMillis());
            
        } else if (java.sql.Time.class.equals(type)) {
            path = new PTime<java.sql.Time>(java.sql.Time.class, pm);
            rv = (T) new java.sql.Time(System.currentTimeMillis());

        } else if (Long.class.equals(type) || long.class.equals(type)) {
            path = new PNumber<Long>(Long.class, pm);
            rv = (T) Long.valueOf(42l);

        } else if (Short.class.equals(type) || short.class.equals(type)) {
            path = new PComparable<Short>(Short.class, pm);
            rv = (T) Short.valueOf((short) 42);

        } else if (Double.class.equals(type) || double.class.equals(type)) {
            path = new PNumber<Double>(Double.class, pm);
            rv = (T) Double.valueOf(42d);

        } else if (Float.class.equals(type) || float.class.equals(type)) {
            path = new PNumber<Float>(Float.class, pm);
            rv = (T) Float.valueOf(42f);

        } else if (BigInteger.class.equals(type)) {
            path = new PNumber<BigInteger>(BigInteger.class, pm);
            rv = (T) BigInteger.valueOf(42l);

        } else if (BigDecimal.class.equals(type)) {
            path = new PNumber<BigDecimal>(BigDecimal.class, pm);
            rv = (T) BigDecimal.valueOf(42d);

        } else if (Boolean.class.equals(type) || boolean.class.equals(type)) {
            path = new PBoolean(pm);
            rv = (T) Boolean.TRUE;

            // Collection API types

        } else if (List.class.isAssignableFrom(type)) {
            Class<?> _elementType = getTypeParameter(genericType, 0);
            path = new PEntityList(_elementType, _elementType.getSimpleName(), pm);
            rv = (T) aliasFactory.createAliasForProp(type, parent, path);

        } else if (Set.class.isAssignableFrom(type)) {
            Class<?> _elementType = getTypeParameter(genericType, 0);
            path = new PEntityCollection(_elementType, _elementType.getName(), pm);
            rv = (T) aliasFactory.createAliasForProp(type, parent, path);

        } else if (Collection.class.isAssignableFrom(type)) {
            Class<?> _elementType = getTypeParameter(genericType, 0);
            path = new PEntityCollection(_elementType, _elementType.getSimpleName(), pm);
            rv = (T) aliasFactory.createAliasForProp(type, parent, path);

        } else if (Map.class.isAssignableFrom(type)) {
            Class<?> _keyType = getTypeParameter(genericType, 0);
            Class<?> _valueType = getTypeParameter(genericType, 1);
            path = new PEntityMap(_keyType, _valueType, _valueType.getSimpleName(), pm);
            rv = (T) aliasFactory.createAliasForProp(type, parent, path);

            // enums

        } else if (Enum.class.isAssignableFrom(type)) {
            path = new PSimple<T>(type, pm);
            rv = type.getEnumConstants()[0];

        } else {
            path = new PEntity<T>((Class<T>) type, type.getSimpleName(), pm);
            rv = (T) aliasFactory.createAliasForProp(type, parent, path);
        }
        propToObj.put(propKey, rv);
        propToExpr.put(propKey, path);
        return rv;
    }

    private String propertyNameForGetter(Method method) {
        String name = method.getName();
        name = name.startsWith("is") ? name.substring(2) : name.substring(3);
        return StringUtils.uncapitalize(name);
    }

}
