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

import java.util.concurrent.ExecutionException;

import javax.annotation.Nullable;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mysema.commons.lang.Pair;
import com.querydsl.core.QueryException;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.PathMetadataFactory;

/**
 * AliasFactory is a factory class for alias creation
 *
 * @author tiwe
 */
public class AliasFactory {

    private final ThreadLocal<Expression<?>> current = new ThreadLocal<Expression<?>>();

    private final PathFactory pathFactory;
    
    private final TypeSystem typeSystem;
    
    // caches top level paths (class/var as key)
    private final LoadingCache<Pair<Class<?>,String>, EntityPath<?>> pathCache;
        
    private final LoadingCache<Pair<Class<?>,Expression<?>>, ManagedObject> proxyCache =
        CacheBuilder.newBuilder().build(
            new CacheLoader<Pair<Class<?>,Expression<?>>,ManagedObject>() {
                @Override
                public ManagedObject load(Pair<Class<?>, Expression<?>> input) {
                    return (ManagedObject) createProxy(input.getFirst(), input.getSecond());
                }
            });
    
    public AliasFactory(final PathFactory pathFactory, TypeSystem typeSystem) {
        this.pathFactory = pathFactory; 
        this.typeSystem = typeSystem;
        this.pathCache = CacheBuilder.newBuilder().build( 
            new CacheLoader<Pair<Class<?>, String>, EntityPath<?>>() {
                @Override
                public EntityPath<?> load( Pair<Class<?>, String> input) {
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
        try {
            return (A) proxyCache.get(Pair.<Class<?>, Expression<?>>of(cl, expr));
        } catch (ExecutionException e) {
           throw new QueryException(e);
        }
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
        try {
            Expression<?> path = pathCache.get(Pair.<Class<?>, String>of(cl, var));
            return (A) proxyCache.get(Pair.<Class<?>, Expression<?>>of(cl, path));
        } catch (ExecutionException e) {
            throw new QueryException(e);
        }        
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
    protected <A> A createProxy(Class<A> cl, Expression<?> path) {
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
