/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.alias;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PathMetadataFactory;
import com.mysema.query.util.FactoryMap;

/**
 * AliasFactory is a factory class for alias creation
 * 
 * @author tiwe
 * @version $Id$
 */
class AliasFactory {

    private final ThreadLocal<Expr<?>> current = new ThreadLocal<Expr<?>>();

    // caches top level paths (class/var as key)
    private FactoryMap<PEntity<?>> pathCache = new FactoryMap<PEntity<?>>() {
        @SuppressWarnings("unused")
        public <A> PEntity<A> create(Class<A> cl, String var) {
            return new PEntity<A>(cl, PathMetadataFactory.forVariable(var));
        }
    };

    // caches top level proxies (class/var as key)
    private FactoryMap<ManagedObject> proxyCache = new FactoryMap<ManagedObject>() {
        @SuppressWarnings("unused")
        public ManagedObject create(Class<?> cl, Expr<?> path) {
            return (ManagedObject) createProxy(cl, path);
        }
    };

    @SuppressWarnings("unchecked")
    public <A> A createAliasForExpr(Class<A> cl, Expr<? extends A> expr) {
        return (A) proxyCache.get(cl, expr);
    }

    public <A> A createAliasForProperty(Class<A> cl, Object parent, Expr<?> path) {
        A proxy = createProxy(cl, path);
        return proxy;
    }

    @SuppressWarnings("unchecked")
    public <A> A createAliasForVariable(Class<A> cl, String var) {
        Expr<?> path = pathCache.get(cl, var);
        A proxy = (A) proxyCache.get(cl, path);
        return proxy;
    }

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
        A rv = (A) enhancer.create();
        return rv;
    }

    @SuppressWarnings("unchecked")
    public <A extends Expr<?>> A getCurrent() {
        return (A) current.get();
    }

    public <A extends Expr<?>> A getCurrentAndReset() {
        A rv = this.<A> getCurrent();
        reset();
        return rv;
    }

    public void reset() {
        current.set(null);
    }

    public void setCurrent(Expr<?> path) {
        current.set(path);
    }

}