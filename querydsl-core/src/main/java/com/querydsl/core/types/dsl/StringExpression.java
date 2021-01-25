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

import org.jetbrains.annotations.Nullable;

import com.querydsl.core.types.*;

/**
 * {@code StringExpression} represents {@link java.lang.String} expressions
 *
 * @author tiwe
 * @see java.lang.String
 */
public abstract class StringExpression extends LiteralExpression<String> {

    private static final long serialVersionUID = 1536955079961023361L;

    @Nullable
    private transient volatile NumberExpression<Integer> length;

    @Nullable
    private transient volatile StringExpression lower, trim, upper;

    @Nullable
    private transient volatile StringExpression min, max;

    @Nullable
    private transient volatile BooleanExpression isempty;

    public StringExpression(Expression<String> mixin) {
        super(mixin);
    }

    @Override
    public StringExpression as(Path<String> alias) {
        return Expressions.stringOperation(Ops.ALIAS, mixin, alias);
    }

    @Override
    public StringExpression as(String alias) {
        return as(ExpressionUtils.path(String.class, alias));
    }

    /**
     * Create a {@code concat(this, str)} expression
     *
     * <p>Get the concatenation of this and str</p>
     *
     * @param str string to append
     * @return this + str
     */
    public StringExpression append(Expression<String> str) {
        return Expressions.stringOperation(Ops.CONCAT, mixin, str);
    }

    /**
     * Create a {@code concat(this, str)} expression
     *
     * <p>Get the concatenation of this and str</p>
     *
     * @param str string to append
     * @return this + str
     */
    public StringExpression append(String str) {
        return append(ConstantImpl.create(str));
    }

    /**
     * Create a {@code this.charAt(i)} expression
     *
     * <p>Get the character at the given index</p>
     *
     * @param i zero based index
     * @return this.charAt(i)
     * @see java.lang.String#charAt(int)
     */
    public SimpleExpression<Character> charAt(Expression<Integer> i) {
        return Expressions.comparableOperation(Character.class, Ops.CHAR_AT, mixin, i);
    }

    /**
     * Create a {@code this.charAt(i)} expression
     *
     * <p>Get the character at the given index</p>
     *
     * @param i zero based index
     * @return this.charAt(i)
     * @see java.lang.String#charAt(int)
     */
    public SimpleExpression<Character> charAt(int i) {
        return charAt(ConstantImpl.create(i));
    }

    /**
     * Create a {@code concat(this, str)} expression
     *
     * <p>Get the concatenation of this and str</p>
     *
     * @param str string to append
     * @return this + str
     */
    public StringExpression concat(Expression<String> str) {
        return append(str);
    }

    /**
     * Create a {@code concat(this, str)} expression
     *
     * <p>Get the concatenation of this and str</p>
     *
     * @param str string to append
     * @return this + str
     */
    public StringExpression concat(String str) {
        return append(str);
    }

    /**
     * Create a {@code this.contains(str)} expression
     *
     * <p>Returns true if the given String is contained</p>
     *
     * @param str string
     * @return this.contains(str)
     * @see java.lang.String#contains(CharSequence)
     */
    public BooleanExpression contains(Expression<String> str) {
        return Expressions.booleanOperation(Ops.STRING_CONTAINS, mixin, str);
    }

    /**
     * Create a {@code this.contains(str)} expression
     *
     * <p>Returns true if the given String is contained</p>
     *
     * @param str string
     * @return this.contains(str)
     * @see java.lang.String#contains(CharSequence)
     */
    public BooleanExpression contains(String str) {
        return contains(ConstantImpl.create(str));
    }

    /**
     * Create a {@code this.containsIgnoreCase(str)} expression
     *
     * <p>Returns true if the given String is contained, compare case insensitively</p>
     *
     * @param str string
     * @return this.containsIgnoreCase(str) expression
     */
    public BooleanExpression containsIgnoreCase(Expression<String> str) {
        return Expressions.booleanOperation(Ops.STRING_CONTAINS_IC, mixin, str);
    }

