/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.QueryException;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;

public class ExpressionUtilsTest {

    private static final StringPath str = Expressions.stringPath("str");

    private static final StringPath str2 = Expressions.stringPath("str2");

    @Test
    public void likeToRegex() {
        assertEquals(".*", regex(ConstantImpl.create("%")));
        assertEquals("^abc.*", regex(ConstantImpl.create("abc%")));
        assertEquals(".*abc$", regex(ConstantImpl.create("%abc")));
        assertEquals("^.$",  regex(ConstantImpl.create("_")));

        StringPath path = Expressions.stringPath("path");
        assertEquals("path + .*", regex(path.append("%")));
        assertEquals(".* + path", regex(path.prepend("%")));
        assertEquals("path + .", regex(path.append("_")));
        assertEquals(". + path", regex(path.prepend("_")));
    }

    @Test
    @Ignore
    public void likeToRegexSpeed() {
        // 4570
        StringPath path = Expressions.stringPath("path");
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
    public void likeToRegex_escape() {
        assertEquals("^\\.$",  regex(ConstantImpl.create(".")));
    }

    @Test
    public void regexToLike() {
        assertEquals("%", like(ConstantImpl.create(".*")));
        assertEquals("_",  like(ConstantImpl.create(".")));
        assertEquals(".", like(ConstantImpl.create("\\.")));

        StringPath path = Expressions.stringPath("path");
        assertEquals("path + %", like(path.append(".*")));
        assertEquals("% + path", like(path.prepend(".*")));
        assertEquals("path + _", like(path.append(".")));
        assertEquals("_ + path", like(path.prepend(".")));
    }

    @Test(expected = QueryException.class)
    public void regexToLike_fail() {
        like(ConstantImpl.create("a*"));
    }

    @Test(expected = QueryException.class)
    public void regexToLike_fail2() {
        like(ConstantImpl.create("\\d"));
    }

    @Test(expected = QueryException.class)
    public void regexToLike_fail3() {
        like(ConstantImpl.create("[ab]"));
    }

    @Test
    @Ignore
    public void regexToLikeSpeed() {
        // 3255
        StringPath path = Expressions.stringPath("path");
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
    public void count() {
        assertEquals("count(str)", ExpressionUtils.count(str).toString());
    }

    @Test
    public void eqConst() {
        assertEquals("str = X", ExpressionUtils.eqConst(str, "X").toString());
    }

    @Test
    public void eq() {
        assertEquals("str = str2", ExpressionUtils.eq(str, str2).toString());
    }

    @Test
    public void in() {
        assertEquals("str in [a, b, c]", ExpressionUtils.in(str, Arrays.asList("a","b","c")).toString());
    }

    @Test
    public void in_subQuery() {
        String s = ExpressionUtils.in(str, new SubQueryExpressionImpl<String>(String.class, new DefaultQueryMetadata())).toString();
        assertTrue(s.startsWith("str in com.querydsl.core.DefaultQueryMetadata@c"));
    }

    @Test
    public void inAny() {
        Collection<List<String>> of = Arrays.asList(Arrays.asList("a", "b", "c"), Arrays.asList("d", "e", "f"));
        assertEquals("str in [a, b, c] || str in [d, e, f]",
                ExpressionUtils.inAny(str, of).toString());
    }

    @Test
    public void isNull() {
        assertEquals("str is null", ExpressionUtils.isNull(str).toString());
    }

    @Test
    public void isNotNull() {
        assertEquals("str is not null", ExpressionUtils.isNotNull(str).toString());
    }

    @Test
    public void neConst() {
        assertEquals("str != X", ExpressionUtils.neConst(str, "X").toString());
    }

    @Test
    public void ne() {
        assertEquals("str != str2", ExpressionUtils.ne(str, str2).toString());
    }

    @Test
    public void notInAny() {
        Collection<List<String>> of = Arrays.asList(Arrays.asList("a", "b", "c"), Arrays.asList("d", "e", "f"));
        assertEquals("str not in [a, b, c] && str not in [d, e, f]",
                ExpressionUtils.notInAny(str, of).toString());
    }

    @Test
    public void notIn_subQuery() {
        String s = ExpressionUtils.notIn(str, new SubQueryExpressionImpl<String>(String.class, new DefaultQueryMetadata())).toString();
        assertTrue(s.startsWith("str not in com.querydsl.core.DefaultQueryMetadata@c"));
    }

}
