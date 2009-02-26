package com.mysema.query.grammar;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.mysema.query.collections.Domain.QCat;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Expr.EBoolean;


/**
 * FilteredJavaSerializerTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class FilteredJavaSerializerTest {
    
    private static JavaOps ops = new JavaOps();
    
    private static QCat cat = new QCat("cat");    
    private static QCat otherCat = new QCat("otherCat");
    
    @Test
    @Ignore
    public void testSerialization(){
        assertMatches("cat.getName().equals(a1) && true",       cat.name.eq("Test").and(otherCat.isnull()));
        assertMatches("cat.getName().equals(a1) && !(false)",    cat.name.eq("Test").and(otherCat.isnull().not()));
        assertMatches("!(cat.getName().equals(a1)) && !(true)", cat.name.eq("Test").not().and(otherCat.isnull().not()));
        assertMatches("cat.getName().equals(a1) && !(false)",    cat.name.eq("Test").and(otherCat.isnull().not()));
    }

    private void assertMatches(String expected, EBoolean where) {
        JavaSerializer ser = new FilteredJavaSerializer(ops, Collections.<Expr<?>>singletonList(cat));
        ser.handle(where);
        Assert.assertEquals(expected, ser.toString());
    }

}
