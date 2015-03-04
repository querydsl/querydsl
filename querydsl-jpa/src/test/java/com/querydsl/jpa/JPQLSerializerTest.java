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

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.JoinType;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.domain.QCat;
import com.querydsl.core.support.Expressions;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.path.EntityPathBase;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.StringPath;
import com.querydsl.jpa.domain.JobFunction;
import com.querydsl.jpa.domain.Location;
import com.querydsl.jpa.domain.QDomesticCat;
import com.querydsl.jpa.domain.QEmployee;

public class JPQLSerializerTest {

    @Test
    public void And_Or() {
        //A.a.id.eq(theId).and(B.b.on.eq(false).or(B.b.id.eq(otherId)));
        QCat cat = QCat.cat;
        Predicate pred = cat.id.eq(1).and(cat.name.eq("Kitty").or(cat.name.eq("Boris")));
        JPQLSerializer serializer = new JPQLSerializer(HQLTemplates.DEFAULT);
        serializer.handle(pred);
        assertEquals("cat.id = ?1 and (cat.name = ?2 or cat.name = ?3)", serializer.toString());
        assertEquals("cat.id = 1 && (cat.name = Kitty || cat.name = Boris)", pred.toString());
    }

    @Test
    public void Case() {
        QCat cat = QCat.cat;
        JPQLSerializer serializer = new JPQLSerializer(JPQLTemplates.DEFAULT);
        Expression<?> expr = Expressions.cases().when(cat.toes.eq(2)).then(2)
                .when(cat.toes.eq(3)).then(3)
                .otherwise(4);
        serializer.handle(expr);
        assertEquals("case when (cat.toes = ?1) then ?1 when (cat.toes = ?2) then ?2 else ?3 end", serializer.toString());
    }

    @Test
    public void Case_Hibernate() {
        QCat cat = QCat.cat;
        JPQLSerializer serializer = new JPQLSerializer(HQLTemplates.DEFAULT);
        Expression<?> expr = Expressions.cases().when(cat.toes.eq(2)).then(2)
                .when(cat.toes.eq(3)).then(3)
                .otherwise(4);
        serializer.handle(expr);
        assertEquals("case when (cat.toes = 2) then 2 when (cat.toes = 3) then 3 else 4 end", serializer.toString());
    }

    @Test
    public void Case2() {
        QCat cat = QCat.cat;
        JPQLSerializer serializer = new JPQLSerializer(JPQLTemplates.DEFAULT);
        Expression<?> expr = Expressions.cases().when(cat.toes.eq(2)).then(cat.id.multiply(2))
                .when(cat.toes.eq(3)).then(cat.id.multiply(3))
                .otherwise(4);
        serializer.handle(expr);
        assertEquals("case when (cat.toes = ?1) then (cat.id * ?1) when (cat.toes = ?2) then (cat.id * ?2) else ?3 end", serializer.toString());
    }

    @Test
    public void Case2_Hibernate() {
        QCat cat = QCat.cat;
        JPQLSerializer serializer = new JPQLSerializer(HQLTemplates.DEFAULT);
        Expression<?> expr = Expressions.cases().when(cat.toes.eq(2)).then(cat.id.multiply(2))
                .when(cat.toes.eq(3)).then(cat.id.multiply(3))
                .otherwise(4);
        serializer.handle(expr);
        assertEquals("case when (cat.toes = 2) then (cat.id * 2) when (cat.toes = 3) then (cat.id * 3) else 4 end", serializer.toString());
    }

    @Test
    public void FromWithCustomEntityName() {
        JPQLSerializer serializer = new JPQLSerializer(HQLTemplates.DEFAULT);
        EntityPath<Location> entityPath = new EntityPathBase<Location>(Location.class, "entity");
        QueryMetadata md = new DefaultQueryMetadata();
        md.addJoin(JoinType.DEFAULT, entityPath);
        serializer.serialize(md, false, null);
        assertEquals("select entity\nfrom Location2 entity", serializer.toString());
    }

