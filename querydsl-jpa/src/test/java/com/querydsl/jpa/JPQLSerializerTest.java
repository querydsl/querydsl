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

import static com.querydsl.jpa.JPAExpressions.selectOne;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.JoinType;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.domain.QAnimal;
import com.querydsl.core.domain.QCat;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.domain.JobFunction;
import com.querydsl.jpa.domain.Location;
import com.querydsl.jpa.domain.QDomesticCat;
import com.querydsl.jpa.domain.QEmployee;

public class JPQLSerializerTest {

    @Test
    public void and_or() {
        //A.a.id.eq(theId).and(B.b.on.eq(false).or(B.b.id.eq(otherId)));
        QCat cat = QCat.cat;
        Predicate pred = cat.id.eq(1).and(cat.name.eq("Kitty").or(cat.name.eq("Boris")));
        JPQLSerializer serializer = new JPQLSerializer(HQLTemplates.DEFAULT);
        serializer.handle(pred);
        assertEquals("cat.id = ?1 and (cat.name = ?2 or cat.name = ?3)", serializer.toString());
        assertEquals("cat.id = 1 && (cat.name = Kitty || cat.name = Boris)", pred.toString());
    }

    @Test
    public void case1() {
        QCat cat = QCat.cat;
        JPQLSerializer serializer = new JPQLSerializer(JPQLTemplates.DEFAULT);
        Expression<?> expr = Expressions.cases().when(cat.toes.eq(2)).then(2)
                .when(cat.toes.eq(3)).then(3)
                .otherwise(4);
        serializer.handle(expr);
        assertEquals("case when (cat.toes = ?1) then ?2 when (cat.toes = ?3) then ?4 else ?5 end", serializer.toString());
    }

    @Test
    public void case1_hibernate() {
        QCat cat = QCat.cat;
        JPQLSerializer serializer = new JPQLSerializer(HQLTemplates.DEFAULT);
        Expression<?> expr = Expressions.cases().when(cat.toes.eq(2)).then(2)
                .when(cat.toes.eq(3)).then(3)
                .otherwise(4);
        serializer.handle(expr);
        assertEquals("case when (cat.toes = ?1) then ?2 when (cat.toes = ?3) then ?4 else 4 end", serializer.toString());
    }

    @Test
    public void case2() {
        QCat cat = QCat.cat;
        JPQLSerializer serializer = new JPQLSerializer(JPQLTemplates.DEFAULT);
        Expression<?> expr = Expressions.cases().when(cat.toes.eq(2)).then(cat.id.multiply(2))
                .when(cat.toes.eq(3)).then(cat.id.multiply(3))
                .otherwise(4);
        serializer.handle(expr);
        assertEquals("case when (cat.toes = ?1) then (cat.id * ?2) when (cat.toes = ?3) then (cat.id * ?4) else ?5 end", serializer.toString());
    }

    @Test
    public void case2_hibernate() {
        QCat cat = QCat.cat;
        JPQLSerializer serializer = new JPQLSerializer(HQLTemplates.DEFAULT);
        Expression<?> expr = Expressions.cases().when(cat.toes.eq(2)).then(cat.id.multiply(2))
                .when(cat.toes.eq(3)).then(cat.id.multiply(3))
                .otherwise(4);
        serializer.handle(expr);
        assertEquals("case when (cat.toes = ?1) then (cat.id * ?2) when (cat.toes = ?3) then (cat.id * ?4) else 4 end", serializer.toString());
    }

    @Test
    public void count() {
        QCat cat = QCat.cat;
        QueryMetadata md = new DefaultQueryMetadata();
        md.addJoin(JoinType.DEFAULT, cat);
        md.setProjection(cat.mate.countDistinct());
        JPQLSerializer serializer1 = new JPQLSerializer(HQLTemplates.DEFAULT);
        serializer1.serialize(md, true, null);
        assertEquals("select count(count(distinct cat.mate))\n" +
                "from Cat cat",  serializer1.toString());

        JPQLSerializer serializer2 = new JPQLSerializer(HQLTemplates.DEFAULT);
        serializer2.serialize(md, false, null);
        assertEquals("select count(distinct cat.mate)\n" +
                "from Cat cat", serializer2.toString());
    }

    @Test
    public void fromWithCustomEntityName() {
        JPQLSerializer serializer = new JPQLSerializer(HQLTemplates.DEFAULT);
        EntityPath<Location> entityPath = new EntityPathBase<Location>(Location.class, "entity");
        QueryMetadata md = new DefaultQueryMetadata();
        md.addJoin(JoinType.DEFAULT, entityPath);
        serializer.serialize(md, false, null);
        assertEquals("select entity\nfrom Location2 entity", serializer.toString());
    }

