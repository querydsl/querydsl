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
    
    /** The test data. */
    private StandardTestData testData = new StandardTestData();
    
    /**
     * Test.
     * 
     * @throws IllegalArgumentException the illegal argument exception
     * @throws IllegalAccessException the illegal access exception
     */
    @SuppressWarnings("unchecked")
    @Test
    public void test() throws IllegalArgumentException, IllegalAccessException{        
        // make sure all Operators are covered in expression factory methods
        Set<Operator<?>> usedOperators = new HashSet<Operator<?>>();
        List<Expr<?>> exprs = new ArrayList<Expr<?>>();
        // numeric
        exprs.addAll(testData.numericProjections($(0), $(1), 1));
        exprs.addAll(testData.numericMatchingFilters($(0), $(1), 1));
        exprs.addAll(testData.numericFilters($(0), $(1), 1));        
        exprs.addAll(testData.numericCasts($(0), $(1), 1));
        // string
        exprs.addAll(testData.stringProjections($("a"), $("b"), "abc"));
        exprs.addAll(testData.stringMatchingFilters($("a"), $("b"), "abc"));        
        exprs.addAll(testData.stringFilters($("a"), $("b"), "abc"));
        // boolean
        exprs.addAll(testData.booleanFilters($(true), $(false)));                
        // collection
        exprs.addAll(testData.listProjections($(new ArrayList<String>()), $(new ArrayList<String>()), ""));
        exprs.addAll(testData.listFilters($(new ArrayList<String>()), $(new ArrayList<String>()), ""));
        // map
        exprs.addAll(testData.mapProjections($(new HashMap<String,String>()), $(new HashMap<String,String>()), "", ""));
        exprs.addAll(testData.mapFilters($(new HashMap<String,String>()), $(new HashMap<String,String>()), "", ""));
        
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
