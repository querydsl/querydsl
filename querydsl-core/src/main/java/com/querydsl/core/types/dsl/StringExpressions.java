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
package com.querydsl.core.types.dsl;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Ops;

/**
 * Extended String expressions, supported by the SQL module
 * 
 * @author tiwe
 *
 */
public final class StringExpressions {

    /**
     * Create a {@code ltrim(str)} expression
     *
     * <p>Returns a character expression after it removes leading blanks.</p>
     *
     * @param str string
     * @return ltrim(str)
     */
    public static StringExpression ltrim(Expression<String> str) {
        return Expressions.stringOperation(Ops.StringOps.LTRIM, str);
    }

    /**
     * Create a {@code rtrim(str)} expression
     *
     * <p>Returns a character string after truncating all trailing blanks.</p>
     *
     * @param str string
     * @return rtrim(str)
     */
    public static StringExpression rtrim(Expression<String> str) {
        return Expressions.stringOperation(Ops.StringOps.RTRIM, str);
    }
    
    /**
     * Create a {@code lpad(in, length)} expression
     *
     * <p>Returns in left-padded to length characters</p>
     *
     * @param in string to be padded
     * @param length target length
     * @return lpad(in, length)
     */
    public static StringExpression lpad(Expression<String> in, int length) {
        return Expressions.stringOperation(Ops.StringOps.LPAD, in, ConstantImpl.create(length));
    }
    
    /**
     * Create a {@code lpad(in, length)} expression
     *
     * <p>Returns in left-padded to length characters</p>
     *
     * @param in string to be padded
     * @param length target length
     * @return lpad(in, length)
     */
    public static StringExpression lpad(Expression<String> in, Expression<Integer> length) {
        return Expressions.stringOperation(Ops.StringOps.LPAD, in, length);
    }
    
    /**
     * Create a {@code rpad(in, length)} expression
     *
     * <p>Returns in right-padded to length characters</p>
     *
     * @param in string to be padded
     * @param length target length
     * @return rpad(in, length)
     */
    public static StringExpression rpad(Expression<String> in, int length) {
        return Expressions.stringOperation(Ops.StringOps.RPAD, in, ConstantImpl.create(length));
    }
    
    /**
     * Create a {@code rpad(in, length)} expression
     *
     * <p>Returns in right-padded to length characters</p>
     *
     * @param in string to be padded
     * @param length target length
     * @return rpad(in, length)
     */
    public static StringExpression rpad(Expression<String> in, Expression<Integer> length) {
        return Expressions.stringOperation(Ops.StringOps.RPAD, in, length);
    }
    
    /**
     * Create a {@code lpad(in, length, c)} expression
     *
     * <p>Returns in left-padded to length characters with c</p>
     *
     * @param in string to be padded
     * @param length target length
     * @param c padding char
     * @return lpad(in, length, c)
     */
    public static StringExpression lpad(Expression<String> in, NumberExpression<Integer> length, char c) {
        return Expressions.stringOperation(Ops.StringOps.LPAD2, in, length, ConstantImpl.create(c));
    }

    /**
     * Create a {@code lpad(in, length, c)} expression
     *
     * <p>Returns in left-padded to length characters with c</p>
     *
     * @param in string to be padded
     * @param length target length
     * @param c padding char
     * @return lpad(in, length, c)
     */
    public static StringExpression lpad(Expression<String> in, int length, char c) {
        return Expressions.stringOperation(Ops.StringOps.LPAD2, in, ConstantImpl.create(length), ConstantImpl.create(c));
    }

    /**
     * Create a {@code rpad(in, length, c)} expression
     *
     * <p>Returns in right-padded to length characters with c</p>
     *
     * @param in string to be padded
     * @param length target length
     * @param c padding char
     * @return rpad(in, length, c)
     */
    public static StringExpression rpad(Expression<String> in, NumberExpression<Integer> length, char c) {
        return Expressions.stringOperation(Ops.StringOps.RPAD2, in, length, ConstantImpl.create(c));
    }

    /**
     * Create a {@code rpad(in, length, c)} expression
     *
     * <p>Returns in right-padded to length characters with c</p>
     *
     * @param in string to be padded
     * @param length target length
     * @param c padding char
     * @return rpad(in, length, c)
     */
    public static StringExpression rpad(Expression<String> in, int length, char c) {
        return Expressions.stringOperation(Ops.StringOps.RPAD2, in, ConstantImpl.create(length), ConstantImpl.create(c));
    }
    
    private StringExpressions() {}

}
