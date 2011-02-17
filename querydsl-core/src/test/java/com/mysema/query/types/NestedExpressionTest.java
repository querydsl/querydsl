package com.mysema.query.types;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import com.mysema.query.types.path.StringPath;

public class NestedExpressionTest {

    StringPath str1 = new StringPath("str1");
    StringPath str2 = new StringPath("str2");
    StringPath str3 = new StringPath("str3");
    StringPath str4 = new StringPath("str3");

    Concatenation concat1 = new Concatenation(new Concatenation(str1, str2), str3);
    Concatenation concat2 = new Concatenation(new Concatenation(str1, new Concatenation(str2, str3)), str4);

    @Test
    public void Wrapped_Projection_Has_Right_Arguments(){
        FactoryExpression<String> wrapped = FactoryExpressionUtils.wrap(concat1);
        assertEquals(Arrays.asList(str1, str2, str3), wrapped.getArgs());
    }

    @Test
    public void Wrapped_Projection_Compresses_Projection(){
        FactoryExpression<String> wrapped = FactoryExpressionUtils.wrap(concat1);
        assertEquals("123", wrapped.newInstance("1","2","3"));
    }

    @Test
    public void Deeply_Wrapped_Projection_Has_Right_Arguments(){
        FactoryExpression<String> wrapped = FactoryExpressionUtils.wrap(concat2);
        assertEquals(Arrays.asList(str1, str2, str3, str4), wrapped.getArgs());
    }

    @Test
    public void Deeply_Wrapped_Projection_Compresses_Projection(){
        FactoryExpression<String> wrapped = FactoryExpressionUtils.wrap(concat2);
        assertEquals("1234", wrapped.newInstance("1","2","3","4"));
    }

}
