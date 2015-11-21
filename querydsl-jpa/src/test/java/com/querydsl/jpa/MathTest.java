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
package com.querydsl.jpa;

import static com.querydsl.jpa.Constants.*;

import org.junit.Test;

import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.domain.QCat;

public class MathTest extends AbstractQueryTest {

    @Test
    public void test() {
        NumberPath<Double> path = QCat.cat.bodyWeight;
        assertToString("(cat.bodyWeight - sum(cat.bodyWeight)) * cat.bodyWeight", path.subtract(path.sum()).multiply(path));
    }

    @Test
    public void add() {
        assertToString("cat.bodyWeight + ?1", cat.bodyWeight.add(10));
    }

    @Test
    public void subtract() {
        assertToString("cat.bodyWeight - ?1", cat.bodyWeight.subtract(10));
    }

    @Test
    public void multiply() {
        assertToString("cat.bodyWeight * ?1", cat.bodyWeight.multiply(10));
    }

    @Test
    public void divide() {
        assertToString("cat.bodyWeight / ?1", cat.bodyWeight.divide(10));
    }

    @Test
    public void add_and_compare() {
        assertToString("cat.bodyWeight + ?1 < ?1", cat.bodyWeight.add(10.0).lt(10.0));
    }

    @Test
    public void subtract_and_compare() {
        assertToString("cat.bodyWeight - ?1 < ?1", cat.bodyWeight.subtract(10.0).lt(10.0));
    }

    @Test
    public void multiply_and_compare() {
        assertToString("cat.bodyWeight * ?1 < ?1", cat.bodyWeight.multiply(10.0).lt(10.0));
    }

    @Test
    public void divide_and_compare() {
        assertToString("cat.bodyWeight / ?1 < ?2", cat.bodyWeight.divide(10.0).lt(20.0));
    }

    @Test
    public void add_and_multiply() {
        assertToString("(cat.bodyWeight + ?1) * ?2", cat.bodyWeight.add(10).multiply(20));
    }

    @Test
    public void subtract_and_multiply() {
        assertToString("(cat.bodyWeight - ?1) * ?2", cat.bodyWeight.subtract(10).multiply(20));
    }


    @Test
    public void multiply_and_add() {
        assertToString("cat.bodyWeight * ?1 + ?2", cat.bodyWeight.multiply(10).add(20));
    }


    @Test
    public void multiply_and_subtract() {
        assertToString("cat.bodyWeight * ?1 - ?2", cat.bodyWeight.multiply(10).subtract(20));
    }


    @Test
    public void arithmetic_and_arithmetic2() {
        QCat c1 = new QCat("c1");
        QCat c2 = new QCat("c2");
        QCat c3 = new QCat("c3");
        assertToString("c1.id + c2.id * c3.id", c1.id.add(c2.id.multiply(c3.id)));
        assertToString("c1.id * (c2.id + c3.id)", c1.id.multiply(c2.id.add(c3.id)));
        assertToString("(c1.id + c2.id) * c3.id", c1.id.add(c2.id).multiply(c3.id));
    }

    @Test
    public void mathematicalOperations() {
        // mathematical operators +, -, *, /
        cat.bodyWeight.add(kitten.bodyWeight);
        cat.bodyWeight.subtract(kitten.bodyWeight);
        cat.bodyWeight.multiply(kitten.bodyWeight);
        cat.bodyWeight.divide(kitten.bodyWeight);
    }

}
