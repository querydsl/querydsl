/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.alias;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.map.LazyMap;

import com.mysema.commons.lang.Pair;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PathMetadataFactory;

/**
 * AliasFactory is a factory class for alias creation
 * 
 * @author tiwe
 * @version $Id$
 */
public class AliasFactory {

    private final ThreadLocal<Expr<?>> current = new ThreadLocal<Expr<?>>();

    // caches top level paths (class/var as key)
    private final Map<Pair<Class<?>,String>, PEntity<?>> pathCache =
        LazyMap.decorate(
            new HashMap<Pair<Class<?>,String>,PEntity<?>>(),
            new Transformer<Pair<Class<?>,String>,PEntity<?>>(){
                @SuppressWarnings("unchecked")
                @Override
                public PEntity<?> transform(Pair<Class<?>, String> input) {
                    return new PEntity(input.getFirst(), PathMetadataFactory.forVariable(input.getSecond()));
                }                
            });
            
    private final Map<Pair<Class<?>,Expr<?>>, ManagedObject> proxyCache =
        LazyMap.decorate(
            new HashMap<Pair<Class<?>,Expr<?>>,ManagedObject>(),
            new Transformer<Pair<Class<?>,Expr<?>>,ManagedObject>(){
                @Override
                public ManagedObject transform(Pair<Class<?>, Expr<?>> input) {
                    return (ManagedObject) createProxy(input.getFirst(), input.getSecond());
                }                
            });    

    /**
     * @param <A>
     * @param cl
     * @param expr
     * @return
     */
    @SuppressWarnings("unchecked")
    public <A> A createAliasForExpr(Class<A> cl, Expr<? extends A> expr) {
        return (A) proxyCache.get(Pair.of(cl, expr));
    }

    /**
     * @param <A>
     * @param cl
     * @param parent
     * @param path
     * @return
     */
    public <A> A createAliasForProperty(Class<A> cl, Object parent, Expr<?> path) {
        return createProxy(cl, path);
    }

    /**
     * @param <A>
     * @param cl
     * @param var
     * @return
     */
    @SuppressWarnings("unchecked")
    public <A> A createAliasForVariable(Class<A> cl, String var) {
        Expr<?> path = pathCache.get(Pair.of(cl, var));
        return (A) proxyCache.get(Pair.of(cl, path));
    }

    /**
     * @param <A>
     * @param cl
     * @param path
     * @return
     */
    @SuppressWarnings("unchecked")
    private <A> A createProxy(Class<A> cl, Expr<?> path) {
        Enhancer enhancer = new Enhancer();
        enhancer.setClassLoader(AliasFactory.class.getClassLoader());
        if (cl.isInterface()) {
            enhancer.setInterfaces(new Class[] { cl, ManagedObject.class });
        } else {
            enhancer.setSuperclass(cl);
            enhancer.setInterfaces(new Class[] { ManagedObject.class });
        }
        // creates one handler per proxy
        MethodInterceptor handler = new PropertyAccessInvocationHandler(path, this);
        enhancer.setCallback(handler);
        return (A) enhancer.create();
    }

    /**
     * @param <A>
     * @return
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public <A extends Expr<?>> A getCurrent() {
        return (A) current.get();
    }

    /**
     * @param <A>
     * @return
     */
    @Nullable
    public <A extends Expr<?>> A getCurrentAndReset() {
        A rv = this.<A> getCurrent();
        reset();
        return rv;
    }

    /**
     * 
     */
    public void reset() {
        current.set(null);
    }

    /**
     * @param path
     */
    public void setCurrent(Expr<?> path) {
        current.set(path);
    }

}