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

import com.querydsl.core.types.dsl.*
import org.junit.Test
import java.math.BigDecimal
import kotlin.test.assertEquals

class CollectionTest {

    @Test
    fun basicArithmetic() {
        val someBigDecimal = Expressions.numberPath(BigDecimal::class.java, "a")
        val someDouble = Expressions.numberPath(Double::class.java, "b");
        val someInteger = Expressions.numberPath(Int::class.java, "c")
        val someOtherInteger = Expressions.numberPath(Int::class.java, "d")

        val multiplyResult: NumberExpression<BigDecimal> = someBigDecimal * someDouble / someInteger
        val expected: NumberExpression<BigDecimal> = someBigDecimal.multiply(someDouble).divide(someInteger)
        assertEquals(expected, multiplyResult)

        val sumResult : NumberExpression<BigDecimal> = someBigDecimal + someDouble - someInteger;
        assertEquals(someBigDecimal.add(someDouble).subtract(someInteger), sumResult)

        val moduloResult = someInteger % someOtherInteger
        assertEquals(someInteger.mod(someOtherInteger), moduloResult)
    }


    @Test
    fun basicArithmeticWithConstants() {
        val someBigDecimal = Expressions.numberPath(BigDecimal::class.java, "a")
        val someInteger = Expressions.numberPath(Int::class.java, "c")

        val constantArithmeticResult = (((someBigDecimal * 2) - 1) / 4) + 1
        val expectedConstantArithmeticResult = someBigDecimal.multiply(2).subtract(1).divide(4).add(1)
        assertEquals(expectedConstantArithmeticResult, constantArithmeticResult)

        val moduloResult = someInteger % 5
        assertEquals(someInteger.mod(5), moduloResult)
    }

    @Test
    fun booleanOperators() {
        val someBoolean = Expressions.booleanPath("a");
        val someOtherBoolean = Expressions.booleanPath("b");

        val booleanResult = (!someBoolean) or (someBoolean and someOtherBoolean)
        assertEquals(someBoolean.not().or(someBoolean.and(someOtherBoolean)), booleanResult)
    }

    @Test
    fun stringOperators() {
        val someString = Expressions.stringPath("a");
        val someOtherString = Expressions.stringPath("b");
        val someIndex = Expressions.numberPath(Int::class.java, "idx")

        val firstChar = someString[0]
        assertEquals(someString.charAt(0), firstChar)

        val firstCharExpr = someString[someIndex];
        assertEquals(someString.charAt(someIndex), firstCharExpr)

        val stringConcat = someString + someOtherString + "c"
        assertEquals(someString.concat(someOtherString).concat("c"), stringConcat)
    }

    @Test
    fun listPathOperators() {
        val listPath = Expressions.listPath(String::class.java, StringPath::class.java, Expressions.stringPath("a").metadata);

        val someIndex = Expressions.numberPath(Int::class.java, "idx")

        val firstValue = listPath[someIndex]
        assertEquals(listPath.get(someIndex), firstValue)

        val secondValue = listPath[1]
        assertEquals(listPath.get(1), secondValue)
    }

    @Test
    fun mapPathOperators() {
        val mapPath = Expressions.mapPath(String::class.java, String::class.java, StringPath::class.java, Expressions.stringPath("a").metadata);

        val someKey = Expressions.stringPath("idx")

        val firstValue = mapPath[someKey]
        assertEquals(mapPath.get(someKey), firstValue)

        val secondValue = mapPath["second"]
        assertEquals(mapPath.get("second"), secondValue)
    }

}