    /**
     * Create a {@code this.containsIgnoreCase(str)} expression
     *
     * <p>Returns true if the given String is contained, compare case insensitively</p>
     *
     * @param str string
     * @return this.containsIgnoreCase(str)
     */
    public BooleanExpression containsIgnoreCase(String str) {
        return containsIgnoreCase(ConstantImpl.create(str));
    }

    /**
     * Create a {@code this.endsWith(str)} expression
     *
     * <p>Returns true if this ends with str</p>
     *
     * @param str string
     * @return this.endsWith(str)
     * @see java.lang.String#endsWith(String)
     */
    public BooleanExpression endsWith(Expression<String> str) {
        return Expressions.booleanOperation(Ops.ENDS_WITH, mixin, str);
    }

    /**
     * Create a {@code this.endsWithIgnoreCase(str)} expression
     *
     * <p>Returns true if this ends with str, compares case insensitively</p>
     *
     * @param str string
     * @return this.endsWithIgnoreCase(str)
     */
    public BooleanExpression endsWithIgnoreCase(Expression<String> str) {
        return Expressions.booleanOperation(Ops.ENDS_WITH_IC, mixin, str);
    }

    /**
     * Create a {@code this.endsWith(str)} expression
     *
     * <p>Returns true if this ends with str</p>
     *
     * @param str string
     * @return this.endsWith(str)
     * @see java.lang.String#endsWith(String)
     */
    public BooleanExpression endsWith(String str) {
        return endsWith(ConstantImpl.create(str));
    }

    /**
     * Create a {@code this.endsWithIgnoreCase(str)} expression
     *
     * <p>Returns true if this ends with str, compares case insensitively</p>
     *
     * @param str string
     * @return this.endsWithIgnoreCase(str)
     */
    public BooleanExpression endsWithIgnoreCase(String str) {
        return endsWithIgnoreCase(ConstantImpl.create(str));
    }

    /**
     * Create a {@code this.equalsIgnoreCase(str)} expression
     *
     * <p>Compares this {@code StringExpression} to another {@code StringExpression}, ignoring case
     * considerations.</p>
     *
     * @param str string
     * @return this.equalsIgnoreCase(str)
     * @see java.lang.String#equalsIgnoreCase(String)
     */
    public BooleanExpression equalsIgnoreCase(Expression<String> str) {
        return Expressions.booleanOperation(Ops.EQ_IGNORE_CASE, mixin, str);
    }

    /**
     * Create a {@code this.equalsIgnoreCase(str)} expression
     *
     * <p>Compares this {@code StringExpression} to another {@code StringExpression}, ignoring case
     * considerations.</p>
     *
     * @param str string
     * @return this.equalsIgnoreCase(str)
     * @see java.lang.String#equalsIgnoreCase(String)
     */
    public BooleanExpression equalsIgnoreCase(String str) {
        return equalsIgnoreCase(ConstantImpl.create(str));
    }

    /**
     * Create a {@code this.indexOf(str)} expression
     *
     * <p>Get the index of the given substring in this String</p>
     *
     * @param str string
     * @return this.indexOf(str)
     * @see java.lang.String#indexOf(String)
     */
    public NumberExpression<Integer> indexOf(Expression<String> str) {
        return Expressions.numberOperation(Integer.class, Ops.INDEX_OF, mixin, str);
    }

    /**
     * Create a {@code this.indexOf(str)} expression
     *
     * <p>Get the index of the given substring in this String</p>
     *
     * @param str string
     * @return this.indexOf(str)
     * @see java.lang.String#indexOf(String)
     */
    public NumberExpression<Integer> indexOf(String str) {
        return indexOf(ConstantImpl.create(str));
    }

    /**
     * Create a {@code this.indexOf(str, i)} expression
     *
     * <p>Get the index of the given substring in this String, starting from the given index</p>
     *
     * @param str string
     * @param i zero based index
     * @return this.indexOf(str, i)
     * @see java.lang.String#indexOf(String, int)
     */
    public NumberExpression<Integer> indexOf(String str, int i) {
        return indexOf(ConstantImpl.create(str), i);
    }

