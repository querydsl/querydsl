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
package com.querydsl.jpa;

import org.junit.Test;

import com.querydsl.jpa.domain.QCat;
import com.querydsl.core.types.path.NumberPath;

public class MathTest extends AbstractQueryTest{

    @Test
    public void test() {
        NumberPath<Double> path = QCat.cat.bodyWeight;
        assertToString("(cat.bodyWeight - sum(cat.bodyWeight)) * cat.bodyWeight", path.subtract(path.sum()).multiply(path));
    }

    @Test
    public void Add() {
        assertToString("cat.bodyWeight + ?1", cat.bodyWeight.add(10));
    }
    
    @Test
    public void Subtract() {
        assertToString("cat.bodyWeight - ?1", cat.bodyWeight.subtract(10));
    }
    
    @Test
    public void Multiply() {
        assertToString("cat.bodyWeight * ?1", cat.bodyWeight.multiply(10));
    }
    
    @Test
    public void Divide() {
        assertToString("cat.bodyWeight / ?1", cat.bodyWeight.divide(10));   
    }
        
    @Test
    public void Add_And_Compare() {
        assertToString("cat.bodyWeight + ?1 < ?1", cat.bodyWeight.add(10.0).lt(10.0));
    }
    
    @Test
    public void Subtract_And_Compare() {
        assertToString("cat.bodyWeight - ?1 < ?1", cat.bodyWeight.subtract(10.0).lt(10.0));    
    }
    
    @Test
    public void Multiply_And_Compare() {
        assertToString("cat.bodyWeight * ?1 < ?1", cat.bodyWeight.multiply(10.0).lt(10.0));   
    }
    
    @Test
    public void Divide_And_Compare() {
        assertToString("cat.bodyWeight / ?1 < ?2", cat.bodyWeight.divide(10.0).lt(20.0));
    }
    
    @Test
    public void Add_And_Multiply() {
        assertToString("(cat.bodyWeight + ?1) * ?2", cat.bodyWeight.add(10).multiply(20));
    }
    
    @Test
    public void Subtract_And_Multiply() {
        assertToString("(cat.bodyWeight - ?1) * ?2", cat.bodyWeight.subtract(10).multiply(20));
    }
    
    
    @Test
    public void Multiply_And_Add() {
        assertToString("cat.bodyWeight * ?1 + ?2", cat.bodyWeight.multiply(10).add(20));
    }
    
    
    @Test
    public void Multiply_And_Subtract() {
        assertToString("cat.bodyWeight * ?1 - ?2", cat.bodyWeight.multiply(10).subtract(20));
    }
    
    
    @Test
    public void Arithmetic_And_Arithmetic2() {
        QCat c1 = new QCat("c1");
        QCat c2 = new QCat("c2");
        QCat c3 = new QCat("c3");
        assertToString("c1.id + c2.id * c3.id", c1.id.add(c2.id.multiply(c3.id)));
        assertToString("c1.id * (c2.id + c3.id)", c1.id.multiply(c2.id.add(c3.id)));
        assertToString("(c1.id + c2.id) * c3.id", c1.id.add(c2.id).multiply(c3.id));   
    }
    
    @Test
    public void MathematicalOperations() {
        // mathematical operators +, -, *, /
        cat.bodyWeight.add(kitten.bodyWeight);
        cat.bodyWeight.subtract(kitten.bodyWeight);
        cat.bodyWeight.multiply(kitten.bodyWeight);
        cat.bodyWeight.divide(kitten.bodyWeight);
    }

}
