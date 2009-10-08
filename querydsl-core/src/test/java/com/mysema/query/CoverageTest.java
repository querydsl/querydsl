/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import static com.mysema.query.alias.Alias.$;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.Operation;
import com.mysema.query.types.operation.Operator;
import com.mysema.query.types.operation.Ops;

/**
 * The Class CoverageTest.
 */
public class CoverageTest {

    private MatchingFilters matchers = new MatchingFilters(Module.COLLECTIONS, Target.MEM);
    
    private Projections projections = new Projections(Module.COLLECTIONS, Target.MEM);
    
    private Filters filters = new Filters(projections, Module.COLLECTIONS, Target.MEM);
    
    @SuppressWarnings("unchecked")
    @Test
    public void test() throws IllegalArgumentException, IllegalAccessException{        
        // make sure all Operators are covered in expression factory methods
        Set<Operator<?>> usedOperators = new HashSet<Operator<?>>();
        List<Expr<?>> exprs = new ArrayList<Expr<?>>();
        // numeric
        exprs.addAll(projections.numeric($(0), $(1), 1));
        exprs.addAll(matchers.numeric($(0), $(1), 1));
        exprs.addAll(filters.numeric($(0), $(1), 1));        
        exprs.addAll(projections.numericCasts($(0), $(1), 1));
        // string
        exprs.addAll(projections.string($("a"), $("b"), "abc"));
        exprs.addAll(matchers.string($("a"), $("b"), "abc"));        
        exprs.addAll(filters.string($("a"), $("b"), "abc"));
        // boolean
        exprs.addAll(filters.booleanFilters($(true), $(false)));                
        // collection
        exprs.addAll(projections.list($(new ArrayList<String>()), $(new ArrayList<String>()), ""));
        exprs.addAll(filters.list($(new ArrayList<String>()), $(new ArrayList<String>()), ""));
        // map
        exprs.addAll(projections.map($(new HashMap<String,String>()), $(new HashMap<String,String>()), "", ""));
        exprs.addAll(filters.map($(new HashMap<String,String>()), $(new HashMap<String,String>()), "", ""));
        
        for (Expr<?> e : exprs){
            if (e instanceof Operation){
                Operation<?,?> op = (Operation<?,?>)e;
                if (op.getArg(0) instanceof Operation){
                    usedOperators.add(((Operation<?,?>)op.getArg(0)).getOperator());
                }else if (op.getArgs().size() > 1 && op.getArg(1) instanceof Operation){
                    usedOperators.add(((Operation<?,?>)op.getArg(1)).getOperator());
                }                
                usedOperators.add(op.getOperator());
            }
                
        }
        
        // missing mappings
        usedOperators.addAll(Arrays.<Operator<?>>asList(
            Ops.INSTANCE_OF,
            Ops.ALIAS,
            Ops.ARRAY_SIZE,
            Ops.MOD,
            Ops.STRING_CAST,
            
            Ops.XOR,
            Ops.XNOR,
                
            // aggregation
            Ops.AggOps.AVG_AGG,
            Ops.AggOps.MAX_AGG,
            Ops.AggOps.MIN_AGG,
            Ops.AggOps.SUM_AGG,
            Ops.AggOps.COUNT_AGG,
            Ops.AggOps.COUNT_ALL_AGG,
            Ops.EXISTS
         ));        
             
        
        List<Operator<?>> notContained = new ArrayList<Operator<?>>();
        for (Field field : Ops.class.getFields()){
            if (Operator.class.isAssignableFrom(field.getType())){
                Operator<?> val = (Operator<?>) field.get(null);
                if (!usedOperators.contains(val)){
                    System.err.println(field.getName() + " was not contained");
                    notContained.add(val);    
                }
            }
        }
                
        assertTrue(notContained.size() + " errors in processing, see log for details", notContained.isEmpty());        
    }

}
