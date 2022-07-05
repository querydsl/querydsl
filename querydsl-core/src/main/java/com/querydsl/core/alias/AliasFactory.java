/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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

import com.querydsl.core.QueryException;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadataFactory;
import java.lang.reflect.InvocationTargetException;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * {@code AliasFactory} is a factory class for alias creation
 *
 * @author tiwe
 * @author persapiens
 */
class AliasFactory {

    private final ThreadLocal<Expression<?>> current = new ThreadLocal<Expression<?>>();

    private final PathFactory pathFactory;

    private final TypeSystem typeSystem;

    private final ConcurrentHashMap<Class<?>, Map<Expression<?>, ManagedObject>> proxyCache = new ConcurrentHashMap<>();

    AliasFactory(final PathFactory pathFactory, TypeSystem typeSystem) {
        this.pathFactory = pathFactory;
        this.typeSystem = typeSystem;
    }

    /**
     * Create an alias instance for the given class and Expression
     *
     * @param <A>
     * @param cl type for alias
     * @param expr underlying expression
     * @return alias instance
     */
    @SuppressWarnings("unchecked")
    public <A> A createAliasForExpr(Class<A> cl, Expression<? extends A> expr) {
        try {
            final Map<Expression<?>, ManagedObject> expressionCache = proxyCache.computeIfAbsent(cl, a -> Collections.synchronizedMap(new WeakHashMap<>()));
            return (A) expressionCache.computeIfAbsent(expr, e -> (ManagedObject) createProxy(cl, expr));
        } catch (ClassCastException e) {
           throw new QueryException(e);
        }
    }

    /**
     * Create an alias instance for the given class, parent and path
     *
     * @param <A>
     * @param cl type for alias
     * @param path underlying expression
     * @return alias instance
     */
    public <A> A createAliasForProperty(Class<A> cl, Expression<?> path) {
        return createProxy(cl, path);
    }

    /**
     * Create an alias instance for the given class and variable name
     *
     * @param <A>
     * @param cl type for alias
     * @param var variable name for the underlying expression
     * @return alias instance
     */
    public <A> A createAliasForVariable(Class<A> cl, String var) {
        final Path<A> expr = pathFactory.createEntityPath(cl, PathMetadataFactory.forVariable(var));
        return createAliasForExpr(cl, expr);
    }

    /**
     * Create a proxy instance for the given class and path
     *
     * @param <A>
     * @param cl type of the proxy
     * @param path underlying expression
     * @return proxy instance
     */
    @SuppressWarnings("unchecked")
    protected <A> A createProxy(Class<A> cl, Expression<?> path) {
        Class<? extends A> loaded = new ByteBuddy()
                .subclass(cl)
                .implement(ManagedObject.class)
                .method(ElementMatchers.any())
                .intercept(MethodDelegation.to(new PropertyAccessInvocationHandler(path, this, pathFactory, typeSystem)))
                .make()
                .load(getClass().getClassLoader())
                .getLoaded();
        try {
            return (A) loaded.getDeclaredConstructors()[0].newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(AliasFactory.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Get the current thread bound expression without resetting it
     *
     * @param <A>
     * @return expression
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
     * @return expression
     */
    @Nullable
    public <A extends Expression<?>> A getCurrentAndReset() {
        A rv = this.getCurrent();
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
     * @param expr expression to be set to current
     */
    public void setCurrent(Expression<?> expr) {
        current.set(expr);
    }

}
