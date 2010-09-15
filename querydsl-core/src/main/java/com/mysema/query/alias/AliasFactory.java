/*
 * Copyright (c) 2010 Mysema Ltd.
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
import com.mysema.query.types.Expression;
import com.mysema.query.types.path.EntityPathBase;

/**
 * AliasFactory is a factory class for alias creation
 *
 * @author tiwe
 * @version $Id$
 */
public class AliasFactory {

    private final ThreadLocal<Expression<?>> current = new ThreadLocal<Expression<?>>();

    // caches top level paths (class/var as key)
    private final Map<Pair<Class<?>,String>, EntityPathBase<?>> pathCache =
        LazyMap.decorate(new HashMap<Pair<Class<?>,String>,EntityPathBase<?>>(), new PEntityTransformer());

    private final Map<Pair<Class<?>,Expression<?>>, ManagedObject> proxyCache =
        LazyMap.decorate(
            new HashMap<Pair<Class<?>,Expression<?>>,ManagedObject>(),
            new Transformer<Pair<Class<?>,Expression<?>>,ManagedObject>(){
                @Override
                public ManagedObject transform(Pair<Class<?>, Expression<?>> input) {
                    return (ManagedObject) createProxy(input.getFirst(), input.getSecond());
                }
            });

    private final PathFactory pathFactory;
    
    public AliasFactory(PathFactory pathFactory){
        this.pathFactory = pathFactory; 
    }
    
    /**
     * @param <A>
     * @param cl
     * @param expr
     * @return
     */
    @SuppressWarnings("unchecked")
    public <A> A createAliasForExpr(Class<A> cl, Expression<? extends A> expr) {
        return (A) proxyCache.get(Pair.of(cl, expr));
    }

    /**
     * @param <A>
     * @param cl
     * @param parent
     * @param path
     * @return
     */
    public <A> A createAliasForProperty(Class<A> cl, Object parent, Expression<?> path) {
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
        Expression<?> path = pathCache.get(Pair.of(cl, var));
        return (A) proxyCache.get(Pair.of(cl, path));
    }

    /**
     * @param <A>
     * @param cl
     * @param path
     * @return
     */
    @SuppressWarnings("unchecked")
    private <A> A createProxy(Class<A> cl, Expression<?> path) {
        Enhancer enhancer = new Enhancer();
        enhancer.setClassLoader(AliasFactory.class.getClassLoader());
        if (cl.isInterface()) {
            enhancer.setInterfaces(new Class[] { cl, ManagedObject.class });
        } else {
            enhancer.setSuperclass(cl);
            enhancer.setInterfaces(new Class[] { ManagedObject.class });
        }
        // creates one handler per proxy
        MethodInterceptor handler = new PropertyAccessInvocationHandler(path, this, pathFactory);
        enhancer.setCallback(handler);
        return (A) enhancer.create();
    }

    /**
     * @param <A>
     * @return
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public <A extends Expression<?>> A getCurrent() {
        return (A) current.get();
    }

    /**
     * @param <A>
     * @return
     */
    @Nullable
    public <A extends Expression<?>> A getCurrentAndReset() {
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
    public void setCurrent(Expression<?> path) {
        current.set(path);
    }

}
