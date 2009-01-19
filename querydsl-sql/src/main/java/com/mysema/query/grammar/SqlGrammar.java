package com.mysema.query.grammar;

import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.SubQuery;

/**
 * SqlGrammar provides
 *
 * @author tiwe
 * @version $Id$
 */
public class SqlGrammar extends Grammar{
    
    public static <A> SubQuery<SqlJoinMeta,A> select(Expr<A> select){
        return new SubQuery<SqlJoinMeta,A>(select);
    }
    
    public static SubQuery<SqlJoinMeta,Object[]> select(Expr<?>... select){
        return new SubQuery<SqlJoinMeta,Object[]>().select(select);
    }

}
