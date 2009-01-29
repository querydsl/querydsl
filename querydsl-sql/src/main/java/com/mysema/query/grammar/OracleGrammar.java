package com.mysema.query.grammar;


import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.PathMetadata;
import com.mysema.query.grammar.types.SumOver;
import com.mysema.query.grammar.types.Expr.ENumber;
import com.mysema.query.grammar.types.Path.PNumber;

/**
 * OracleGrammar provides
 *
 * @author tiwe
 * @version $Id$
 */
public class OracleGrammar extends SqlGrammar{
    
    public static ENumber<Integer> level = new PNumber<Integer>(Integer.class, md("level"));
    
    public static ENumber<Integer> rownum = new PNumber<Integer>(Integer.class, md("rownum"));
    
    private static PathMetadata<String> md(String var){
        return PathMetadata.forVariable(var);
    }

    public static <A extends Number & Comparable<A>> SumOver<A> sumOver(Expr<A> expr){
        return new SumOver<A>(expr);
    }
}
