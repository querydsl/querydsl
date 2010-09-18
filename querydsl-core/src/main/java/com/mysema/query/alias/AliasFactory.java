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
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Expression;
import com.mysema.query.types.PathMetadataFactory;

/**
 * AliasFactory is a factory class for alias creation
 *
 * @author tiwe
 * @version $Id$
 */
public class AliasFactory {

    private final ThreadLocal<Expression<?>> current = new ThreadLocal<Expression<?>>();

    private final PathFactory pathFactory;
    
    private final TypeSystem typeSystem;
    
    // caches top level paths (class/var as key)
    private final Map<Pair<Class<?>,String>, EntityPath<?>> pathCache;
        
    private final Map<Pair<Class<?>,Expression<?>>, ManagedObject> proxyCache =
        LazyMap.decorate(
            new HashMap<Pair<Class<?>,Expression<?>>,ManagedObject>(),
            new Transformer<Pair<Class<?>,Expression<?>>,ManagedObject>(){
                @Override
                public ManagedObject transform(Pair<Class<?>, Expression<?>> input) {
                    return (ManagedObject) createProxy(input.getFirst(), input.getSecond());
                }
            });
    
    public AliasFactory(final PathFactory pathFactory, TypeSystem typeSystem){
        this.pathFactory = pathFactory; 
        this.typeSystem = typeSystem;
        this.pathCache = LazyMap.decorate(new HashMap<Pair<Class<?>,String>,EntityPath<?>>(), 
            new Transformer<Pair<Class<?>, String>, EntityPath<?>>() {
                @Override
                public EntityPath<?> transform( Pair<Class<?>, String> input) {
                    return (EntityPath<?>)pathFactory.createEntityPath(
                            input.getFirst(), 
                            PathMetadataFactory.forVariable(input.getSecond()));
                }
            });
    }
    
    /**
     * Create an alias instance for the given class and Expression
     * 
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
     * Create an alias instance for the given class, parent and path
     * 
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
     * Create an alias instance for the given class and variable name
     * 
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
     * Create a proxy instance for the given class and path
     * 
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
        MethodInterceptor handler = new PropertyAccessInvocationHandler(path, this, pathFactory, typeSystem);
        enhancer.setCallback(handler);
        return (A) enhancer.create();
    }

    /**
     * Get the current thread bound expression without reseting it
     * 
     * @param <A>
     * @return
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public <A extends Expression<?>> A getCurrent() {
        return (A) current.get();
    }

    /**
     * Get the current thread bound expression and reset it
     * 
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
     * Reset the thread bound expression to null
     */
    public void reset() {
        current.set(null);
    }

    /**
     * Set the thread bound expression to the given value
     * 
     * @param expr
     */
    public void setCurrent(Expression<?> expr) {
        current.set(expr);
    }

}
