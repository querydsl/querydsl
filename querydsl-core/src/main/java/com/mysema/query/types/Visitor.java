/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.map.LazyMap;

import com.mysema.query.types.alias.AEntity;
import com.mysema.query.types.alias.AEntityCollection;
import com.mysema.query.types.alias.ASimple;
import com.mysema.query.types.alias.AToPath;
import com.mysema.query.types.alias.Alias;
import com.mysema.query.types.custom.CBoolean;
import com.mysema.query.types.custom.CComparable;
import com.mysema.query.types.custom.CSimple;
import com.mysema.query.types.custom.CString;
import com.mysema.query.types.custom.Custom;
import com.mysema.query.types.expr.EConstant;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.OBoolean;
import com.mysema.query.types.operation.OComparable;
import com.mysema.query.types.operation.ONumber;
import com.mysema.query.types.operation.OSimple;
import com.mysema.query.types.operation.OString;
import com.mysema.query.types.operation.OStringArray;
import com.mysema.query.types.operation.Operation;
import com.mysema.query.types.path.PArray;
import com.mysema.query.types.path.PBoolean;
import com.mysema.query.types.path.PBooleanArray;
import com.mysema.query.types.path.PComparable;
import com.mysema.query.types.path.PComparableArray;
import com.mysema.query.types.path.PComponentCollection;
import com.mysema.query.types.path.PComponentList;
import com.mysema.query.types.path.PComponentMap;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PEntityCollection;
import com.mysema.query.types.path.PEntityList;
import com.mysema.query.types.path.PEntityMap;
import com.mysema.query.types.path.PMap;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PSimple;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PStringArray;
import com.mysema.query.types.path.Path;
import com.mysema.query.types.quant.Quant;

/**
 * Visitor provides a dispatching Visitor for Expr instances.
 * 
 * @author tiwe
 * @version $Id$
 */
public abstract class Visitor<T extends Visitor<T>> {
    
	private static final Set<Package> knownPackages = new HashSet<Package>(Arrays.asList(
			Visitor.class.getPackage(),
			Alias.class.getPackage(),
			Custom.class.getPackage(),
			Expr.class.getPackage(),
			Operation.class.getPackage(),
			Path.class.getPackage(),
			Quant.class.getPackage()));

    private final Map<Class<?>, Method> methodMap = LazyMap.decorate(new HashMap<Class<?>,Method>(), 
            new Transformer<Class<?>,Method>(){

        public Method transform(Class<?> cl) {
            try {
            	while (!knownPackages.contains(cl.getPackage())){            		
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
                throw new RuntimeException(e.getMessage(), e);
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

    protected abstract void visit(AEntity<?> expr);
    
    protected abstract void visit(AEntityCollection<?> expr);
    
    protected abstract void visit(ASimple<?> expr);

    protected abstract void visit(AToPath expr);

    protected abstract void visit(CBoolean expr);

    protected abstract void visit(CComparable<?> expr);

    protected abstract void visit(CSimple<?> expr);
    
    protected abstract void visit(CString expr);
    
    protected abstract void visit(Custom<?> expr);
    
    protected abstract void visit(EConstant<?> expr);
    
    protected abstract void visit(OBoolean expr);
    
    protected abstract void visit(OComparable<?,?> expr);

    protected abstract void visit(ONumber<?,?> expr);
    
    protected abstract void visit(Operation<?, ?> expr);
    
    protected abstract void visit(OSimple<?,?> expr);
    
    protected abstract void visit(OString expr);

    protected abstract void visit(OStringArray expr);
    
    protected abstract void visit(PArray<?> expr);

    protected abstract void visit(Path<?> expr);
    
    protected abstract void visit(PBoolean expr);
    
    protected abstract void visit(PBooleanArray expr);
    
    protected abstract void visit(PComparable<?> expr);

    protected abstract void visit(PComparableArray<?> expr);

    protected abstract void visit(PComponentCollection<?> expr);
    
    protected abstract void visit(PComponentList<?> expr);
    
    protected abstract void visit(PComponentMap<?,?> expr);
    
    protected abstract void visit(PEntity<?> expr);
    
    protected abstract void visit(PEntityCollection<?> expr);
    
    protected abstract void visit(PEntityList<?> expr);
    
    protected abstract void visit(PEntityMap<?,?> expr);
    
    protected abstract void visit(PMap<?,?> expr);
    
    protected abstract void visit(PNumber<?> expr);
    
    protected abstract void visit(PSimple<?> expr);
    
    protected abstract void visit(PString expr);
    
    protected abstract void visit(PStringArray expr);
        
}
