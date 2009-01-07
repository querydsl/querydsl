/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.alias;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Path;
import com.mysema.query.grammar.types.PathMetadata;
import com.mysema.query.grammar.types.Path.PSimple;
import com.mysema.query.util.FactoryMap;

/**
 * AliasFactory provides
 *
 * @author tiwe
 * @version $Id$
 */
public class AliasFactory {
    
    private final ThreadLocal<WeakIdentityHashMap<Object, Expr<?>>> bindings = new ThreadLocal<WeakIdentityHashMap<Object, Expr<?>>>() {
        @Override
        protected WeakIdentityHashMap<Object, Expr<?>> initialValue() {
                return new WeakIdentityHashMap<Object, Expr<?>>();
        }
    };
    
    private final ThreadLocal<Expr<?>> current = new ThreadLocal<Expr<?>>();
    
    // caches top level paths (class/var as key)
    private FactoryMap<PSimple<?>> pathCache = new FactoryMap<PSimple<?>>(){
        public <A> PSimple<A> create(Class<A> cl, String var){
            return new Path.PSimple<A>(cl, PathMetadata.forVariable(var));
        }
    };
    
    // cahces top level proxies (class/var as key)
    private FactoryMap<Object> proxyCache = new FactoryMap<Object>(){
        public <A> Object create(Class<A> cl, String var){
            return createProxy(cl);
        }
    };
    
    public <A> A createAliasForProp(Class<A> cl, Object parent, Expr<?> path){        
        A proxy = createProxy(cl);
        if (!cl.getPackage().getName().equals("java.lang")){
            bindings.get().put(proxy, path);    
        }
        return proxy;
    }
        
    @SuppressWarnings("unchecked")
    public <A> A createAliasForVar(Class<A> cl, String var){    
        Expr<?> path = pathCache.get(cl,var);
        A proxy = (A) proxyCache.get(cl, var);
        bindings.get().put(proxy, path);
        return proxy;
    }
    
    @SuppressWarnings("unchecked")
    private <A> A createProxy(Class<A> cl) {
        Enhancer enhancer = new Enhancer();
        enhancer.setClassLoader(AliasFactory.class.getClassLoader());
        if (cl.isInterface()){
            enhancer.setInterfaces(new Class[]{cl,ManagedObject.class});
        }else{
            enhancer.setSuperclass(cl);    
            enhancer.setInterfaces(new Class[]{ManagedObject.class});
        }         
        // creates one handler per proxy
        MethodInterceptor handler = new PropertyAccessInvocationHandler(this);
        enhancer.setCallback(handler);
        A rv = (A)enhancer.create();
        return rv;
    }
    
    @SuppressWarnings("unchecked")
    public <A extends Expr<?>> A getCurrent() {
        return (A) current.get();
    }

    public boolean hasCurrent() {
        return current.get() != null;
    }
    
    public Expr<?> pathForAlias(Object key){
        return bindings.get().get(key);
    }

    public void setCurrent(Expr<?> path){
        current.set(path);
    }
    
}
