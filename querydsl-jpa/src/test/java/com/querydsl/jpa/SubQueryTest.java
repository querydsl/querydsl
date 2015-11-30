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
import static com.querydsl.jpa.JPAExpressions.*;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.querydsl.core.domain.QCat;
import com.querydsl.jpa.domain.QEmployee;
import com.querydsl.jpa.domain.QUser;

public class SubQueryTest extends AbstractQueryTest {

    @Test
    public void single_source() {
        JPQLQuery<?> query = selectFrom(cat);
        assertEquals("select cat\nfrom Cat cat", query.toString());
    }

    @Test
    public void multiple_sources() {
        JPQLQuery<?> query = select(cat).from(cat, fatcat);
        assertEquals("select cat\nfrom Cat cat, Cat fatcat",
                query.toString());
    }

    @Test
    public void in() {
        cat.in(selectFrom(cat));
    }

    @Test
    public void innerJoin() {
        assertEquals("select cat\nfrom Cat cat\n  inner join cat.mate",
                selectFrom(cat).innerJoin(cat.mate).toString());
    }

    @Test
    public void innerJoin2() {
        QEmployee employee = QEmployee.employee;
        QUser user = QUser.user;
        assertEquals("select employee\nfrom Employee employee\n  inner join employee.user as user",
                selectFrom(employee).innerJoin(employee.user, user).toString());
    }

    @Test
    public void leftJoin() {
        assertEquals("select cat\nfrom Cat cat\n  left join cat.mate",
                selectFrom(cat).leftJoin(cat.mate).toString());
    }

    @Test
    public void join() {
        assertEquals("select cat\nfrom Cat cat\n  inner join cat.mate",
                selectFrom(cat).join(cat.mate).toString());
    }

    @Test
    public void uniqueProjection() {
        assertToString("(select cat from Cat cat)",
                selectFrom(cat));
    }

    @Test
    public void listProjection() {
        assertToString("(select cat from Cat cat)",
                selectFrom(cat));
    }

    @Test
    public void listContains() {
        assertToString("cat in (select cat from Cat cat)",
                cat.in(selectFrom(cat)));
    }

    @Test
    public void exists() {
        assertToString("exists (select 1 from Cat cat)",
                selectOne().from(cat).exists());
    }

    @Test
    public void exists_where() {
        assertToString("exists (select 1 from Cat cat where cat.weight < ?1)",
                selectFrom(cat).where(cat.weight.lt(1)).exists());
    }

    @Test
    public void exists_via_unique() {
        assertToString("exists (select 1 from Cat cat where cat.weight < ?1)",
                selectOne().from(cat).where(cat.weight.lt(1)).exists());
    }

    @Test
    public void notExists() {
        assertToString("not exists (select 1 from Cat cat)",
                selectOne().from(cat).notExists());
    }

    @Test
    public void notExists_where() {
        assertToString("not exists (select 1 from Cat cat where cat.weight < ?1)",
                selectOne().from(cat).where(cat.weight.lt(1)).notExists());
    }

    @Test
    public void notExists_via_unique() {
        assertToString("not exists (select 1 from Cat cat where cat.weight < ?1)",
                selectOne().from(cat).where(cat.weight.lt(1)).notExists());
    }

    @Test
    public void count() {
        assertToString("(select count(cat) from Cat cat)",
                select(cat.count()).from(cat));
    }

    @Test
    public void count_via_list() {
        assertToString("(select count(cat) from Cat cat)",
                select(cat.count()).from(cat));
    }

    @Test
    public void count_name() {
        assertToString("(select count(cat.name) from Cat cat)",
                select(cat.name.count()).from(cat));
    }

    @Test
    public void count_multiple_sources() {
        QCat other = new QCat("other");
        assertToString("(select count(cat) from Cat cat, Cat other)",

                select(cat.count()).from(cat, other));
    }

    @Test
    public void count_multiple_sources_via_list() {
        QCat other = new QCat("other");
        assertToString("(select count(cat) from Cat cat, Cat other)",

                select(cat.count()).from(cat, other));
    }

    @Test
    public void indexed_access() {
        assertMatches("\\(select count\\(cat\\) from Cat cat   " +
                "left join cat.kittens as cat_kittens_\\w+ " +
                "with index\\(cat_kittens_\\w+\\) = \\?1 where cat_kittens_\\w+.name = \\?2\\)",

                select(cat.count()).from(cat).where(cat.kittens.get(0).name.eq("Kate")));
    }

    @Test
    public void indexed_access_without_constant() {
        assertMatches("\\(select count\\(cat\\) from Cat cat   " +
                        "left join cat.kittens as cat_kittens_\\w+ " +
                        "with index\\(cat_kittens_\\w+\\) = cat.id where cat_kittens_\\w+.name = \\?1\\)",

                select(cat.count()).from(cat).where(cat.kittens.get(cat.id).name.eq("Kate")));
    }

    @Test
    public void indexOf() {
        assertToString("(select count(cat) from Cat cat where locate(?1,cat.name)-1 = ?2)",
                select(cat.count()).from(cat).where(cat.name.indexOf("a").eq(1)));
    }

//    @Test
//    public void orderBy() {
//        JPQLQuery<Void> query = query().from(cat1).where(cat1.alive);
//        SubQueryExpression<Double> subquery = sub().from(cat).where(cat.mate.id.eq(cat1.id)).select(cat.floatProperty.avg());
//        query.orderBy(subquery.subtract(-1.0f).asc());
//
//        assertEquals("select cat1 from Cat cat1 where cat1.alive order by (select avg(cat.floatProperty) from Cat cat where cat.mate.id = cat1.id) - ?1 asc",
//                query.toString().replace("\n", " "));
//    }

}
