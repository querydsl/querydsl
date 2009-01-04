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
 * Visitor provides a dispatching Visitor for Expr instances.
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

    protected abstract void visit(Alias.AEntity<?> expr);
    
    protected abstract void visit(Alias.AEntityCollection<?> expr);
    
    protected abstract void visit(Alias.ASimple<?> expr);

    protected abstract void visit(Alias.AToPath expr);

    protected abstract void visit(Expr.EConstant<?> expr);

    protected abstract void visit(Operation.OBoolean expr);

    protected abstract void visit(Operation.OComparable<?,?> expr);
    
    protected abstract void visit(Operation.ONumber<?,?> expr);
    
    protected abstract void visit(Operation.OString expr);
    
    protected abstract void visit(Operation.OStringArray expr);
    
    protected abstract void visit(Operation<?, ?> expr);

    protected abstract void visit(Path.PArray<?> expr);
    
    protected abstract void visit(Path.PBoolean expr);
    
    protected abstract void visit(Path.PBooleanArray expr);

    protected abstract void visit(Path.PComparable<?> expr);
    
    protected abstract void visit(Path.PComparableArray<?> expr);

    protected abstract void visit(Path.PComponentCollection<?> expr);
    
    protected abstract void visit(Path.PComponentList<?> expr);
    
    protected abstract void visit(Path.PComponentMap<?,?> expr);
    
    protected abstract void visit(Path.PEntity<?> expr);

    protected abstract void visit(Path.PEntityCollection<?> expr);

    protected abstract void visit(Path.PEntityList<?> expr);
    
    protected abstract void visit(Path.PEntityMap<?,?> expr);
    
    protected abstract void visit(Path.PMap<?,?> expr);
    
    protected abstract void visit(Path.PSimple<?> expr);
    
    protected abstract void visit(Path.PString expr);
    
    protected abstract void visit(Path.PStringArray expr);
    
    protected abstract void visit(Path<?> expr);
    
}
