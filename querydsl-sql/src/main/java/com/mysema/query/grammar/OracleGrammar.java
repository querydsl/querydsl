/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;


import java.util.Date;

import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.PathMetadata;
import com.mysema.query.grammar.types.SumOver;
import com.mysema.query.grammar.types.Expr.EComparable;
import com.mysema.query.grammar.types.Expr.ENumber;
import com.mysema.query.grammar.types.Path.PComparable;
import com.mysema.query.grammar.types.Path.PNumber;

/**
 * OracleGrammar provides Oracle specific extensions to the SqlGrammar
 *
 * @author tiwe
 * @version $Id$
 */
public class OracleGrammar extends SqlGrammar{
    
    // global columns
    
    public static ENumber<Integer> level = new PNumber<Integer>(Integer.class, md("level"));
    
    public static ENumber<Integer> rownum = new PNumber<Integer>(Integer.class, md("rownum"));
    
    public static EComparable<Date> sysdate = new PComparable<Date>(Date.class, md("sysdate"));
    
    // custom functions
    
    public static <A extends Number & Comparable<? super A>> SumOver<A> sumOver(Expr<A> expr){
        return new SumOver<A>(expr);
    }
    
    // internal
    
    private static PathMetadata<String> md(String var){
        return PathMetadata.forVariable(var);
    }

    
}
