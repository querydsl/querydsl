/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.core.types;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.ImmutableSet;

import org.junit.Ignore;
import org.junit.Test;

import com.querydsl.core.QueryException;
import com.querydsl.core.types.path.StringPath;

public class ExpressionUtilsTest {

    private static final StringPath str = new StringPath("str");
    
    private static final StringPath str2 = new StringPath("str2");
    
    @Test
    public void LikeToRegex() {
        assertEquals(".*", regex(ConstantImpl.create("%")));
        assertEquals("^abc.*", regex(ConstantImpl.create("abc%")));
        assertEquals(".*abc$", regex(ConstantImpl.create("%abc")));
        assertEquals("^.$",  regex(ConstantImpl.create("_")));
        
        StringPath path = new StringPath("path");
        assertEquals("path + .*", regex(path.append("%")));
        assertEquals(".* + path", regex(path.prepend("%")));
        assertEquals("path + .", regex(path.append("_")));
        assertEquals(". + path", regex(path.prepend("_")));               
    }
    
    @Test
    @Ignore
    public void LikeToRegexSpeed() {
        // 4570
        StringPath path = new StringPath("path");
        final int iterations = 1000000;
        long start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            regex(ConstantImpl.create("%"));
            regex(ConstantImpl.create("abc%"));
            regex(ConstantImpl.create("%abc"));
            regex(ConstantImpl.create("_"));
            regex(path.append("%"));
            regex(path.prepend("%"));
            regex(path.append("_"));
            regex(path.prepend("_"));    
        }
        long duration = System.currentTimeMillis() - start;
        System.err.println(duration);
    }
    
    @Test
    public void LikeToRegex_Escape() {        
        assertEquals("^\\.$",  regex(ConstantImpl.create(".")));
    }
    
    @Test
    public void RegexToLike() {
        assertEquals("%", like(ConstantImpl.create(".*")));
        assertEquals("_",  like(ConstantImpl.create(".")));
        assertEquals(".", like(ConstantImpl.create("\\.")));
        
        StringPath path = new StringPath("path");
        assertEquals("path + %", like(path.append(".*")));
        assertEquals("% + path", like(path.prepend(".*")));
        assertEquals("path + _", like(path.append(".")));
        assertEquals("_ + path", like(path.prepend(".")));
    }
    
    @Test(expected=QueryException.class)
    public void RegexToLike_Fail() {
        like(ConstantImpl.create("a*"));
    }
    
    @Test(expected=QueryException.class)
    public void RegexToLike_Fail2() {
        like(ConstantImpl.create("\\d"));
    }
    
    @Test(expected=QueryException.class)
    public void RegexToLike_Fail3() {
        like(ConstantImpl.create("[ab]"));
    }
    
    @Test
    @Ignore
    public void RegexToLikeSpeed() {
        // 3255
        StringPath path = new StringPath("path");        
        final int iterations = 1000000;
        long start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            like(ConstantImpl.create(".*"));
            like(ConstantImpl.create("."));                
            like(path.append(".*"));
            like(path.prepend(".*"));
            like(path.append("."));
            like(path.prepend("."));    
        }
        long duration = System.currentTimeMillis() - start;
        System.err.println(duration);
    }
    
    private String regex(Expression<String> expr) {
        return ExpressionUtils.likeToRegex(expr).toString();
    }
    
    private String like(Expression<String> expr) {
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
    public void InAny() {
        ImmutableSet<List<String>> of = ImmutableSet.of(Arrays.asList("a", "b", "c"), Arrays.asList("d", "e", "f"));
        assertEquals("str in [a, b, c] || str in [d, e, f]",
                ExpressionUtils.inAny(str, of).toString());
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

    @Test
    public void NotInAny() {
        ImmutableSet<List<String>> of = ImmutableSet.of(Arrays.asList("a", "b", "c"), Arrays.asList("d", "e", "f"));
        assertEquals("str not in [a, b, c] && str not in [d, e, f]",
                ExpressionUtils.notInAny(str, of).toString());
    }

}