    /**
     * Create a {@code this.indexOf(str)} expression
     *
     * <p>Get the index of the given substring in this String, starting from the given index</p>
     *
     * @param str string
     * @param i zero based index
     * @return this.indexOf(str)
     */
    public NumberExpression<Integer> indexOf(Expression<String> str, int i) {
        return Expressions.numberOperation(Integer.class, Ops.INDEX_OF_2ARGS, mixin, str, ConstantImpl.create(i));
    }

    /**
     * Create a {@code this.isEmpty()} expression
     *
     * <p>Return true if this String is empty</p>
     *
     * @return this.isEmpty()
     * @see java.lang.String#isEmpty()
     */
    public BooleanExpression isEmpty() {
        if (isempty == null) {
            isempty = Expressions.booleanOperation(Ops.STRING_IS_EMPTY, mixin);
        }
        return isempty;
    }

    /**
     * Create a {@code !this.isEmpty()} expression
     *
     * <p>Return true if this String is not empty</p>
     *
     * @return !this.isEmpty()
     * @see java.lang.String#isEmpty()
     */
    public BooleanExpression isNotEmpty() {
        return isEmpty().not();
    }

    /**
     * Create a {@code this.length()} expression
     *
     * <p>Return the length of this String</p>
     *
     * @return this.length()
     * @see java.lang.String#length()
     */
    public NumberExpression<Integer> length() {
        if (length == null) {
            length = Expressions.numberOperation(Integer.class, Ops.STRING_LENGTH, mixin);
        }
        return length;
    }

    /**
     * Create a {@code this like str} expression
     *
     * @param str string
     * @return this like str
     */
    public BooleanExpression like(String str) {
        return Expressions.booleanOperation(Ops.LIKE, this, ConstantImpl.create(str));
    }

    /**
     * Create a {@code this like str} expression
     *
     * @param str string
     * @return this like str
     */
    public BooleanExpression like(Expression<String> str) {
        return Expressions.booleanOperation(Ops.LIKE, mixin, str);
    }

    /**
     * Create a {@code this like str} expression ignoring case
     *
     * @param str string
     * @return this like str
     */
    public BooleanExpression likeIgnoreCase(String str) {
        return Expressions.booleanOperation(Ops.LIKE_IC, mixin, ConstantImpl.create(str));
    }

    /**
     * Create a {@code this like str} expression ignoring case
     *
     * @param str string
     * @return this like str
     */
    public BooleanExpression likeIgnoreCase(Expression<String> str) {
        return Expressions.booleanOperation(Ops.LIKE_IC, mixin, str);
    }

    /**
     * Create a {@code this like str} expression
     *
     * @param str string
     * @return this like str
     */
    public BooleanExpression like(String str, char escape) {
        return Expressions.booleanOperation(Ops.LIKE_ESCAPE, mixin, ConstantImpl.create(str), ConstantImpl.create(escape));
    }

    /**
     * Create a {@code this like str} expression
     *
     * @param str string
     * @return this like str
     */
    public BooleanExpression like(Expression<String> str, char escape) {
        return Expressions.booleanOperation(Ops.LIKE_ESCAPE, mixin, str, ConstantImpl.create(escape));
    }

    /**
     * Create a {@code this like str} expression ignoring case
     *
     * @param str string
     * @param escape escape character
     * @return this like string
     */
    public BooleanExpression likeIgnoreCase(String str, char escape) {
        return Expressions.booleanOperation(Ops.LIKE_ESCAPE_IC, mixin, ConstantImpl.create(str), ConstantImpl.create(escape));
    }

    /**
     * Create a {@code this like str} expression ignoring case
     *
     * @param str string
     * @param escape escape character
     * @return this like string
     */
    public BooleanExpression likeIgnoreCase(Expression<String> str, char escape) {
        return Expressions.booleanOperation(Ops.LIKE_ESCAPE_IC, mixin, str, ConstantImpl.create(escape));
    }

    /**
     * Create a {@code locate(str, this)} expression
     *
     * <p>Get the position of the given String in this String, the first position is 1</p>
     *
     * @param str string
     * @return locate(str, this)
     */
    public NumberExpression<Integer> locate(Expression<String> str) {
        return Expressions.numberOperation(Integer.class, Ops.StringOps.LOCATE, str, mixin);
    }

