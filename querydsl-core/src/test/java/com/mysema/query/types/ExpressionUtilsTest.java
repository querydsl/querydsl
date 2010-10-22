package com.mysema.query.types;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mysema.query.types.path.StringPath;

public class ExpressionUtilsTest {

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

}
