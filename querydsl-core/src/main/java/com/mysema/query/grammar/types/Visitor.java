/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar.types;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.map.LazyMap;

/**
 * Visitor provides a dispatching Visitor for Expr instances
 * 
 * @author tiwe
 * @version $Id$
 */
public abstract class Visitor<T extends Visitor<T>> {
    
    private static final Package PACKAGE = Visitor.class.getPackage();

    private final Map<Class<?>, Method> methodMap = LazyMap.decorate(new HashMap<Class<?>,Method>(), 
            new Transformer<Class<?>,Method>(){

        public Method transform(Class<?> cl) {
            try {
                while (!cl.getPackage().equals(PACKAGE)){
                    cl = cl.getSuperclass();
                }                     
                Method method = null;
                Class<?> sigClass = Visitor.this.getClass();
                while (method == null && !sigClass.equals(Visitor.class)) {
                    try {
                        method = sigClass.getDeclaredMethod("visit", cl);
                    } catch (NoSuchMethodException nsme) {
                        sigClass = sigClass.getSuperclass();
                    }
                }
                if (method != null) {
                    method.setAccessible(true);
                } else {
                    throw new IllegalArgumentException("No method found for " + cl.getName());
                }
                return method;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    });

    @SuppressWarnings("unchecked")
    public final T handle(Expr<?> expr) {
        assert expr != null;
        try {
            methodMap.get(expr.getClass()).invoke(this, expr);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return (T) this;
    }

    protected abstract void visit(Alias.Entity<?> expr);
    
    protected abstract void visit(Alias.EntityCollection<?> expr);
    
    protected abstract void visit(Alias.NoEntity<?> expr);

    protected abstract void visit(Alias.Simple expr);

    protected abstract void visit(Alias.ToPath expr);

    protected abstract void visit(Expr.Constant<?> expr);

    protected abstract void visit(Operation<?, ?> expr);

    protected abstract void visit(Operation.Boolean expr);
    
    protected abstract void visit(Operation.Comparable<?,?> expr);
    
    protected abstract void visit(Operation.Number<?,?> expr);
    
    protected abstract void visit(Operation.String expr);

    protected abstract void visit(Path<?> expr);

    protected abstract void visit(Path.Boolean expr);

    protected abstract void visit(Path.Comparable<?> expr);
    
    protected abstract void visit(Path.ComponentCollection<?> expr);
    
    protected abstract void visit(Path.ComponentMap<?,?> expr);

    protected abstract void visit(Path.Entity<?> expr);

    protected abstract void visit(Path.EntityCollection<?> expr);
    
    protected abstract void visit(Path.EntityMap<?,?> expr);

    protected abstract void visit(Path.EntityRenamable<?> expr);
    
    protected abstract void visit(Path.NoEntity<?> expr);
    
    protected abstract void visit(Path.NoEntitySimple<?> expr);

    protected abstract void visit(Path.String expr);
    
}
