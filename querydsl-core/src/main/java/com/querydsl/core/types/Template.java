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

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.annotation.concurrent.Immutable;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.util.MathUtils;

/**
 * {@code Template} provides serialization templates for {@link Operation},
 * {@link TemplateExpression} and {@link Path} serialization
 *
 * @author tiwe
 *
 */
@Immutable
public final class Template implements Serializable {

    private static final long serialVersionUID = -1697705745769542204L;

    /**
     * General template element
     */
    @Immutable
    public abstract static class Element implements Serializable {

        private static final long serialVersionUID = 3396877288101929387L;

        public abstract Object convert(List<?> args);

        public abstract boolean isString();

    }

    /**
     * Expression as string
     */
    public static final class AsString extends Element {

        private static final long serialVersionUID = -655362047873616197L;

        private final int index;

        private final String toString;

        public AsString(int index) {
            this.index = index;
            this.toString = index + "s";
        }

        @Override
        public Object convert(final List<?> args) {
            final Object arg = args.get(index);
            return arg instanceof Constant<?> ? arg.toString() : arg;
        }

        @Override
        public boolean isString() {
            return true;
        }

        @Override
        public String toString() {
            return toString;
        }

    }

    /**
     * Static text element
     */
    public static final class StaticText extends Element {

        private static final long serialVersionUID = -2791869625053368023L;

        private final String text;

        private final String toString;

        public StaticText(String text) {
            this.text = text;
            this.toString = "'" + text + "'";
        }

        @Override
        public boolean isString() {
            return true;
        }

        @Override
        public Object convert(List<?> args) {
            return text;
        }

        @Override
        public String toString() {
            return toString;
        }

    }

    /**
     * Transformed expression
     */
    public static final class Transformed extends Element {

        private static final long serialVersionUID = 702677732175745567L;

        private final int index;

        private final transient Function<Object, Object> transformer;

        private final String toString;

        public Transformed(int index, Function<Object, Object> transformer) {
            this.index = index;
            this.transformer = transformer;
            this.toString = String.valueOf(index);
        }

        @Override
        public Object convert(final List<?> args) {
            return transformer.apply(args.get(index));
        }

        @Override
        public boolean isString() {
            return false;
        }

        @Override
        public String toString() {
            return toString;
        }

    }

    /**
     * Argument by index
     */
    public static final class ByIndex extends Element {

        private static final long serialVersionUID = 4711323946026029998L;

        private final int index;

        private final String toString;

        public ByIndex(int index) {
            this.index = index;
            this.toString = String.valueOf(index);
        }

        @Override
        public Object convert(final List<?> args) {
            final Object arg = args.get(index);
            if (arg instanceof Expression) {
                return ExpressionUtils.extract((Expression<?>) arg);
            } else {
                return arg;
            }
        }

        public int getIndex() {
            return index;
        }

        @Override
        public boolean isString() {
            return false;
        }

        @Override
        public String toString() {
            return toString;
        }

    }

    /**
     * Math operation
     */
    public static final class Operation extends Element {

        private static final long serialVersionUID = 1400801176778801584L;

        private final int index1, index2;

        private final Operator operator;

        private final boolean asString;

        public Operation(int index1, int index2, Operator operator, boolean asString) {
            this.index1 = index1;
            this.index2 = index2;
            this.operator = operator;
            this.asString = asString;
        }

        @Override
        public Object convert(List<?> args) {
            Object arg1 = args.get(index1);
            Object arg2 = args.get(index2);
            if (isNumber(arg1) && isNumber(arg2)) {
                return MathUtils.result(asNumber(arg1), asNumber(arg2), operator);
            } else {
                Expression<?> expr1 = asExpression(arg1);
                Expression<?> expr2 = asExpression(arg2);
                return ExpressionUtils.operation(expr1.getType(), operator, expr1, expr2);
            }
        }

        @Override
        public boolean isString() {
            return asString;
        }

        @Override
        public String toString() {
            return index1 + " " + operator + " " + index2;
        }
    }

    /**
     * Math operation with constant
     */
    public static final class OperationConst extends Element {

        private static final long serialVersionUID = 1400801176778801584L;

        private final int index1;

        private final BigDecimal arg2;

        private final Expression<BigDecimal> expr2;

        private final Operator operator;

        private final boolean asString;

        public OperationConst(int index1, BigDecimal arg2, Operator operator, boolean asString) {
            this.index1 = index1;
            this.arg2 = arg2;
            this.expr2 = Expressions.constant(arg2);
            this.operator = operator;
            this.asString = asString;
        }

        @Override
        public Object convert(List<?> args) {
            Object arg1 = args.get(index1);
            if (isNumber(arg1)) {
                return MathUtils.result(asNumber(arg1), arg2, operator);
            } else {
                Expression<?> expr1 = asExpression(arg1);
                return ExpressionUtils.operation(expr1.getType(), operator, expr1, expr2);
            }
        }

        @Override
        public boolean isString() {
            return asString;
        }

        @Override
        public String toString() {
            return index1 + " " + operator + " " + arg2;
        }
    }

    private final ImmutableList<Element> elements;

    private final String template;

    Template(String template, ImmutableList<Element> elements) {
        this.template = template;
        this.elements = elements;
    }

    public List<Element> getElements() {
        return elements;
    }

    @Override
    public String toString() {
        return template;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof Template) {
            return ((Template) o).template.equals(template);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return template.hashCode();
    }

    private static Number asNumber(Object arg) {
        if (arg instanceof Number) {
            return (Number) arg;
        } else if (arg instanceof Constant) {
            return (Number) ((Constant) arg).getConstant();
        } else {
            throw new IllegalArgumentException(arg.toString());
        }
    }

    private static boolean isNumber(Object o) {
        return o instanceof Number || o instanceof Constant
                && ((Constant<?>) o).getConstant() instanceof Number;
    }

    private static Expression<?> asExpression(Object arg) {
        if (arg instanceof Expression) {
            return ExpressionUtils.extract((Expression<?>) arg);
        } else {
            return Expressions.constant(arg);
        }
    }

}
