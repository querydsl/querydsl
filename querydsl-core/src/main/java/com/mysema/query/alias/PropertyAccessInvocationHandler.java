/*
 * Copyright (c) 2008 Mysema Ltd.
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
import java.util.*;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.apache.commons.lang.StringUtils;

import com.mysema.query.grammar.Grammar;
import com.mysema.query.grammar.types.CollectionType;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Path;
import com.mysema.query.grammar.types.PathMetadata;
import com.mysema.query.grammar.types.ExtTypes.ExtString;
import com.mysema.query.grammar.types.Path.PCollection;
import com.mysema.query.grammar.types.Path.PEntity;
import com.mysema.query.grammar.types.Path.PList;

/**
 * PropertyAccessInvocationHandler is the main InvocationHandler class for the CGLIB alias proxies
 *
 * @author tiwe
 * @version $Id$
 */
class PropertyAccessInvocationHandler implements MethodInterceptor{
    
    private final Expr<?> path;
    
    private final AliasFactory aliasFactory;
        
    private final Map<String,Expr<?>> propToExpr = new HashMap<String,Expr<?>>();
    
    private final Map<String,Object> propToObj = new HashMap<String,Object>();
    
    public PropertyAccessInvocationHandler(Expr<?> path, AliasFactory aliasFactory){    
        this.path = path;
        this.aliasFactory = aliasFactory;
    }
    
