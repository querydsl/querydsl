package com.mysema.query.grammar.types;

import org.joda.time.LocalDate;
import org.junit.Test;

import com.mysema.query.grammar.GrammarWithAlias;
import com.mysema.query.grammar.types.Expr.EComparable;

/**
 * JodaTimeTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class JodaTimeTest {
    
    @Test
    public void test(){
        EComparable<LocalDate> expr = GrammarWithAlias.$(new LocalDate());
        expr.after(expr);
        expr.after(new LocalDate());
        expr.before(expr);
        expr.before(new LocalDate());
    }

}

