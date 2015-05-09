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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.querydsl.core.support.Context;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.domain.QCat;
import com.querydsl.jpa.domain.QDomesticCat;
import com.querydsl.jpa.domain.QEmployee;


public class JPACollectionAnyVisitorTest {

    private QCat cat = QCat.cat;

    @Test
    public void Path() {
        assertEquals("cat_kittens_0", serialize(cat.kittens.any()));
    }

    @Test
    public void Longer_Path() {
        assertEquals("cat_kittens_0.name", serialize(cat.kittens.any().name));
    }

    @Test
    public void Simple_BooleanOperation() {
        Predicate predicate = cat.kittens.any().name.eq("Ruth123");
        assertEquals("exists (select 1\n" +
                "from cat.kittens as cat_kittens_0\n" +
                "where cat_kittens_0.name = ?1)", serialize(predicate));
    }

    @Test
    public void Simple_BooleanOperation_ElementCollection() {
        QEmployee employee = QEmployee.employee;
        Predicate predicate = employee.jobFunctions.any().stringValue().eq("CODER");
        assertEquals("exists (select 1\n" +
                "from Employee employee_1463394548\n" +
                "  inner join employee_1463394548.jobFunctions as employee_jobFunctions_0\n" +
                "where employee_1463394548 = employee and str(employee_jobFunctions_0) = ?1)", serialize(predicate));
    }

    @Test
    public void Simple_StringOperation() {
        Predicate predicate = cat.kittens.any().name.substring(1).eq("uth123");
        assertEquals("exists (select 1\n" +
                "from cat.kittens as cat_kittens_0\n" +
                "where substring(cat_kittens_0.name,2) = ?1)", serialize(predicate));
    }

    @Test
    public void And_Operation() {
        Predicate predicate = cat.kittens.any().name.eq("Ruth123").and(cat.kittens.any().bodyWeight.gt(10.0));
        assertEquals("exists (select 1\n" +
                "from cat.kittens as cat_kittens_0\n" +
                "where cat_kittens_0.name = ?1) and exists (select 1\n" +
                "from cat.kittens as cat_kittens_1\n" +
                "where cat_kittens_1.bodyWeight > ?2)", serialize(predicate));
    }

    @Test
    public void Template() {
        Expression<Boolean> templateExpr = ExpressionUtils.template(Boolean.class, "{0} = {1}",
                cat.kittens.any().name, ConstantImpl.create("Ruth123"));
        assertEquals("exists (select 1\n" +
                "from cat.kittens as cat_kittens_0\n" +
                "where cat_kittens_0.name = ?1)", serialize(templateExpr));
    }

    @Test
    public void Cast() {
//        JPAQuery query = new JPAQuery(em).from(QPerson.person);
//        QDog anyDog = QPerson.person.animals.any().as(QDog.class);
//        query.where(anyDog.gender.eq("M"));
//        List<Person> foundOwners = query.fetch(QPerson.person);

        QDomesticCat anyCat = QCat.cat.kittens.any().as(QDomesticCat.class);
        Predicate predicate = anyCat.name.eq("X");

        assertEquals("exists (select 1\n" +
                "from cat.kittens as cat_kittens_0\n" +
                "where cat_kittens_0.name = ?1)", serialize(predicate));
    }

    private String serialize(Expression<?> expression) {
        Expression<?> transformed = expression.accept(new JPACollectionAnyVisitor(), new Context());
        JPQLSerializer serializer = new JPQLSerializer(HQLTemplates.DEFAULT, null);
        serializer.handle(transformed);
        return serializer.toString();
    }

}