    @Test
    public void Join_With() {
        QCat cat = QCat.cat;
        JPQLSerializer serializer = new JPQLSerializer(HQLTemplates.DEFAULT);
        QueryMetadata md = new DefaultQueryMetadata();
        md.addJoin(JoinType.DEFAULT, cat);
        md.addJoin(JoinType.INNERJOIN, cat.mate);
        md.addJoinCondition(cat.mate.alive);
        serializer.serialize(md, false, null);
        assertEquals("select cat\nfrom Cat cat\n  inner join cat.mate with cat.mate.alive", serializer.toString());
    }

    @Test
    public void NormalizeNumericArgs() {
        JPQLSerializer serializer = new JPQLSerializer(HQLTemplates.DEFAULT);
        NumberPath<Double> doublePath = new NumberPath<Double>(Double.class, "doublePath");
        serializer.handle(doublePath.add(1));
        serializer.handle(doublePath.between((float)1.0, 1l));
        serializer.handle(doublePath.lt((byte)1));
        for (Object constant : serializer.getConstantToLabel().keySet()) {
            assertEquals(Double.class, constant.getClass());
        }
    }

    @Test
    public void Delete_Clause_Uses_DELETE_FROM() {
        QEmployee employee = QEmployee.employee;
        JPQLSerializer serializer = new JPQLSerializer(HQLTemplates.DEFAULT);
        QueryMetadata md = new DefaultQueryMetadata();
        md.addJoin(JoinType.DEFAULT, employee);
        md.addWhere(employee.lastName.isNull());
        serializer.serializeForDelete(md);
        assertEquals("delete from Employee employee\nwhere employee.lastName is null", serializer.toString());
    }

    @Test
    public void Delete_With_SubQuery() {
        QCat parent = QCat.cat;
        QCat child = new QCat("kitten");

        JPQLSerializer serializer = new JPQLSerializer(HQLTemplates.DEFAULT);
        QueryMetadata md = new DefaultQueryMetadata();
        md.addJoin(JoinType.DEFAULT, child);
        md.addWhere(
            child.id.eq(1)
            .and(new JPASubQuery()
                .from(parent)
                .where(parent.id.eq(2), child.in(parent.kittens)).exists()));
        serializer.serializeForDelete(md);
        assertEquals("delete from Cat kitten\n" +
                "where kitten.id = ?1 and exists (select 1\n" +
        	"from Cat cat\nwhere cat.id = ?2 and kitten member of cat.kittens)", serializer.toString());
    }

    @Test
    public void In() {
        JPQLSerializer serializer = new JPQLSerializer(HQLTemplates.DEFAULT);
        serializer.handle(new NumberPath<Integer>(Integer.class, "id").in(Arrays.asList(1, 2)));
        assertEquals("id in (?1)", serializer.toString());
    }

    @Test
    public void Not_In() {
        JPQLSerializer serializer = new JPQLSerializer(HQLTemplates.DEFAULT);
        serializer.handle(new NumberPath<Integer>(Integer.class, "id").notIn(Arrays.asList(1, 2)));
        assertEquals("id not in (?1)", serializer.toString());
    }

    @Test
    public void Like() {
        JPQLSerializer serializer = new JPQLSerializer(HQLTemplates.DEFAULT);
        serializer.handle(new StringPath("str").contains("abc!"));
        assertEquals("str like ?1 escape '!'", serializer.toString());
        assertEquals("%abc!!%", serializer.getConstantToLabel().keySet().iterator().next().toString());
    }

    @Test
    public void StringContainsIc() {
        JPQLSerializer serializer = new JPQLSerializer(HQLTemplates.DEFAULT);
        serializer.handle(new StringPath("str").containsIgnoreCase("ABc!"));
        assertEquals("lower(str) like ?1 escape '!'", serializer.toString());
        assertEquals("%abc!!%", serializer.getConstantToLabel().keySet().iterator().next().toString());
    }