    /**
     * Create a {@code locate(str, this)} expression
     *
     * <p>Get the position of the given String in this String, the first position is 1</p>
     *
     * @param str string
     * @return locate(str, this)
     */
    public NumberExpression<Integer> locate(String str) {
        return Expressions.numberOperation(Integer.class, Ops.StringOps.LOCATE, ConstantImpl.create(str), mixin);
    }

    /**
     * Create a {@code locate(str, this, start)} expression
     *
     * <p>Get the position of the given String in this String, the first position is 1</p>
     *
     * @param str string
     * @param start start
     * @return locate(str, this, start)
     */
    public NumberExpression<Integer> locate(Expression<String> str, NumberExpression<Integer> start) {
        return Expressions.numberOperation(Integer.class, Ops.StringOps.LOCATE2, str, mixin, start);
    }

    /**
     * Create a {@code locate(str, this, start)} expression
     *
     * <p>Get the position of the given String in this String, the first position is 1</p>
     *
     * @param str string
     * @param start start
     * @return locate(str, this, start)
     */
    public NumberExpression<Integer> locate(String str, int start) {
        return Expressions.numberOperation(Integer.class, Ops.StringOps.LOCATE2, ConstantImpl.create(str), mixin, ConstantImpl.create(start));
    }

    /**
     * Create a {@code locate(str, this, start)} expression
     *
     * <p>Get the position of the given String in this String, the first position is 1</p>
     *
     * @param str string
     * @param start start
     * @return locate(str, this, start)
     */
    public NumberExpression<Integer> locate(String str, Expression<Integer> start) {
        return Expressions.numberOperation(Integer.class, Ops.StringOps.LOCATE2, ConstantImpl.create(str), mixin, start);
    }

    /**
     * Create a {@code this.toLowerCase()} expression
     *
     * <p>Get the lower case form</p>
     *
     * @return this.toLowerCase()
     * @see java.lang.String#toLowerCase()
     */
    public StringExpression lower() {
        if (lower == null) {
            lower = Expressions.stringOperation(Ops.LOWER, mixin);
        }
        return lower;
    }

    /**
     * Create a {@code this.matches(regex)} expression
     *
     * <p>Return true if this String matches the given regular expression</p>
     *
     * <p>Some implementations such as Querydsl JPA will try to convert a regex expression into like
     * form and will throw an Exception when this fails</p>
     *
     * @param regex regular expression
     * @return this.matches(right)
     * @see java.lang.String#matches(String)
     */
    public BooleanExpression matches(Expression<String> regex) {
        return Expressions.booleanOperation(Ops.MATCHES, mixin, regex);
    }

    /**
     * Create a {@code this.matches(regex)} expression
     *
     * <p>Return true if this String matches the given regular expression</p>
     *
     * <p>Some implementations such as Querydsl JPA will try to convert a regex expression into like
     * form and will throw an Exception when this fails</p>
     *
     * @param regex regular expression
     * @return this.matches(regex)
     * @see java.lang.String#matches(String)
     */
    public BooleanExpression matches(String regex) {
        return matches(ConstantImpl.create(regex));
    }

    /**
     * Create a {@code max(this)} expression
     *
     * <p>Get the maximum value of this expression (aggregation)</p>
     *
     * @return max(this)
     */
    public StringExpression max() {
        if (max == null) {
            max = Expressions.stringOperation(Ops.AggOps.MAX_AGG, mixin);
        }
        return max;
    }

    /**
     * Create a {@code min(this)} expression
     *
     * <p>Get the minimum value of this expression (aggregation)</p>
     *
     * @return min(this)
     */
    @Override
    public StringExpression min() {
        if (min == null) {
            min = Expressions.stringOperation(Ops.AggOps.MIN_AGG, mixin);
        }
        return min;
    }

