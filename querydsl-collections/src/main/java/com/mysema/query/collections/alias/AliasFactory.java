/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.alias;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

import com.mysema.query.grammar.types.Path;
import com.mysema.query.grammar.types.PathMetadata;

/**
 * AliasFactory provides
 *
 * @author tiwe
 * @version $Id$
 */
public class AliasFactory {
    
    private final ThreadLocal<WeakIdentityHashMap<Object, Path<?>>> bindings = new ThreadLocal<WeakIdentityHashMap<Object, Path<?>>>() {
        @Override
        protected WeakIdentityHashMap<Object, Path<?>> initialValue() {
                return new WeakIdentityHashMap<Object, Path<?>>();
        }
    };
    
    private final ThreadLocal<Path<?>> current = new ThreadLocal<Path<?>>();
    
    public <A> A createAliasForProp(Class<A> cl, Object parent, String prop, Path<?> path){        
        A proxy = createProxy(cl);
        if (!cl.getPackage().getName().equals("java.lang")){
            bindings.get().put(proxy, path);    
        }
        return proxy;
    }
    
    public <A> A createAliasForVar(Class<A> cl, String var){        
        Path<?> path = new Path.PSimple<A>(cl, PathMetadata.forVariable(var));
        A proxy = createProxy(cl);
        bindings.get().put(proxy, path);
        return proxy;
    }
    
    @SuppressWarnings("unchecked")
    private <A> A createProxy(Class<A> cl) {
        Enhancer enhancer = new Enhancer();
        enhancer.setClassLoader(AliasFactory.class.getClassLoader());
        if (cl.isInterface()){
            enhancer.setInterfaces(new Class[]{cl});
        }else{
            enhancer.setSuperclass(cl);    
        }         
        // creates one handler per proxy
        MethodInterceptor handler = new PropertyAccessInvocationHandler(this);
        enhancer.setCallback(handler);
        A rv = (A)enhancer.create();
        return rv;
    }
    
    @SuppressWarnings("unchecked")
    public <A extends Path<?>> A getCurrent() {
        return (A) current.get();
    }

    public boolean isBound() {
        return current.get() != null;
    }
    
    public Path<?> pathForAlias(Object key){
        return bindings.get().get(key);
    }

    public void setCurrent(Path<?> path){
        current.set(path);
    }
    
}
