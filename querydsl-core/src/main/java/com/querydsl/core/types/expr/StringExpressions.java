/*
 * Copyright 2012, Mysema Ltd
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
package com.querydsl.core.types.expr;

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
     * @param str
     * @return ltrim(str)
     */
    public static StringExpression ltrim(Expression<String> str) {
        return StringOperation.create(Ops.StringOps.LTRIM, str);
    }

    /**
     * @param str
     * @return rtrim(str)
     */
    public static StringExpression rtrim(Expression<String> str) {
        return StringOperation.create(Ops.StringOps.RTRIM, str);
    }
    
    /**
     * @param in
     * @param length
     * @return
     */
    public static StringExpression lpad(Expression<String> in, int length) {
        return StringOperation.create(Ops.StringOps.LPAD, in, ConstantImpl.create(length));
    }
    
    /**
     * @param in
     * @param length
     * @return
     */
    public static StringExpression lpad(Expression<String> in, Expression<Integer> length) {
        return StringOperation.create(Ops.StringOps.LPAD, in, length);
    }
    
    /**
     * @param in
     * @param length
     * @return
     */
    public static StringExpression rpad(Expression<String> in, int length) {
        return StringOperation.create(Ops.StringOps.RPAD, in, ConstantImpl.create(length));
    }
    
    /**
     * @param in
     * @param length
     * @return
     */
    public static StringExpression rpad(Expression<String> in, Expression<Integer> length) {
        return StringOperation.create(Ops.StringOps.RPAD, in, length);
    }
    
    /**
     * @param in
     * @param length
     * @param c
     * @return
     */
    public static StringExpression lpad(Expression<String> in, NumberExpression<Integer> length, char c) {
        return StringOperation.create(Ops.StringOps.LPAD2, in, length, ConstantImpl.create(c));        
    }

    /**
     * @param in
     * @param length
     * @param c
     * @return
     */
    public static StringExpression lpad(Expression<String> in, int length, char c) {
        return StringOperation.create(Ops.StringOps.LPAD2, in, ConstantImpl.create(length), ConstantImpl.create(c));        
    }

    /**
     * @param in
     * @param length
     * @param c
     * @return
     */
    public static StringExpression rpad(Expression<String> in, NumberExpression<Integer> length, char c) {
        return StringOperation.create(Ops.StringOps.RPAD2, in, length, ConstantImpl.create(c));        
    }

    /**
     * @param in
     * @param length
     * @param c
     * @return
     */
    public static StringExpression rpad(Expression<String> in, int length, char c) {
        return StringOperation.create(Ops.StringOps.RPAD2, in, ConstantImpl.create(length), ConstantImpl.create(c));        
    }
    
    private StringExpressions() {}

}
