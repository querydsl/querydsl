package com.mysema.query.types;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import com.mysema.query.types.path.StringPath;

public class ExpressionUtilsTest {

    private static final StringPath str = new StringPath("str");
    
    private static final StringPath str2 = new StringPath("str2");
    
    @Test
    public void LikeToRegex() {
        assertEquals(".*", regex(ConstantImpl.create("%")));
        assertEquals(".",  regex(ConstantImpl.create("_")));
        
        StringPath path = new StringPath("path");
        assertEquals("path + .*", regex(path.append("%")));
        assertEquals(".* + path", regex(path.prepend("%")));
        assertEquals("path + .", regex(path.append("_")));
        assertEquals(". + path", regex(path.prepend("_")));
    }
    
    @Test
    public void RegexToLike() {
        assertEquals("%", like(ConstantImpl.create(".*")));
        assertEquals("_",  like(ConstantImpl.create(".")));
        
        StringPath path = new StringPath("path");
        assertEquals("path + %", like(path.append(".*")));
        assertEquals("% + path", like(path.prepend(".*")));
        assertEquals("path + _", like(path.append(".")));
        assertEquals("_ + path", like(path.prepend(".")));
    }
    
    private String regex(Expression<String> expr){
        return ExpressionUtils.likeToRegex(expr).toString();
    }
    
    private String like(Expression<String> expr){
        return ExpressionUtils.regexToLike(expr).toString();
    }
    
    @Test
    public void Count() {
        assertEquals("count(str)", ExpressionUtils.count(str).toString());
    }
    
    @Test
    public void EqConst() {
        assertEquals("str = X", ExpressionUtils.eqConst(str, "X").toString());
    }
    
    @Test
    public void Eq() {
        assertEquals("str = str2", ExpressionUtils.eq(str, str2).toString());    
    }
    
    @Test
    public void In() {
        assertEquals("str in [a, b, c]", ExpressionUtils.in(str, Arrays.asList("a","b","c")).toString());
    }
    
    @Test
    public void IsNull() {
        assertEquals("str is null", ExpressionUtils.isNull(str).toString());
    }
    
    @Test
    public void IsNotNull() {
        assertEquals("str is not null", ExpressionUtils.isNotNull(str).toString());
    }
    
    @Test
    public void NeConst() {
        assertEquals("str != X", ExpressionUtils.neConst(str, "X").toString());
    }
    
    @Test
    public void Ne() {
        assertEquals("str != str2", ExpressionUtils.ne(str, str2).toString());
    }

}
