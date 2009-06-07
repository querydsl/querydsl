/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import static com.mysema.query.alias.GrammarWithAlias.$;
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

public class CoverageTest {
    
    @Test
    public void test() throws IllegalArgumentException, IllegalAccessException{
        // make sure all Operators are covered in expression factory methods
        Set<Operator<?>> usedOperators = new HashSet<Operator<?>>();
        List<Expr<?>> exprs = new ArrayList<Expr<?>>();
        // numeric
        exprs.addAll(StandardTestData.numericProjections($(0), $(1), 1));
        exprs.addAll(StandardTestData.numericMatchingFilters($(0), $(1), 1));
        exprs.addAll(StandardTestData.numericFilters($(0), $(1), 1));        
        exprs.addAll(StandardTestData.numericCasts($(0), $(1), 1));
        // string
        exprs.addAll(StandardTestData.stringProjections($("a"), $("b"), "abc"));
        exprs.addAll(StandardTestData.stringMatchingFilters($("a"), $("b"), "abc"));        
        exprs.addAll(StandardTestData.stringFilters($("a"), $("b"), "abc"));
        // boolean
        exprs.addAll(StandardTestData.booleanFilters($(true), $(false)));                
        // collection
        exprs.addAll(StandardTestData.listProjections($(new ArrayList<String>()), $(new ArrayList<String>()), ""));
        exprs.addAll(StandardTestData.listFilters($(new ArrayList<String>()), $(new ArrayList<String>()), ""));
        // map
        exprs.addAll(StandardTestData.mapProjections($(new HashMap<String,String>()), $(new HashMap<String,String>()), "", ""));
        exprs.addAll(StandardTestData.mapFilters($(new HashMap<String,String>()), $(new HashMap<String,String>()), "", ""));
        
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
            Ops.INSTANCEOF,
            Ops.ALIAS,
            Ops.ARRAY_SIZE,
            Ops.MOD,
            Ops.STRING_CAST,
                
            // aggregation
            Ops.AVG_AGG,
            Ops.MAX_AGG,
            Ops.MIN_AGG,
            Ops.SUM_AGG,
            Ops.COUNT_AGG,
            Ops.COUNT_ALL_AGG,
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
