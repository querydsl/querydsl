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
package com.querydsl.core;

import java.util.Locale;

import javax.annotation.Nullable;

import com.querydsl.core.types.Constant;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Visitor;
import com.querydsl.core.types.expr.BooleanExpression;
import com.querydsl.core.types.expr.NumberExpression;
import com.querydsl.core.types.expr.SimpleExpression;
import com.querydsl.core.types.expr.StringExpression;

/**
 * StringConstant represents String constants
 *
 * @author tiwe
 *
 */
public final class StringConstant extends StringExpression implements Constant<String>{

    private static final long serialVersionUID = 5182804405789674556L;
    
    public static StringExpression create(String str) {
        return new StringConstant(str);
    }

    private final String constant;

    @Nullable
    private volatile NumberExpression<Integer> length;

    @Nullable
    private volatile StringExpression lower, trim, upper;

    StringConstant(String constant) {
        super(ConstantImpl.create(constant));
        this.constant = constant;
    }

    @Override
    public final <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(this, context);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public StringExpression append(Expression<String> s) {
        if (s instanceof Constant) {
            return append(((Constant<String>)s).getConstant());
        } else {
            return super.append(s);
        }
    }

    @Override
    public StringExpression append(String s) {
        return new StringConstant(constant + s);
    }

    @Override
    public SimpleExpression<Character> charAt(int i) {
        return SimpleConstant.create(constant.charAt(i));
    }

    @Override
    public StringExpression concat(String s) {
        return append(s);
    }

    @Override
    public BooleanExpression eq(String s) {
        return BooleanConstant.create(constant.equals(s));
    }

    @Override
    public BooleanExpression equalsIgnoreCase(String str) {
        return BooleanConstant.create(constant.equalsIgnoreCase(str));
    }

    @Override
    public String getConstant() {
        return constant;
    }

    @Override
    public BooleanExpression isEmpty() {
        return BooleanConstant.create(constant.isEmpty());
    }

    @Override
    public BooleanExpression isNotEmpty() {
        return BooleanConstant.create(!constant.isEmpty());
    }

    @Override
    public NumberExpression<Integer> length() {
        if (length == null) {
            length = NumberConstant.create(Integer.valueOf(constant.length()));
        }
        return length;
    }

    @Override
    public StringExpression lower() {
        if (lower == null) {
            lower = new StringConstant(constant.toLowerCase(Locale.ENGLISH));
        }
        return lower;
    }

    @Override
    public BooleanExpression matches(String pattern) {
        return BooleanConstant.create(constant.matches(pattern));
    }

    @Override
    public BooleanExpression ne(String s) {
        return BooleanConstant.create(!constant.equals(s));
    }

    @SuppressWarnings("unchecked")
    @Override
    public StringExpression prepend(Expression<String> s) {
        if (s instanceof Constant) {
            return prepend(((Constant<String>)s).getConstant());
        } else {
            return super.prepend(s);
        }
    }

    @Override
    public StringExpression prepend(String s) {
        return new StringConstant(s + constant);
    }

    @Override
    public StringExpression substring(int beginIndex) {
        return new StringConstant(constant.substring(beginIndex));
    }

    @Override
    public StringExpression substring(int beginIndex, int endIndex) {
        return new StringConstant(constant.substring(beginIndex, endIndex));
    }

    @Override
    public StringExpression toLowerCase() {
        return lower();
    }

    @Override
    public StringExpression toUpperCase() {
        return upper();
    }

    @Override
    public StringExpression trim() {
        if (trim == null) {
            trim = new StringConstant(constant.trim());
        }
        return trim;
    }

    @Override
    public StringExpression upper() {
        if (upper == null) {
            upper = new StringConstant(constant.toUpperCase(Locale.ENGLISH));
        }
        return upper;
    }
}
