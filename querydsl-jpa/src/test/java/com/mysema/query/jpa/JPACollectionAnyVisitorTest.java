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
package com.mysema.query.jpa;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.mysema.query.jpa.domain.JobFunction;
import com.mysema.query.jpa.domain.QCat;
import com.mysema.query.jpa.domain.QDomesticCat;
import com.mysema.query.jpa.domain.QEmployee;
import com.mysema.query.support.Context;
import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.TemplateExpressionImpl;


public class JPACollectionAnyVisitorTest {

    private QCat cat = QCat.cat;

    @Test
    public void Path() {
        assertMatches("cat_kittens.*", serialize(cat.kittens.any()));
    }

    @Test
    public void Longer_Path() {
        assertMatches("cat_kittens.*\\.name", serialize(cat.kittens.any().name));
    }

    @Test
    public void Simple_BooleanOperation() {
        Predicate predicate = cat.kittens.any().name.eq("Ruth123");
        assertMatches("exists \\(select 1\n" +
                "from Cat cat_kittens.*\n" +
                "where cat_kittens.* in elements\\(cat\\.kittens\\) and cat_kittens.*\\.name = \\?1\\)", serialize(predicate));
    }

    @Test
    public void Simple_BooleanOperation_ElementCollection() {
        QEmployee employee = QEmployee.employee;
        Predicate predicate = employee.jobFunctions.any().eq(JobFunction.CODER);
        assertMatches("exists \\(select 1\n" +
        	"from Employee employee.*\n" +
        	"  inner join employee.*.jobFunctions as employee_jobFunctions.*\n" +
        	"where employee.* = employee and employee_jobFunctions.* = \\?1\\)", serialize(predicate));
    }

    @Test
    public void Simple_StringOperation() {
        Predicate predicate = cat.kittens.any().name.substring(1).eq("uth123");
        assertMatches("exists \\(select 1\n"+
                "from Cat cat_kittens.*\n" +
                "where cat_kittens.* in elements\\(cat.kittens\\) and substring\\(cat_kittens.*\\.name,2\\) = \\?1\\)", serialize(predicate));
    }

    @Test
    public void And_Operation() {
        Predicate predicate = cat.kittens.any().name.eq("Ruth123").and(cat.kittens.any().bodyWeight.gt(10.0));
        assertMatches("exists \\(select 1\n"+
                "from Cat cat_kittens.*\n" +
                "where cat_kittens.* in elements\\(cat.kittens\\) and cat_kittens.*\\.name = \\?1\\) and exists \\(select 1\n" +
                "from Cat cat_kittens.*\n" +
                "where cat_kittens.* in elements\\(cat.kittens\\) and cat_kittens.*\\.bodyWeight > \\?2\\)", serialize(predicate));
    }

    @Test
    public void Template() {
        Expression<Boolean> templateExpr = TemplateExpressionImpl.create(Boolean.class, "{0} = {1}",
                cat.kittens.any().name, ConstantImpl.create("Ruth123"));
        assertMatches("exists \\(select 1\n" +
                "from Cat cat_kittens.*\n" +
                "where cat_kittens.* in elements\\(cat\\.kittens\\) and cat_kittens.*\\.name = \\?1\\)", serialize(templateExpr));
    }

    @Test
    public void Cast() {
//        JPAQuery query = new JPAQuery(em).from(QPerson.person);
//        QDog anyDog = QPerson.person.animals.any().as(QDog.class);
//        query.where(anyDog.gender.eq("M"));
//        List<Person> foundOwners = query.list(QPerson.person);

        QDomesticCat anyCat = QCat.cat.kittens.any().as(QDomesticCat.class);
        Predicate predicate = anyCat.name.eq("X");

        assertMatches("exists \\(select 1\n" +
            "from DomesticCat cat_kittens.*\n" +
            "where cat_kittens.* in elements\\(cat.kittens\\) and cat_kittens.*\\.name = \\?1\\)", serialize(predicate));
    }

    private String serialize(Expression<?> expression) {
        Expression<?> transformed = expression.accept(JPACollectionAnyVisitor.DEFAULT, new Context());
        JPQLSerializer serializer = new JPQLSerializer(HQLTemplates.DEFAULT, null);
        serializer.handle(transformed);
        return serializer.toString();
    }

    private static void assertMatches(String str1, String str2) {
        assertTrue(str2, str2.matches(str1));
    }
}
