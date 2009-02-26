package com.mysema.query.grammar;

import java.util.Collections;

import org.junit.Test;

import com.mysema.query.collections.Domain.QCat;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Expr.EBoolean;
import com.mysema.query.serialization.OperationPatterns;


/**
 * FilteredJavaSerializerTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class FilteredJavaSerializerTest {
    
    private static OperationPatterns ops = new JavaOps();
    
    private static QCat cat = new QCat("cat");    
    private static QCat otherCat = new QCat("otherCat");
    
    @Test
    public void testSerialization(){
        test(cat.name.eq("Test").and(otherCat.isnull()));
    }

    private void test(EBoolean where) {
        JavaSerializer ser = new FilteredJavaSerializer(ops, Collections.<Expr<?>>singletonList(cat));
        ser.handle(where);
    }

}