    @Test
    public void join_with() {
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
    public void normalizeNumericArgs() {
        JPQLSerializer serializer = new JPQLSerializer(HQLTemplates.DEFAULT);
        NumberPath<Double> doublePath = Expressions.numberPath(Double.class, "doublePath");
        serializer.handle(doublePath.add(1));
        serializer.handle(doublePath.between((float) 1.0, 1L));
        serializer.handle(doublePath.lt((byte) 1));
        for (Object constant : serializer.getConstants()) {
            assertEquals(Double.class, constant.getClass());
        }
    }

    @Test
    public void delete_clause_uses_dELETE_fROM() {
        QEmployee employee = QEmployee.employee;
        JPQLSerializer serializer = new JPQLSerializer(HQLTemplates.DEFAULT);
        QueryMetadata md = new DefaultQueryMetadata();
        md.addJoin(JoinType.DEFAULT, employee);
        md.addWhere(employee.lastName.isNull());
        serializer.serializeForDelete(md);
        assertEquals("delete from Employee employee\nwhere employee.lastName is null", serializer.toString());
    }

    @Test
    public void delete_with_subQuery() {
        QCat parent = QCat.cat;
        QCat child = new QCat("kitten");

        JPQLSerializer serializer = new JPQLSerializer(HQLTemplates.DEFAULT);
        QueryMetadata md = new DefaultQueryMetadata();
        md.addJoin(JoinType.DEFAULT, child);
        md.addWhere(
            child.id.eq(1)
            .and(selectOne()
                .from(parent)
                .where(parent.id.eq(2), child.in(parent.kittens)).exists()));
        serializer.serializeForDelete(md);
        assertEquals("delete from Cat kitten\n" +
                "where kitten.id = ?1 and exists (select 1\n" +
            "from Cat cat\nwhere cat.id = ?2 and kitten member of cat.kittens)", serializer.toString());
    }

    @Test
    public void in() {
        JPQLSerializer serializer = new JPQLSerializer(HQLTemplates.DEFAULT);
        serializer.handle(Expressions.numberPath(Integer.class, "id").in(Arrays.asList(1, 2)));
        assertEquals("id in (?1)", serializer.toString());
    }

    @Test
    public void not_in() {
        JPQLSerializer serializer = new JPQLSerializer(HQLTemplates.DEFAULT);
        serializer.handle(Expressions.numberPath(Integer.class, "id").notIn(Arrays.asList(1, 2)));
        assertEquals("id not in (?1)", serializer.toString());
    }

    @Test
    public void like() {
        JPQLSerializer serializer = new JPQLSerializer(HQLTemplates.DEFAULT);
        serializer.handle(Expressions.stringPath("str").contains("abc!"));
        assertEquals("str like ?1 escape '!'", serializer.toString());
        assertEquals("%abc!!%", serializer.getConstants().get(0).toString());
    }

    @Test
    public void stringContainsIc() {
        JPQLSerializer serializer = new JPQLSerializer(HQLTemplates.DEFAULT);
        serializer.handle(Expressions.stringPath("str").containsIgnoreCase("ABc!"));
        assertEquals("lower(str) like ?1 escape '!'", serializer.toString());
        assertEquals("%abc!!%", serializer.getConstants().get(0).toString());
    }

    @Test
    public void substring() {
        JPQLSerializer serializer = new JPQLSerializer(HQLTemplates.DEFAULT);
        QCat cat = QCat.cat;
        serializer.handle(cat.name.substring(cat.name.length().subtract(1), 1));
        assertEquals("substring(cat.name,length(cat.name) + ?1,?2 - (length(cat.name) - ?3))", serializer.toString());
    }

    @Test
    public void nullsFirst() {
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
    public void nullsLast() {
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

    @SuppressWarnings("unchecked")
    @Test
    public void treat() {
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
    public void treated_path() {
        QAnimal animal = QAnimal.animal;
        JPQLSerializer serializer = new JPQLSerializer(HQLTemplates.DEFAULT);
        QueryMetadata md = new DefaultQueryMetadata();
        md.addJoin(JoinType.DEFAULT, animal);

        md.addWhere(JPAExpressions.treat(animal, QCat.class).breed.eq(1));
        md.setProjection(animal);
        serializer.serialize(md, false, null);
        assertEquals("select animal\n" +
                "from Animal animal\n" +
                "where treat(animal as Cat).breed = ?1", serializer.toString());
    }

    @Test
    public void openJPA_variables() {
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

    @Test
    public void substring_indexOf() {
        QCat cat = QCat.cat;
        JPQLSerializer serializer = new JPQLSerializer(HQLTemplates.DEFAULT);
        cat.name.substring(cat.name.indexOf("")).accept(serializer, null);
        assertEquals("substring(cat.name,locate(?1,cat.name)-1 + ?2)", serializer.toString());
    }

    @Test
    public void visit_subQueryExpression_with_limit_jpql() {
        QCat cat = QCat.cat;
        JPQLQuery<Integer> subQuery = JPAExpressions.select(cat.id).from(cat).limit(5);
        JPQLSerializer serializer = new JPQLSerializer(JPQLTemplates.DEFAULT);
        serializer.visit(subQuery, null);

        assertEquals("(select cat.id\nfrom Cat cat)", serializer.toString());
        assertEquals(0, serializer.getConstants().size());
    }

    @Test
    public void visit_subQueryExpression_with_limit_hql() {
        QCat cat = QCat.cat;
        JPQLQuery<Integer> subQuery = JPAExpressions.select(cat.id).from(cat).limit(5);
        JPQLSerializer serializer = new JPQLSerializer(HQLTemplates.DEFAULT);
        serializer.visit(subQuery, null);

        assertEquals("(select cat.id\nfrom Cat cat\nlimit ?1)", serializer.toString());
        assertEquals(1, serializer.getConstants().size());
        assertEquals(5L, serializer.getConstants().get(0));
    }

    @Test
    public void visit_subQueryExpression_with_offset_jpql() {
        QCat cat = QCat.cat;
        JPQLQuery<Integer> subQuery = JPAExpressions.select(cat.id).from(cat).offset(10);
        JPQLSerializer serializer = new JPQLSerializer(JPQLTemplates.DEFAULT);
        serializer.visit(subQuery, null);

        assertEquals("(select cat.id\nfrom Cat cat)", serializer.toString());
        assertEquals(0, serializer.getConstants().size());
    }

    @Test
    public void visit_subQueryExpression_with_offset_hql() {
        QCat cat = QCat.cat;
        JPQLQuery<Integer> subQuery = JPAExpressions.select(cat.id).from(cat).offset(10);
        JPQLSerializer serializer = new JPQLSerializer(HQLTemplates.DEFAULT);
        serializer.visit(subQuery, null);

        assertEquals("(select cat.id\nfrom Cat cat\noffset ?1)", serializer.toString());
        assertEquals(1, serializer.getConstants().size());
        assertEquals(10L, serializer.getConstants().get(0));
    }

    @Test
    public void visit_subQueryExpression_with_limit_and_offset_jpql() {
        QCat cat = QCat.cat;
        JPQLQuery<Integer> subQuery = JPAExpressions.select(cat.id).from(cat).limit(5).offset(10);
        JPQLSerializer serializer = new JPQLSerializer(JPQLTemplates.DEFAULT);
        serializer.visit(subQuery, null);

        assertEquals("(select cat.id\nfrom Cat cat)", serializer.toString());
        assertEquals(0, serializer.getConstants().size());
    }

    @Test
    public void visit_subQueryExpression_with_limit_and_offset_hql() {
        QCat cat = QCat.cat;
        JPQLQuery<Integer> subQuery = JPAExpressions.select(cat.id).from(cat).limit(5).offset(10);
        JPQLSerializer serializer = new JPQLSerializer(HQLTemplates.DEFAULT);
        serializer.visit(subQuery, null);

        assertEquals("(select cat.id\nfrom Cat cat\nlimit ?1\noffset ?2)", serializer.toString());
        assertEquals(2, serializer.getConstants().size());
        assertEquals(5L, serializer.getConstants().get(0));
        assertEquals(10L, serializer.getConstants().get(1));
    }

    @Test
    public void visit_nested_subQueryExpression_with_modifiers_hql() {
        QCat cat = QCat.cat;
        QCat mate = new QCat("mate");

        JPQLQuery<Integer> innerSubQuery = JPAExpressions.select(mate.id).from(mate).limit(3);

        JPQLQuery<Integer> outerSubQuery =
            JPAExpressions.select(cat.id).from(cat).where(cat.id.in(innerSubQuery)).limit(5);

        JPQLSerializer serializer = new JPQLSerializer(HQLTemplates.DEFAULT);
        serializer.visit(outerSubQuery, null);

        assertEquals(
                "(select cat.id\n" +
                "from Cat cat\n" +
                "where cat.id in (select mate.id\n" +
                "from Cat mate\n" +
                "limit ?1)\n" +
                "limit ?2)", serializer.toString());
        assertEquals(2, serializer.getConstants().size());
        assertEquals(3L, serializer.getConstants().get(0));
        assertEquals(5L, serializer.getConstants().get(1));
    }

    @Test
    public void subquery_in_where_clause_with_modifiers_jpql() {
        QCat cat = QCat.cat;
        QCat kitten = new QCat("kitten");

        QueryMetadata mainQuery = new DefaultQueryMetadata();
        mainQuery.addJoin(JoinType.DEFAULT, cat);

        SubQueryExpression<Integer> subQuery =
            JPAExpressions.select(kitten.id)
                .from(kitten)
                .where(kitten.name.isNotNull())
                .limit(5)
                .offset(10);

        mainQuery.addWhere(cat.id.in(subQuery));
        mainQuery.setProjection(cat);

        JPQLSerializer jpqlSerializer = new JPQLSerializer(JPQLTemplates.DEFAULT);
        jpqlSerializer.serialize(mainQuery, false, null);

        assertEquals(
                "select cat\n" +
                "from Cat cat\n" +
                "where cat.id in (select kitten.id\n" +
                "from Cat kitten\n" +
                "where kitten.name is not null)", jpqlSerializer.toString());
        assertEquals(0, jpqlSerializer.getConstants().size());
    }

    @Test
    public void subquery_in_where_clause_with_modifiers_hql() {
        QCat cat = QCat.cat;
        QCat kitten = new QCat("kitten");

        QueryMetadata mainQuery = new DefaultQueryMetadata();
        mainQuery.addJoin(JoinType.DEFAULT, cat);

        SubQueryExpression<Integer> subQuery =
            JPAExpressions.select(kitten.id)
                .from(kitten)
                .where(kitten.name.isNotNull())
                .limit(5)
                .offset(10);

        mainQuery.addWhere(cat.id.in(subQuery));
        mainQuery.setProjection(cat);

        JPQLSerializer hqlSerializer = new JPQLSerializer(HQLTemplates.DEFAULT);
        hqlSerializer.serialize(mainQuery, false, null);

        assertEquals(
                "select cat\n" +
                "from Cat cat\n" +
                "where cat.id in (select kitten.id\n" +
                "from Cat kitten\n" +
                "where kitten.name is not null\n" +
                "limit ?1\n" +
                "offset ?2)", hqlSerializer.toString());
        assertEquals(2, hqlSerializer.getConstants().size());
        assertEquals(5L, hqlSerializer.getConstants().get(0));
        assertEquals(10L, hqlSerializer.getConstants().get(1));
    }

    @Test
    public void subquery_in_select_clause_with_modifiers_jpql() {
        QCat cat = QCat.cat;
        QCat mate = new QCat("mate");

        QueryMetadata mainQuery = new DefaultQueryMetadata();
        mainQuery.addJoin(JoinType.DEFAULT, cat);

        SubQueryExpression<Integer> subQuery =
            JPAExpressions.select(mate.weight.max())
                .from(mate)
                .where(mate.name.eq(cat.name))
                .limit(1)
                .offset(2);

        mainQuery.setProjection(subQuery);

        JPQLSerializer jpqlSerializer = new JPQLSerializer(JPQLTemplates.DEFAULT);
        jpqlSerializer.serialize(mainQuery, false, null);

        assertEquals(
                "select (select max(mate.weight)\n" +
                "from Cat mate\n" +
                "where mate.name = cat.name)\n" +
                "from Cat cat", jpqlSerializer.toString());
        assertEquals(0, jpqlSerializer.getConstants().size());
    }

    @Test
    public void subquery_in_select_clause_with_modifiers_hql() {
        QCat cat = QCat.cat;
        QCat mate = new QCat("mate");

        QueryMetadata mainQuery = new DefaultQueryMetadata();
        mainQuery.addJoin(JoinType.DEFAULT, cat);

        SubQueryExpression<Integer> subQuery =
            JPAExpressions.select(mate.weight.max())
                .from(mate)
                .where(mate.name.eq(cat.name))
                .limit(1)
                .offset(2);

        mainQuery.setProjection(subQuery);

        JPQLSerializer hqlSerializer = new JPQLSerializer(HQLTemplates.DEFAULT);
        hqlSerializer.serialize(mainQuery, false, null);

        assertEquals(
                "select (select max(mate.weight)\n" +
                "from Cat mate\n" +
                "where mate.name = cat.name\n" +
                "limit ?1\n" +
                "offset ?2)\n" +
                "from Cat cat", hqlSerializer.toString());
        assertEquals(2, hqlSerializer.getConstants().size());
        assertEquals(1L, hqlSerializer.getConstants().get(0));
        assertEquals(2L, hqlSerializer.getConstants().get(1));
    }
}