    /**
     * Create a {@code !this.equalsIgnoreCase(str)} expression
     *
     * <p>Compares this {@code StringExpression} to another {@code StringExpression}, ignoring case
     * considerations.</p>
     *
     * @param str string
     * @return !this.equalsIgnoreCase(str)
     * @see java.lang.String#equalsIgnoreCase(String)
     */
    public BooleanExpression notEqualsIgnoreCase(Expression<String> str) {
        return equalsIgnoreCase(str).not();
    }


    /**
     * Create a {@code !this.equalsIgnoreCase(str)} expression
     *
     * <p>Compares this {@code StringExpression} to another {@code StringExpression}, ignoring case
     * considerations.</p>
     *
     * @param str string
     * @return !this.equalsIgnoreCase(str)
     * @see java.lang.String#equalsIgnoreCase(String)
     */
    public BooleanExpression notEqualsIgnoreCase(String str) {
        return equalsIgnoreCase(str).not();
    }

    /**
     * Create a {@code this not like str} expression
     *
     * @param str string
     * @return this not like str
     */
    public BooleanExpression notLike(String str) {
        return like(str).not();
    }

    /**
     * Create a {@code this not like str} expression
     *
     * @param str string
     * @return this not like str
     */
    public BooleanExpression notLike(Expression<String> str) {
        return like(str).not();
    }

    /**
     * Create a {@code this not like str} expression
     *
     * @param str string
     * @return this not like str
     */
    public BooleanExpression notLike(String str, char escape) {
        return like(str, escape).not();
    }

    /**
     * Create a {@code this not like str} expression
     *
     * @param str string
     * @return this not like str
     */
    public BooleanExpression notLike(Expression<String> str, char escape) {
        return like(str, escape).not();
    }

    /**
     * Create a {@code concat(str, this)} expression
     *
     * <p>Prepend the given String and return the result</p>
     *
     * @param str string
     * @return str + this
     */
    public StringExpression prepend(Expression<String> str) {
        return Expressions.stringOperation(Ops.CONCAT, str, mixin);
    }

    /**
     * Create a {@code concat(str, this)} expression
     *
     * <p>Prepend the given String and return the result</p>
     *
     * @param str string
     * @return str + this
     */
    public StringExpression prepend(String str) {
        return prepend(ConstantImpl.create(str));
    }

    /**
     * Create a {@code this.startsWith(str)} expression
     *
     * <p>Return true if this starts with str</p>
     *
     * @param str string
     * @return this.startsWith(str)
     * @see java.lang.String#startsWith(String)
     */
    public BooleanExpression startsWith(Expression<String> str) {
        return Expressions.booleanOperation(Ops.STARTS_WITH, mixin, str);
    }

    /**
     * Create a {@code this.startsWithIgnoreCase(str)} expression
     *
     * @param str string
     * @return this.startsWithIgnoreCase(str)
     */
    public BooleanExpression startsWithIgnoreCase(Expression<String> str) {
        return Expressions.booleanOperation(Ops.STARTS_WITH_IC, mixin, str);
    }

    /**
     * Create a {@code this.startsWith(str)} expression
     *
     * <p>Return true if this starts with str</p>
     *
     * @param str string
     * @return this.startsWith(str)
     * @see java.lang.String#startsWith(String)
     */
    public BooleanExpression startsWith(String str) {
        return startsWith(ConstantImpl.create(str));
    }

    /**
     * Create a {@code this.startsWithIgnoreCase(str)} expression
     *
     * @param str string
     * @return this.startsWithIgnoreCase(str)
     */
    public BooleanExpression startsWithIgnoreCase(String str) {
        return startsWithIgnoreCase(ConstantImpl.create(str));
    }

    @Override
    public StringExpression stringValue() {
        return this;
    }

    /**
     * Create a {@code this.substring(beginIndex)} expression
     *
     * @param beginIndex inclusive start index
     * @return this.substring(beginIndex)
     * @see java.lang.String#substring(int)
     */
    public StringExpression substring(int beginIndex) {
        return Expressions.stringOperation(Ops.SUBSTR_1ARG, mixin, ConstantImpl.create(beginIndex));
    }