    @Test
    public void Substring() {
        JPQLSerializer serializer = new JPQLSerializer(HQLTemplates.DEFAULT);
        QCat cat = QCat.cat;
        serializer.handle(cat.name.substring(cat.name.length().subtract(1), 1));
        assertEquals("substring(cat.name,(length(cat.name) - ?1)+1,1-(length(cat.name) - ?1))", serializer.toString());
    }

    @Test
    public void NullsFirst() {
        QCat cat = QCat.cat;
        JPQLSerializer serializer = new JPQLSerializer(HQLTemplates.DEFAULT);
        QueryMetadata md = new DefaultQueryMetadata();
        md.addJoin(JoinType.DEFAULT, cat);
        md.addOrderBy(cat.name.asc().nullsFirst());
        serializer.serialize(md, false, null);
        assertEquals("select cat\n" +
        	     "from Cat cat\n" +
        	     "order by cat.name asc nulls first", serializer.toString());
    }

    @Test
    public void NullsLast() {
        QCat cat = QCat.cat;
        JPQLSerializer serializer = new JPQLSerializer(HQLTemplates.DEFAULT);
        QueryMetadata md = new DefaultQueryMetadata();
        md.addJoin(JoinType.DEFAULT, cat);
        md.addOrderBy(cat.name.asc().nullsLast());
        serializer.serialize(md, false, null);
        assertEquals("select cat\n" +
                     "from Cat cat\n" +
                     "order by cat.name asc nulls last", serializer.toString());
    }

    @Test
    public void Treat() {
        QCat cat = QCat.cat;
        JPQLSerializer serializer = new JPQLSerializer(HQLTemplates.DEFAULT);
        QueryMetadata md = new DefaultQueryMetadata();
        md.addJoin(JoinType.DEFAULT, cat);
        md.addJoin(JoinType.JOIN, cat.mate.as((Path) QDomesticCat.domesticCat));
        md.setProjection(QDomesticCat.domesticCat);
        serializer.serialize(md, false, null);
        assertEquals("select domesticCat\n" +
                "from Cat cat\n" +
                "  inner join treat(cat.mate as DomesticCat) as domesticCat", serializer.toString());
    }

    @Test
    public void OpenJPA_Variables() {
        QCat cat = QCat.cat;
        JPQLSerializer serializer = new JPQLSerializer(OpenJPATemplates.DEFAULT);
        QueryMetadata md = new DefaultQueryMetadata();
        md.addJoin(JoinType.DEFAULT, cat);
        md.addJoin(JoinType.INNERJOIN, cat.mate);
        md.addJoinCondition(cat.mate.alive);
        serializer.serialize(md, false, null);
        assertEquals("select cat_\nfrom Cat cat_\n  inner join cat_.mate on cat_.mate.alive",
                serializer.toString());
    }

    @Test
    public void visitLiteral_boolean() {
        JPQLSerializer serializer = new JPQLSerializer(HQLTemplates.DEFAULT);
        serializer.visitLiteral(Boolean.TRUE);
        assertEquals("true", serializer.toString());
    }

    @Test
    public void visitLiteral_number() {
        JPQLSerializer serializer = new JPQLSerializer(HQLTemplates.DEFAULT);
        serializer.visitLiteral(1.543);
        assertEquals("1.543", serializer.toString());
    }

    @Test
    public void visitLiteral_string() {
        JPQLSerializer serializer = new JPQLSerializer(HQLTemplates.DEFAULT);
        serializer.visitLiteral("abc''def");
        assertEquals("'abc''''def'", serializer.toString());
    }

    @Test
    public void visitLiteral_enum() {
        JPQLSerializer serializer = new JPQLSerializer(HQLTemplates.DEFAULT);
        serializer.visitLiteral(JobFunction.MANAGER);
        assertEquals("com.querydsl.jpa.domain.JobFunction.MANAGER", serializer.toString());
    }
}
