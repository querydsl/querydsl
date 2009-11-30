/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;

import com.mysema.query.types.custom.CBoolean;
import com.mysema.query.types.custom.CComparable;
import com.mysema.query.types.custom.CSimple;
import com.mysema.query.types.custom.CString;
import com.mysema.query.types.custom.Custom;
import com.mysema.query.types.expr.Constant;
import com.mysema.query.types.expr.EArrayConstructor;
import com.mysema.query.types.expr.EBooleanConst;
import com.mysema.query.types.expr.EConstructor;
import com.mysema.query.types.expr.EDateConst;
import com.mysema.query.types.expr.EDateTimeConst;
import com.mysema.query.types.expr.ENumberConst;
import com.mysema.query.types.expr.EStringConst;
import com.mysema.query.types.expr.ETimeConst;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.expr.ExprConst;
import com.mysema.query.types.operation.OBoolean;
import com.mysema.query.types.operation.OComparable;
import com.mysema.query.types.operation.ODate;
import com.mysema.query.types.operation.ODateTime;
import com.mysema.query.types.operation.ONumber;
import com.mysema.query.types.operation.OSimple;
import com.mysema.query.types.operation.OString;
import com.mysema.query.types.operation.OTime;
import com.mysema.query.types.operation.Operation;
import com.mysema.query.types.path.PArray;
import com.mysema.query.types.path.PBoolean;
import com.mysema.query.types.path.PCollection;
import com.mysema.query.types.path.PComparable;
import com.mysema.query.types.path.PDate;
import com.mysema.query.types.path.PDateTime;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PList;
import com.mysema.query.types.path.PMap;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PSimple;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PTime;
import com.mysema.query.types.path.Path;
import com.mysema.query.types.query.ListSubQuery;
import com.mysema.query.types.query.ObjectSubQuery;
import com.mysema.query.types.query.SubQuery;

/**
 * VisitorBase provides a base implementation of the Visitor class, where
 * invocations are dispatched to supertypes 
 * 
 * @author tiwe
 * @version $Id$
 */
public abstract class VisitorBase<SubType extends VisitorBase<SubType>> implements Visitor {
    
    @SuppressWarnings("unchecked")
    public final SubType handle(Expr<?> expr) {
        expr.accept(this);
        return (SubType)this;
    }
    
    @Override
    public void visit(CBoolean expr) {
        visit((Custom<?>) expr);
    }
    
    @Override
    public void visit(EArrayConstructor<?> expr){
        visit((EConstructor<?>)expr);
    }
    
    @Override
    public void visit(CComparable<?> expr) {
        visit((Custom<?>) expr);
    }
    
    @Override
    public void visit(CSimple<?> expr) {
        visit((Custom<?>) expr);
    }
    
    @Override
    public void visit(CString expr) {
        visit((Custom<?>) expr);
    }
    
    @Override
    public void visit(EBooleanConst expr) {
        visit((Constant<?>) expr);
    }
    
    @Override
    public void visit(EDateConst expr) {
        visit((Constant<?>) expr);
    }
    
    @Override
    public void visit(EDateTimeConst expr) {
        visit((Constant<?>) expr);
    }
    
    @Override
    public void visit(ETimeConst expr) {
        visit((Constant<?>) expr);
    }
    
    @Override
    public void visit(ENumberConst<?> expr) {
        visit((Constant<?>) expr);
    }
    
    @Override
    public void visit(EStringConst expr) {
        visit((Constant<?>) expr);
    }

    @Override
    public void visit(ExprConst<?> expr) {
        visit((Constant<?>) expr);
    }

    @Override
    public void visit(ListSubQuery<?> query) {
        visit((SubQuery)query);
    }

    @Override
    public void visit(ObjectSubQuery<?> query) {
        visit((SubQuery)query);
    }

    @Override
    public void visit(OBoolean expr) {
        visit((Operation<?, ?>) expr);
    }

    @Override
    public void visit(OComparable<?, ?> expr) {
        visit((Operation<?, ?>) expr);
    }
    
    @Override
    public void visit(ODate<?, ?> expr) {
        visit((Operation<?, ?>) expr);        
    }

    @Override
    public void visit(ODateTime<?, ?> expr) {
        visit((Operation<?, ?>) expr);        
    }

    @Override
    public void visit(ONumber<?, ?> expr) {
        visit((Operation<?, ?>) expr);
    }

    @Override
    public void visit(OSimple<?, ?> expr) {
        visit((Operation<?, ?>) expr);
    }
    
    @Override
    public void visit(OString expr) {
        visit((Operation<?, ?>) expr);
    }

    @Override
    public void visit(OTime<?, ?> expr) {
        visit((Operation<?, ?>) expr);        
    }

    @Override
    public void visit(PArray<?> expr) {
        visit((Path<?>) expr);
    }

    @Override
    public void visit(PBoolean expr) {
        visit((Path<?>) expr);
    }

    @Override
    public void visit(PComparable<?> expr) {
        visit((Path<?>) expr);
    }

    @Override
    public void visit(PDate<?> expr) {
        visit((Path<?>) expr);
    }

    @Override
    public void visit(PDateTime<?> expr) {
        visit((Path<?>) expr);
    }

    @Override
    public void visit(PEntity<?> expr) {
        visit((Path<?>) expr);
    }

    @Override
    public void visit(PCollection<?> expr) {
        visit((Path<?>) expr);
    }

    @Override
    public void visit(PList<?,?> expr) {
        visit((Path<?>) expr);
    }

    @Override
    public void visit(PMap<?, ?, ?> expr) {
        visit((Path<?>) expr);
    }
    
    @Override
    public void visit(PNumber<?> expr) {
        visit((Path<?>) expr);
    }
    
    @Override
    public void visit(PSimple<?> expr) {
        visit((Path<?>) expr);
    }

    @Override
    public void visit(PString expr) {
        visit((Path<?>) expr);
    }

    @Override
    public void visit(PTime<?> expr) {
        visit((Path<?>) expr);
    }

}