    private Class<?> getTypeParameter(Type type, int index) {
        if (type instanceof ParameterizedType){
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
    
    public Object intercept(Object proxy, Method method, Object[] args,
            MethodProxy methodProxy) throws Throwable {      
        Object rv = null;
        
        if (isGetter(method)){
            String ptyName = propertyNameForGetter(method);
            Class<?> ptyClass = method.getReturnType();
            Type genericType = method.getGenericReturnType();
            
            if (propToObj.containsKey(ptyName)){
                rv = propToObj.get(ptyName);
            }else{                
                PathMetadata<String> pm = PathMetadata.forProperty((Path<?>) path, ptyName);
                rv = newInstance(ptyClass, genericType, proxy, ptyName, pm);            
            }       
            aliasFactory.setCurrent(propToExpr.get(ptyName));                        
            
            
        }else if (isSizeAccessor(method)){
            String ptyName = "_size";
            if (propToObj.containsKey(ptyName)){
                rv = propToObj.get(ptyName);
            }else{
                PathMetadata<Integer> pm = PathMetadata.forSize((PCollection<?>) path);
                rv = newInstance(Integer.class, Integer.class, proxy, ptyName, pm);            
            }       
            aliasFactory.setCurrent(propToExpr.get(ptyName));
            
        }else if (isListElementAccess(method)){
            // TODO : manage cases where the argument is based on a property invocation
            String ptyName = "_get" + args[0];
            if (propToObj.containsKey(ptyName)){
                rv = propToObj.get(ptyName);
            }else{
                PathMetadata<Integer> pm = PathMetadata.forListAccess((PList<?>)path, (Integer)args[0]);
                Class<?> elementType = ((PCollection<?>)path).getElementType();
                if (elementType != null){
                    rv = newInstance(elementType, elementType, proxy, ptyName, pm);    
                }else{
                    rv = newInstance(method.getReturnType(), method.getGenericReturnType(), proxy, ptyName, pm);
                }                            
            }       
            aliasFactory.setCurrent(propToExpr.get(ptyName)); 
            
        }else if (isMapElementAccess(method)){
           // TODO
            
        }else if (isContains(method)){    
            rv = false;
            aliasFactory.setCurrent(Grammar.in(args[0], (CollectionType<Object>)path));
                        
        }else if (isToString(method)){
            rv = path.toString();
            
        }else if (isHashCode(method)){    
            rv = path.hashCode();
            
        }else if (isGetMappedPath(method)){
            rv = path;
            
        }else{
//            rv = methodProxy.invokeSuper(proxy, args);
            throw new IllegalArgumentException("Invocation of " + method.getName() + " not supported");
        }        
        return rv; 
    }
    
    private boolean isToString(Method method){
        return checkMethod(method, "toString", 0, String.class);
    }
    
    private boolean isContains(Method method){
        return checkMethod(method, "contains", 1, boolean.class);
    }

    private boolean isGetMappedPath(Method method){
        return checkMethod(method, "__mappedPath", 0, PEntity.class);
    }
    
    private boolean isListElementAccess(Method method) {
        return checkMethod(method, "get", 1, int.class);        
    }
    
    private boolean isHashCode(Method method){
        return checkMethod(method, "hashCode", 0, int.class);        
    }
    
    private boolean isMapElementAccess(Method method) {
        return checkMethod(method, "get", 1, Object.class);        
    }
        
    private boolean isGetter(Method method){
        return method.getParameterTypes().length == 0 
            && (method.getName().startsWith("is") 
            || method.getName().startsWith("get"));
    }
    
    private boolean isSizeAccessor(Method method) {
        return checkMethod(method, "size", 0, int.class);        
    }
    
    private boolean checkMethod(Method method, String name, int paramCount, Class<?> returnType){
        boolean rv = method.getName().equals(name);
        rv &= method.getParameterTypes().length == paramCount;
        rv &= method.getReturnType().equals(returnType);
        return rv;        
    }

    @SuppressWarnings("unchecked")
    private <T> T newInstance(Class<T> type, Type genericType, Object parent, String prop, PathMetadata<?> pm) {        
        Expr<?> path;
        T rv;
        
        if (String.class.equals(type)) {
            path = new ExtString(pm);
            // null is used as a return value to block method invocations on Strings
            rv = null;
            
        } else if (Integer.class.equals(type) || int.class.equals(type)) {
            path = new Path.PNumber<Integer>(Integer.class,pm);
            rv =  (T) Integer.valueOf(42);
            
        } else if (Date.class.equals(type)) {
            path = new Path.PComparable<Date>(Date.class,pm);
            rv =  (T) new Date();
            
        } else if (Long.class.equals(type) || long.class.equals(type)) {
            path = new Path.PNumber<Long>(Long.class,pm);
            rv =  (T) Long.valueOf(42l);
            
        } else if (Short.class.equals(type) || short.class.equals(type)) {
            path = new Path.PComparable<Short>(Short.class,pm);
            rv =  (T) Short.valueOf((short)42);
            
        } else if (Double.class.equals(type) || double.class.equals(type)) {
            path = new Path.PNumber<Double>(Double.class,pm);
            rv =  (T) Double.valueOf(42d);
            
        } else if (Float.class.equals(type) || float.class.equals(type)) {
            path = new Path.PNumber<Float>(Float.class,pm);
            rv =  (T) Float.valueOf(42f);
            
        } else if (BigInteger.class.equals(type)) {
            path = new Path.PNumber<BigInteger>(BigInteger.class,pm);
            rv =  (T) BigInteger.valueOf(42l);
            
        } else if (BigDecimal.class.equals(type)) {
            path = new Path.PNumber<BigDecimal>(BigDecimal.class,pm);
            rv =  (T) BigDecimal.valueOf(42d);
            
        } else if (Boolean.class.equals(type) || boolean.class.equals(type)) {
            path = new Path.PBoolean(pm);
            rv =  (T) Boolean.TRUE;
            
        // Collection API types
            
        } else if (List.class.isAssignableFrom(type)) {
            Class<?> _elementType = getTypeParameter(genericType, 0);
            path = new Path.PEntityList(_elementType, _elementType.getSimpleName(), pm);
            rv = (T) aliasFactory.createAliasForProp(type, parent, path);
            
        } else if (Set.class.isAssignableFrom(type)) {
            Class<?> _elementType = getTypeParameter(genericType, 0);
            path = new Path.PEntityCollection(_elementType, _elementType.getName(), pm);
            rv = (T) aliasFactory.createAliasForProp(type, parent, path);
            
        } else if (Collection.class.isAssignableFrom(type)) {
            Class<?> _elementType = getTypeParameter(genericType, 0);
            path = new Path.PEntityCollection(_elementType, _elementType.getSimpleName(), pm);
            rv = (T) aliasFactory.createAliasForProp(type, parent, path);
            
        } else if (Map.class.isAssignableFrom(type)) {
            Class<?> _keyType = getTypeParameter(genericType, 0);
            Class<?> _valueType = getTypeParameter(genericType, 1);
            path = new Path.PEntityMap(_keyType,_valueType,_valueType.getSimpleName(), pm);
            rv = (T) aliasFactory.createAliasForProp(type, parent, path);
                        
        // enums    
            
        } else if (Enum.class.isAssignableFrom(type)) {
            path = new Path.PSimple<T>(type, pm);
            rv =  type.getEnumConstants()[0];
            
        } else {
            path = new Path.PEntity<T>((Class<T>)type, type.getSimpleName(), pm);
            rv = (T) aliasFactory.createAliasForProp(type, parent, path);            
        }
        propToObj.put(prop, rv);
        propToExpr.put(prop, path);        
        return rv;
    }
    
    private String propertyNameForGetter(Method method) {
        String name = method.getName();
        name = name.startsWith("is") ? name.substring(2) : name.substring(3);
        return StringUtils.uncapitalize(name);
    }

}