    /**
     * Create a {@code this.substring(beginIndex, endIndex)} expression
     *
     * @param beginIndex inclusive start index
     * @param endIndex exclusive end index
     * @return this.substring(beginIndex, endIndex)
     * @see java.lang.String#substring(int, int)
     */
    public StringExpression substring(int beginIndex, int endIndex) {
        return Expressions.stringOperation(Ops.SUBSTR_2ARGS, mixin, ConstantImpl.create(beginIndex), ConstantImpl.create(endIndex));
    }

    /**
     * Create a {@code this.substring(beginIndex, endIndex)} expression
     *
     * @param beginIndex inclusive start index
     * @param endIndex exclusive end index
     * @return this.substring(beginIndex, endIndex)
     * @see java.lang.String#substring(int, int)
     */
    public StringExpression substring(Expression<Integer> beginIndex, int endIndex) {
        return Expressions.stringOperation(Ops.SUBSTR_2ARGS, mixin, beginIndex, ConstantImpl.create(endIndex));
    }

    /**
     * Create a {@code this.substring(beginIndex, endIndex)} expression
     *
     * @param beginIndex inclusive start index
     * @param endIndex exclusive end index
     * @return this.substring(beginIndex, endIndex)
     * @see java.lang.String#substring(int, int)
     */
    public StringExpression substring(int beginIndex, Expression<Integer> endIndex) {
        return Expressions.stringOperation(Ops.SUBSTR_2ARGS, mixin, ConstantImpl.create(beginIndex), endIndex);
    }

    /**
     * Create a {@code this.substring(beginIndex)} expression
     *
     * @param beginIndex inclusive start index
     * @return this.substring(beginIndex)
     * @see java.lang.String#substring(int)
     */
    public StringExpression substring(Expression<Integer> beginIndex) {
        return Expressions.stringOperation(Ops.SUBSTR_1ARG, mixin, beginIndex);
    }

    /**
     * Create a {@code this.substring(beginIndex, endIndex)} expression
     *
     * @param beginIndex inclusive start index
     * @param endIndex exclusive end index
     * @return this.substring(beginIndex, endIndex)
     * @see java.lang.String#substring(int, int)
     */
    public StringExpression substring(Expression<Integer> beginIndex, Expression<Integer> endIndex) {
        return Expressions.stringOperation(Ops.SUBSTR_2ARGS, mixin, beginIndex, endIndex);
    }

    /**
     * Create a {@code this.toLowerCase()} expression
     *
     * <p>Get the lower case form</p>
     *
     * @return this.toLowerCase()
     * @see java.lang.String#toLowerCase()
     */
    public StringExpression toLowerCase() {
        return lower();
    }

    /**
     * Create a {@code this.toUpperCase()} expression
     *
     * <p>Get the upper case form</p>
     *
     * @return this.toUpperCase()
     * @see java.lang.String#toUpperCase()
     */
    public StringExpression toUpperCase() {
        return upper();
    }

    /**
     * Create a {@code this.trim()} expression
     *
     * <p>Create a copy of the string, with leading and trailing whitespace
     * omitted.</p>
     *
     * @return this.trim()
     * @see java.lang.String#trim()
     */
    public StringExpression trim() {
        if (trim == null) {
            trim = Expressions.stringOperation(Ops.TRIM, mixin);
        }
        return trim;
    }

    /**
     * Create a {@code this.toUpperCase()} expression
     *
     * <p>Get the upper case form</p>
     *
     * @return this.toUpperCase()
     * @see java.lang.String#toUpperCase()
     */
    public StringExpression upper() {
        if (upper == null) {
            upper = Expressions.stringOperation(Ops.UPPER, mixin);
        }
        return upper;
    }


    /**
     * Create a {@code nullif(this, other)} expression
     *
     * @param other
     * @return nullif(this, other)
     */
    @Override
    public StringExpression nullif(Expression<String> other) {
        return Expressions.stringOperation(Ops.NULLIF, this, other);
    }

    /**
     * Create a {@code nullif(this, other)} expression
     *
     * @param other
     * @return nullif(this, other)
     */
    @Override
    public StringExpression nullif(String other) {
        return nullif(ConstantImpl.create(other));
    }

}
