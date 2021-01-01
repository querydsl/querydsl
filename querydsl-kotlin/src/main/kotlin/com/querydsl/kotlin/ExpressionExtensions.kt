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

package com.querydsl.kotlin

import com.querydsl.core.types.Expression
import com.querydsl.core.types.Ops
import com.querydsl.core.types.dsl.*
import com.querydsl.core.types.dsl.Expressions.constant

/**
 * Get a negation of this boolean expression
 *
 * @return !this
 */
operator fun Expression<Boolean>.not() : BooleanExpression {
    return Expressions.booleanOperation(Ops.NOT, this)
}

/**
 * Get an intersection of this and the given expression
 *
 * @param predicate right hand side of the union
 * @return this and right
 */
infix fun Expression<Boolean>.and(predicate: Expression<Boolean>) : BooleanExpression {
    return Expressions.booleanOperation(Ops.AND, this, predicate);
}

/**
 * Get a union of this and the given expression
 *
 * @param predicate right hand side of the union
 * @return this || right
 */
infix fun Expression<Boolean>.or(predicate: Expression<Boolean>) : BooleanExpression {
    return Expressions.booleanOperation(Ops.OR, this, predicate);
}

/**
 * Get a union of this and the given expression
 *
 * @param predicate right hand side of the union
 * @return this || right
 */
infix fun Expression<Boolean>.xor(predicate: Expression<Boolean>) : BooleanExpression {
    return Expressions.booleanOperation(Ops.XOR, this, predicate);
}

/**
 * Get a union of this and the given expression
 *
 * @param predicate right hand side of the union
 * @return this || right
 */
infix fun Expression<Boolean>.xnor(predicate: Expression<Boolean>) : BooleanExpression {
    return Expressions.booleanOperation(Ops.XNOR, this, predicate);
}

/**
 * Get the negation of this expression
 *
 * @return this * -1
 */
operator fun <T> Expression<T>.unaryMinus() : NumberExpression<T> where T : Comparable<*>, T : Number {
    return Expressions.numberOperation(type, Ops.NEGATE, this);
}

/**
 * Get the sum of this and right
 *
 * @return this + right
 */
operator fun <T, V> Expression<T>.plus(other : Expression<V>) : NumberExpression<T> where T : Comparable<*>, T : Number, V : Number {
    return Expressions.numberOperation(type, Ops.ADD, this, other);
}

/**
 * Get the difference of this and right
 *
 * @return this - right
 */
operator fun <T, V> Expression<T>.minus(other : Expression<V>) : NumberExpression<T> where T : Comparable<*>, T : Number, V : Number {
    return Expressions.numberOperation(type, Ops.SUB, this, other);
}

/**
 * Get the result of the operation this * right
 *
 * @return this * right
 */
operator fun <T, V> Expression<T>.times(other : Expression<V>) : NumberExpression<T> where T : Comparable<*>, T : Number, V : Number {
    return Expressions.numberOperation(type, Ops.MULT, this, other);
}

/**
 * Get the result of the operation this / right
 *
 * @return this / right
 */
operator fun <T, V> Expression<T>.div(other : Expression<V>) : NumberExpression<T> where T : Comparable<*>, T : Number, V : Number {
    return Expressions.numberOperation(type, Ops.DIV, this, other);
}


/**
 * Get the result of the operation this / right
 *
 * @return this / right
 */
operator fun <T, V> NumberExpression<T>.div(other : Expression<V>) : NumberExpression<T> where T : Comparable<*>, T : Number, V : Number, V : Comparable<*> {
    return this.divide(other);
}

/**
 * Get the result of the operation this % right
 *
 * @return this % right
 */
operator fun <T, V> Expression<T>.rem(other : Expression<V>) : NumberExpression<T> where T : Comparable<*>, T : Number, V : Number {
    return Expressions.numberOperation(type, Ops.MOD, this, other);
}

/**
 * Get the sum of this and right
 *
 * @return this + right
 */
operator fun <T, V> Expression<T>.plus(other : V) : NumberExpression<T> where T : Comparable<*>, T : Number, V : Number {
    return Expressions.numberOperation(type, Ops.ADD, this, constant(other));
}

/**
 * Get the difference of this and right
 *
 * @return this - right
 */
operator fun <T, V> Expression<T>.minus(other : V) : NumberExpression<T> where T : Comparable<*>, T : Number, V : Number {
    return Expressions.numberOperation(type, Ops.SUB, this, constant(other));
}

/**
 * Get the result of the operation this * right
 *
 * @return this * right
 */
operator fun <T, V> Expression<T>.times(other : V) : NumberExpression<T> where T : Comparable<*>, T : Number, V : Number {
    return Expressions.numberOperation(type, Ops.MULT, this, constant(other));
}

/**
 * Get the result of the operation this / right
 *
 * @return this / right
 */
operator fun <T, V> Expression<T>.div(other : V) : NumberExpression<T> where T : Comparable<*>, T : Number, V : Number {
    return Expressions.numberOperation(type, Ops.DIV, this, constant(other));
}

/**
 * Get the result of the operation this / right
 *
 * @return this / right
 */
operator fun <T, V> NumberExpression<T>.div(other : V) : NumberExpression<T> where T : Comparable<*>, T : Number, V : Number, V : Comparable<*> {
    return this.divide(other);
}

/**
 * Get the result of the operation this % right
 *
 * @return this % right
 */
operator fun <T, V> Expression<T>.rem(other : V) : NumberExpression<T> where T : Comparable<*>, T : Number, V : Number {
    return Expressions.numberOperation(type, Ops.MOD, this, constant(other));
}

/**
 * Get the concatenation of this and str
 *
 * @return this + str
 */
operator fun Expression<String>.plus(x : Expression<String>) : StringExpression {
    return Expressions.stringOperation(Ops.CONCAT, this, x);
}

/**
 * Get the concatenation of this and str
 *
 * @return this + str
 */
operator fun Expression<String>.plus(x : String) : StringExpression {
    return Expressions.stringOperation(Ops.CONCAT, this, constant(x));
}

/**
 * Get the character at the given index
 *
 * @param x
 * @return this.charAt(x)
 * @see java.lang.String#charAt(int)
 */
operator fun Expression<String>.get(x : Expression<Int>) : SimpleExpression<Character> {
    return Expressions.simpleOperation(Character::class.java, Ops.CHAR_AT, this, x)
}

/**
 * Get the character at the given index
 *
 * @param x
 * @return this.charAt(x)
 * @see java.lang.String#charAt(int)
 */
operator fun Expression<String>.get(x : Int) : SimpleExpression<Character> {
    return Expressions.simpleOperation(Character::class.java, Ops.CHAR_AT, this, constant(x))
}
