/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.alias;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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
import com.mysema.query.grammar.types.ColTypes.ExtString;
import com.mysema.query.grammar.types.Path.PCollection;
import com.mysema.query.grammar.types.Path.PList;

/**
 * PropertyAccessInvocationHandler provides
 *
 * @author tiwe
 * @version $Id$
 */
class PropertyAccessInvocationHandler implements MethodInterceptor{
    
    private AliasFactory aliasFactory;
    
    private Class<?> elementType;
    
    private final Map<String,Expr<?>> propToExpr = new HashMap<String,Expr<?>>();
    
    private final Map<String,Object> propToObj = new HashMap<String,Object>();
    
    public PropertyAccessInvocationHandler(AliasFactory aliasFactory){       
        this.aliasFactory = aliasFactory;
    }
    
    private Class<?> getFirstTypeParameter(Type type) {
        if (type instanceof ParameterizedType){
            return (Class<?>)((ParameterizedType)type).getActualTypeArguments()[0];
        }else{
            return null;
        }
    }
    
    public Object intercept(Object proxy, Method method, Object[] args,
            MethodProxy methodProxy) throws Throwable {        
        Expr<?> parent = aliasFactory.pathForAlias(proxy);
        Object rv = null;
        if (isGetter(method)){
            String ptyName = propertyNameForGetter(method);
            Class<?> ptyClass = method.getReturnType();
            
            if (propToObj.containsKey(ptyName)){
                rv = propToObj.get(ptyName);
            }else{                
                if (parent == null) throw new IllegalArgumentException("No path for " + proxy);
                PathMetadata<String> pm = PathMetadata.forProperty((Path<?>) parent, ptyName);
                rv = makeNew(ptyClass, proxy, ptyName, pm);            
                if (Collection.class.isAssignableFrom(ptyClass)){
                    ((ManagedObject)rv).setElementType(getFirstTypeParameter(method.getGenericReturnType()));
                }
            }       
            aliasFactory.setCurrent(propToExpr.get(ptyName));                        
            
            
        }else if (isSizeAccessor(method)){
            String ptyName = "_size";
            if (propToObj.containsKey(ptyName)){
                rv = propToObj.get(ptyName);
            }else{
                PathMetadata<Integer> pm = PathMetadata.forSize((PCollection<?>) parent);
                rv = makeNew(Integer.class, proxy, ptyName, pm);            
            }       
            aliasFactory.setCurrent(propToExpr.get(ptyName));
            
        }else if (isElementAccess(method)){
            String ptyName = "_get" + args[0];
            if (propToObj.containsKey(ptyName)){
                rv = propToObj.get(ptyName);
            }else{
                PathMetadata<Integer> pm = PathMetadata.forListAccess((PList<?>)parent, (Integer)args[0]);
                if (elementType != null){
                    rv = makeNew(elementType, proxy, ptyName, pm);    
                }else{
                    rv = makeNew(method.getReturnType(), proxy, ptyName, pm);
                }                            
            }       
            aliasFactory.setCurrent(propToExpr.get(ptyName)); 
            
        }else if (isEquals(method)){    
            rv = methodProxy.invokeSuper(proxy, args);    
            aliasFactory.setCurrent(((Expr<Object>)parent).eq(args[0]));
            
        }else if (isContains(method)){    
            rv = false;
            aliasFactory.setCurrent(Grammar.in(args[0], (CollectionType<Object>)parent));
                        
        }else if (method.getName().equals("setElementType")){    
            elementType = (Class<?>) args[0];
            
        }else{
            rv = methodProxy.invokeSuper(proxy, args);    
        }        
        return rv; 
    }
    
    private boolean isContains(Method method){
        return method.getName().equals("contains")
            && method.getParameterTypes().length == 1
            && method.getReturnType().equals(boolean.class);
    }

    private boolean isElementAccess(Method method) {
        return method.getName().equals("get") 
            && method.getParameterTypes().length == 1 
            && method.getParameterTypes()[0].equals(int.class);
    }

    private boolean isEquals(Method method){
        return method.getName().equals("equals")
            && method.getReturnType().equals(boolean.class)
            && method.getParameterTypes().length == 1;
    }
    
    private boolean isGetter(Method method){
        return method.getParameterTypes().length == 0 
            && (method.getName().startsWith("is") 
            || method.getName().startsWith("get"));
    }
    
    private boolean isSizeAccessor(Method method) {
        return method.getName().equals("size") 
            && method.getParameterTypes().length == 0
            && method.getReturnType().equals(int.class);
    }

    @SuppressWarnings("unchecked")
    private <T> T makeNew(Class<T> type, Object parent, String prop, PathMetadata<?> pm) {        
        Expr<?> path;
        T rv;
        
        if (String.class.equals(type)) {
            path = new ExtString(pm);
            rv = (T) new String();
        
        // primitive types   
            
        } else if (Integer.class.equals(type) || int.class.equals(type)) {
            path = new Path.PComparable<Integer>(Integer.class,pm);
            rv =  (T) new Integer(42);
            
        } else if (Date.class.equals(type)) {
            path = new Path.PComparable<Date>(Date.class,pm);
            rv =  (T) new Date();
            
        } else if (Long.class.equals(type) || long.class.equals(type)) {
            path = new Path.PComparable<Long>(Long.class,pm);
            rv =  (T) new Long(42);
            
        } else if (Short.class.equals(type) || short.class.equals(type)) {
            path = new Path.PComparable<Short>(Short.class,pm);
            rv =  (T) new Short((short) 42);
            
        } else if (Double.class.equals(type) || double.class.equals(type)) {
            path = new Path.PComparable<Double>(Double.class,pm);
            rv =  (T) new Double(42);
            
        } else if (Float.class.equals(type) || float.class.equals(type)) {
            path = new Path.PComparable<Float>(Float.class,pm);
            rv =  (T) new Float(42);
            
        } else if (BigInteger.class.equals(type)) {
            path = new Path.PComparable<BigInteger>(BigInteger.class,pm);
            rv =  (T) new BigInteger("42");
            
        } else if (BigDecimal.class.equals(type)) {
            path = new Path.PComparable<BigDecimal>(BigDecimal.class,pm);
            rv =  (T) new BigDecimal(42);
            
        } else if (Boolean.class.equals(type) || boolean.class.equals(type)) {
            path = new Path.PComparable<Boolean>(Boolean.class,pm);
            rv =  (T) new Boolean(true);
            
        // Collection API types
            
        } else if (List.class.isAssignableFrom(type)) {
            path = new Path.PComponentList(null,pm);
            rv = (T) aliasFactory.createAliasForProp(type, parent, path);
            
        } else if (Set.class.isAssignableFrom(type)) {
            path = new Path.PComponentCollection(null,pm);
            rv = (T) aliasFactory.createAliasForProp(type, parent, path);
            
        } else if (Collection.class.isAssignableFrom(type)) {
            path = new Path.PComponentCollection(null,pm);
            rv = (T) aliasFactory.createAliasForProp(type, parent, path);
            
        } else if (Map.class.isAssignableFrom(type)) {
            path = new Path.PComponentMap(null,null,pm);
            rv = (T) aliasFactory.createAliasForProp(type, parent, path);
                        
        // enums    
            
        } else if (Enum.class.isAssignableFrom(type)) {
            path = new Path.PSimple<T>(type, pm);
            rv =  type.getEnumConstants()[0];
            
        } else {
            path = new Path.PSimple<T>((Class<T>)type, pm);
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
