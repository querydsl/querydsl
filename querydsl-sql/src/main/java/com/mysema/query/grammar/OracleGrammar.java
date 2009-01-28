package com.mysema.query.grammar;

import com.mysema.query.grammar.types.PathMetadata;
import com.mysema.query.grammar.types.Expr.ENumber;
import com.mysema.query.grammar.types.Expr.EString;
import com.mysema.query.grammar.types.Path.PNumber;
import com.mysema.query.grammar.types.Path.PString;

/**
 * OracleGrammar provides
 *
 * @author tiwe
 * @version $Id$
 */
public class OracleGrammar extends SqlGrammar{
    
    public static EString level = new PString(md("level"));
    
    public static ENumber<Integer> rownum = new PNumber<Integer>(Integer.class, md("rownum"));
    
    private static PathMetadata<String> md(String var){
        return PathMetadata.forVariable(var);
    }
}
