/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar.types;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import java.util.List;

import com.mysema.query.grammar.Ops.Op;
import com.mysema.query.grammar.types.Expr.EBoolean;
import com.mysema.query.grammar.types.Expr.EComparable;
import com.mysema.query.grammar.types.Expr.ENumber;
import com.mysema.query.grammar.types.Expr.EString;



/**
 * Operation represents an operation with operator and arguments
 * 
 * @author tiwe
 * @version $Id$
 */
public interface Operation<OP,RT> {
    
    Class<? extends RT> getType();
    List<Expr<?>> getArgs();
    Op<OP> getOperator();

    public static class OBoolean extends EBoolean implements Operation<Boolean,Boolean>{
        private final List<Expr<?>> args;
        private final Op<Boolean> op;
        public OBoolean(Op<Boolean> op, Expr<?>... args){
            this.op = op;
            this.args = unmodifiableList(asList(args));
        }
        public OBoolean(Op<Boolean> op, List<Expr<?>> args){
            this.op = op;
            this.args = unmodifiableList(args);
        }
        public List<Expr<?>> getArgs() {return args;}
        public Op<Boolean> getOperator() {return op;}  
    }    

    public static class OComparable<OpType,D extends Comparable<? super D>> extends EComparable<D> implements Operation<OpType,D> {
        private final List<Expr<?>> args;
        private final Op<OpType> op;
        public OComparable(Class<D> type, Op<OpType> op, Expr<?>... args){
            super(type);
            this.op = op;
            this.args = unmodifiableList(asList(args));
        }
        public OComparable(Class<D> type, Op<OpType> op, List<Expr<?>> args){
            super(type);
            this.op = op;
            this.args = unmodifiableList(args);
        }
        
        public OComparable(Op<OpType> op, Expr<?>... args){
            this(null, op, args);
        }
        public List<Expr<?>> getArgs() {return args;}
        public Op<OpType> getOperator() {return op;}
    }

    public static class ONumber<OpType extends Number, D extends Number & Comparable<? super D>> extends ENumber<D> implements Operation<OpType,D>{
        private final List<Expr<?>> args;
        private final Op<OpType> op;
        public ONumber(Class<? extends D> type, Op<OpType> op, Expr<?>... args){
            super(type);
            this.op = op;
            this.args = unmodifiableList(asList(args));
        }
        public ONumber(Class<? extends D> type, Op<OpType> op, List<Expr<?>> args){
            super(type);
            this.op = op;
            this.args = unmodifiableList(args);
        }
        public List<Expr<?>> getArgs() {return args;}
        public Op<OpType> getOperator() {return op;}    
    }
    
    // TODO : OSimple<OP,RT> extends ESimple implements Operation<OP,RT>
    
    public static class OString extends EString implements Operation<String,String>{
        private final List<Expr<?>> args;
        private final Op<String> op;
        public OString(Op<String> op, Expr<?>... args){
            this.op = op;
            this.args = unmodifiableList(asList(args));
        }
        public OString(Op<String> op, List<Expr<?>> args){
            this.op = op;
            this.args = unmodifiableList(args);
        }
        public List<Expr<?>> getArgs() {return args;}
        public Op<String> getOperator() {return op;}    
    }
    
    public static class OStringArray extends Expr<String[]> implements Operation<String,String[]>{
        private final List<Expr<?>> args;
        private final Op<String> op;
        public OStringArray(Op<String> op, Expr<?>... args){
            super(null);
            this.op = op;
            this.args = unmodifiableList(asList(args));
        }
        public OStringArray(Op<String> op, List<Expr<?>> args){
            super(null);
            this.op = op;
            this.args = unmodifiableList(args);
        }
        public List<Expr<?>> getArgs() {return args;}
        public Op<String> getOperator() {return op;}
    }
    
}
