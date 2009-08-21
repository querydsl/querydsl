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
import com.mysema.query.types.expr.EBooleanConst;
import com.mysema.query.types.expr.ENumberConst;
import com.mysema.query.types.expr.EStringConst;
import com.mysema.query.types.expr.ExprConst;
import com.mysema.query.types.operation.OBoolean;
import com.mysema.query.types.operation.OComparable;
import com.mysema.query.types.operation.ODate;
import com.mysema.query.types.operation.ODateTime;
import com.mysema.query.types.operation.ONumber;
import com.mysema.query.types.operation.OSimple;
import com.mysema.query.types.operation.OString;
import com.mysema.query.types.operation.OStringArray;
import com.mysema.query.types.operation.OTime;
import com.mysema.query.types.operation.Operation;
import com.mysema.query.types.path.PArray;
import com.mysema.query.types.path.PBoolean;
import com.mysema.query.types.path.PBooleanArray;
import com.mysema.query.types.path.PCollection;
import com.mysema.query.types.path.PComparable;
import com.mysema.query.types.path.PComparableArray;
import com.mysema.query.types.path.PComponentCollection;
import com.mysema.query.types.path.PComponentList;
import com.mysema.query.types.path.PComponentMap;
import com.mysema.query.types.path.PDate;
import com.mysema.query.types.path.PDateTime;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PEntityCollection;
import com.mysema.query.types.path.PEntityList;
import com.mysema.query.types.path.PEntityMap;
import com.mysema.query.types.path.PList;
import com.mysema.query.types.path.PMap;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PSimple;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PStringArray;
import com.mysema.query.types.path.PTime;
import com.mysema.query.types.path.Path;

/**
 * VisitorBase provides a base implementation of the Visitor class, where
 * invocations are dispatched to supertypes when available and visible.
 * 
 * @author tiwe
 * @version $Id$
 */
public abstract class VisitorBase<SubType extends VisitorBase<SubType>>
        extends Visitor<SubType> {

    @Override
    protected void visit(EStringConst expr) {
        visit((Constant<?>) expr);
    }
    
    @Override
    protected void visit(EBooleanConst expr) {
        visit((Constant<?>) expr);
    }
    
    @Override
    protected void visit(ExprConst<?> expr) {
        visit((Constant<?>) expr);
    }
    
    @Override
    protected void visit(ENumberConst<?> expr) {
        visit((Constant<?>) expr);
    }
    
    @Override
    protected void visit(CBoolean expr) {
        visit((Custom<?>) expr);
    }

    @Override
    protected void visit(CComparable<?> expr) {
        visit((Custom<?>) expr);
    }

    @Override
    protected void visit(CSimple<?> expr) {
        visit((Custom<?>) expr);
    }

    @Override
    protected void visit(CString expr) {
        visit((Custom<?>) expr);
    }

    @Override
    protected void visit(OBoolean expr) {
        visit((Operation<?, ?>) expr);
    }

    @Override
    protected void visit(OComparable<?, ?> expr) {
        visit((Operation<?, ?>) expr);
    }
    
    @Override
    protected void visit(ODate<?, ?> expr) {
        visit((Operation<?, ?>) expr);        
    }

    @Override
    protected void visit(ODateTime<?, ?> expr) {
        visit((Operation<?, ?>) expr);        
    }

    @Override
    protected void visit(OTime<?, ?> expr) {
        visit((Operation<?, ?>) expr);        
    }

    @Override
    protected void visit(ONumber<?, ?> expr) {
        visit((Operation<?, ?>) expr);
    }
    
    @Override
    protected void visit(OSimple<?, ?> expr) {
        visit((Operation<?, ?>) expr);
    }

    @Override
    protected void visit(OString expr) {
        visit((Operation<?, ?>) expr);
    }

    @Override
    protected void visit(OStringArray expr) {
        visit((Operation<?, ?>) expr);
    }

    @Override
    protected void visit(PArray<?> expr) {
        visit((Path<?>) expr);
    }

    @Override
    protected void visit(PBoolean expr) {
        visit((Path<?>) expr);
    }

    @Override
    protected void visit(PBooleanArray expr) {
        visit((PArray<?>) expr);
    }

    @Override
    protected void visit(PCollection<?> expr) {
        visit((Path<?>) expr);
    }

    @Override
    protected void visit(PComparable<?> expr) {
        visit((Path<?>) expr);
    }

    @Override
    protected void visit(PComparableArray<?> expr) {
        visit((PArray<?>) expr);
    }

    @Override
    protected void visit(PComponentCollection<?> expr) {
        visit((PCollection<?>) expr);
    }

    @Override
    protected void visit(PComponentList<?> expr) {
        visit((PList<?>) expr);
    }

    @Override
    protected void visit(PComponentMap<?, ?> expr) {
        visit((PMap<?, ?>) expr);
    }

    @Override
    protected void visit(PEntity<?> expr) {
        visit((Path<?>) expr);
    }

    @Override
    protected void visit(PEntityCollection<?> expr) {
        visit((PCollection<?>) expr);
    }

    @Override
    protected void visit(PEntityList<?> expr) {
        visit((PList<?>) expr);
    }

    @Override
    protected void visit(PEntityMap<?, ?> expr) {
        visit((PMap<?, ?>) expr);
    }

    @Override
    protected void visit(PList<?> expr) {
        visit((PCollection<?>) expr);
    }

    @Override
    protected void visit(PMap<?, ?> expr) {
        visit((Path<?>) expr);
    }

    @Override
    protected void visit(PNumber<?> expr) {
        visit((Path<?>) expr);
    }
        
    @Override
    protected void visit(PDate<?> expr) {
        visit((Path<?>) expr);
    }
    
    @Override
    protected void visit(PDateTime<?> expr) {
        visit((Path<?>) expr);
    }
    
    @Override
    protected void visit(PTime<?> expr) {
        visit((Path<?>) expr);
    }

    @Override
    protected void visit(PSimple<?> expr) {
        visit((Path<?>) expr);
    }

    @Override
    protected void visit(PString expr) {
        visit((Path<?>) expr);
    }

    @Override
    protected void visit(PStringArray expr) {
        visit((PArray<?>) expr);
    }

}
