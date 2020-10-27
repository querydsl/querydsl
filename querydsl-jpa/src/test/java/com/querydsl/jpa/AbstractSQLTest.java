package com.querydsl.jpa;

import static com.querydsl.sql.SQLExpressions.select;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Target;
import com.querydsl.core.Tuple;
import com.querydsl.core.testutil.ExcludeIn;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.dsl.DateExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.domain.Cat;
import com.querydsl.jpa.domain.Color;
import com.querydsl.jpa.domain.QCat;
import com.querydsl.jpa.domain.QCompany;
import com.querydsl.jpa.domain.sql.SAnimal;

public abstract class AbstractSQLTest {

    protected static final SAnimal cat = new SAnimal("cat");

    protected abstract AbstractSQLQuery<?,?> query();

    public static class CatDTO {

        Cat cat;

        public CatDTO(Cat cat) {
            this.cat = cat;
        }

    }

    @Test
    public void count() {
        assertEquals(6L, query().from(cat).where(cat.dtype.eq("C")).fetchCount());
    }

    @Test
    public void count_via_unique() {
        assertEquals(Long.valueOf(6), query().from(cat).where(cat.dtype.eq("C"))
                .select(cat.id.count()).fetchFirst().orElse(null));
    }

    @Test
    public void countDistinct() {
        assertEquals(6L, query().from(cat).where(cat.dtype.eq("C")).distinct().fetchCount());
    }

    @Test
    public void enum_binding() {
        List<Cat> cats = query().from(cat)
                .select(Projections.bean(Cat.class, QCat.cat.color)).fetch();
        assertFalse(cats.isEmpty());

        for (Cat cat : cats) {
            assertEquals(Color.BLACK, cat.getColor());
        }
    }

    @Test
    @Ignore
    public void entityProjections() {
        List<Cat> cats = query().from(cat).orderBy(cat.name.asc())
                .select(Projections.constructor(Cat.class, cat.name, cat.id)).fetch();
        assertEquals(6, cats.size());
        for (Cat c : cats) {
            assertNotNull(c.getName());
            assertTrue(c.getId() > 0);
        }
    }

    @Test
    public void entityQueries() {
        QCat catEntity = QCat.cat;

        List<Cat> cats = query().from(cat).orderBy(cat.name.asc()).select(catEntity).fetch();
        assertEquals(6, cats.size());
        for (Cat c : cats) {
            assertNotNull(c.getName());
        }
    }

    @Test
    public void entityQueries2() {
        SAnimal mate = new SAnimal("mate");
        QCat catEntity = QCat.cat;

        List<Cat> cats = query().from(cat)
                .innerJoin(mate).on(cat.mateId.eq(mate.id))
                .where(cat.dtype.eq("C"), mate.dtype.eq("C"))
                .select(catEntity).fetch();
        assertTrue(cats.isEmpty());
    }

    @Test
    public void entityQueries3() {
        QCat catEntity = new QCat("animal_");
        assertEquals(0, query().from(catEntity).select(catEntity.toes.max()).fetchFirst().orElse(null).intValue());
    }

    @Test
    @NoBatooJPA
    @NoEclipseLink
    public void entityQueries4() {
        QCat catEntity = QCat.cat;
        List<Tuple> cats = query().from(cat).select(catEntity, cat.name, cat.id).fetch();
        assertEquals(6, cats.size());

        for (Tuple tuple : cats) {
            assertTrue(tuple.get(catEntity) instanceof Cat);
            assertTrue(tuple.get(cat.name) instanceof String);
            assertTrue(tuple.get(cat.id) instanceof Integer);
        }
    }

    @Test
    @NoBatooJPA
    @NoEclipseLink
    public void entityQueries5() {
        QCat catEntity = QCat.cat;
        SAnimal otherCat = new SAnimal("otherCat");
        QCat otherCatEntity = new QCat("otherCat");
        List<Tuple> cats = query().from(cat, otherCat).select(catEntity, otherCatEntity).fetch();
        assertEquals(36, cats.size());

        for (Tuple tuple : cats) {
            assertTrue(tuple.get(catEntity) instanceof Cat);
            assertTrue(tuple.get(otherCatEntity) instanceof Cat);
        }
    }

    @Test
    @NoBatooJPA
    @NoEclipseLink
    public void entityQueries6() {
        QCat catEntity = QCat.cat;
        List<CatDTO> results = query().from(cat).select(Projections.constructor(CatDTO.class, catEntity)).fetch();
        assertEquals(6, results.size());

        for (CatDTO cat : results) {
            assertTrue(cat.cat instanceof Cat);
        }
    }

    @Test
    public void entityQueries7() {
        QCompany company = QCompany.company;
        assertEquals(Collections.emptyList(),
                query().from(company).select(company.officialName).fetch());
    }

    @Test
    public void in() {
        assertEquals(6L, query().from(cat).where(cat.dtype.in("C", "CX")).fetchCount());
    }

    @Test
    public void limit_offset() {
        assertEquals(2, query().from(cat).orderBy(cat.id.asc()).limit(2).offset(2).select(cat.id, cat.name).fetch().size());
    }

    @Test
    public void list() {
        assertEquals(6, query().from(cat).where(cat.dtype.eq("C")).select(cat.id).fetch().size());
    }

    @Test
    public void list_limit_and_offset() {
        assertEquals(3, query().from(cat).orderBy(cat.id.asc()).offset(3).limit(3).select(cat.id).fetch().size());
    }

    @Test
    public void list_limit_and_offset2() {
        List<Tuple> tuples = query().from(cat).orderBy(cat.id.asc()).offset(3).limit(3).select(cat.id, cat.name).fetch();
        assertEquals(3, tuples.size());
        assertEquals(2, tuples.get(0).size());
    }

    @Test
    public void list_limit_and_offset3() {
        List<Tuple> tuples = query().from(cat).orderBy(cat.id.asc()).offset(3).limit(3).select(Projections.tuple(cat.id, cat.name)).fetch();
        assertEquals(3, tuples.size());
        assertEquals(2, tuples.get(0).size());
    }

    @Test
    public void list_multiple() {
        print(query().from(cat).where(cat.dtype.eq("C")).select(cat.id, cat.name, cat.bodyWeight).fetch());
    }

    @Test
    public void list_non_path() {
        assertEquals(6, query().from(cat).where(cat.dtype.eq("C")).select(
                cat.birthdate.year(),
                cat.birthdate.month(),
                cat.birthdate.dayOfMonth()).fetch().size());
    }

    @Test
    public void list_results() {
        QueryResults<String> results = query().from(cat).limit(3).orderBy(cat.name.asc())
                .select(cat.name).fetchResults();
        assertEquals(Arrays.asList("Beck","Bobby","Harold"), results.getResults());
        assertEquals(6L, results.getTotal());
    }

    @Test
    @ExcludeIn(Target.H2)
    public void list_wildcard() {
        assertEquals(6, query().from(cat).where(cat.dtype.eq("C")).select(Wildcard.all).fetch().size());
    }

    @Test
    public void list_with_count() {
        print(query().from(cat).where(cat.dtype.eq("C")).groupBy(cat.name)
                .select(cat.name, cat.id.count()).fetch());
    }

    @Test
    public void list_with_limit() {
        assertEquals(3, query().from(cat).limit(3).select(cat.id).fetch().size());
    }

    @Test
    @ExcludeIn({Target.H2, Target.MYSQL})
    public void list_with_offset() {
        assertEquals(3, query().from(cat).orderBy(cat.id.asc()).offset(3).select(cat.id).fetch().size());
    }

    @Test
    @ExcludeIn(Target.HSQLDB)
    public void no_from() {
        assertNotNull(query().select(DateExpression.currentDate()).fetchFirst().orElse(null));
    }

    private void print(Iterable<Tuple> rows) {
        for (Tuple row : rows) {
            System.out.println(row);
        }
    }

    @Test
    public void projections_duplicateColumns() {
        SAnimal cat = new SAnimal("cat");
        assertEquals(1, query().from(cat).select(Projections.list(cat.count(), cat.count())).fetch().size());
    }

    @Test
    public void single_result() {
        query().from(cat).select(cat.id).fetchFirst().orElse(null);
    }

    @Test
    public void single_result_multiple() {
        assertEquals(1, query().from(cat).orderBy(cat.id.asc()).select(new Expression<?>[]{cat.id}).fetchFirst().orElse(null).get(cat.id).intValue());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void union() throws SQLException {
        SubQueryExpression<Integer> sq1 = select(cat.id.max()).from(cat);
        SubQueryExpression<Integer> sq2 = select(cat.id.min()).from(cat);
        List<Integer> list = query().union(sq1, sq2).list();
        assertFalse(list.isEmpty());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void union_all() {
        SubQueryExpression<Integer> sq1 = select(cat.id.max()).from(cat);
        SubQueryExpression<Integer> sq2 = select(cat.id.min()).from(cat);
        List<Integer> list = query().unionAll(sq1, sq2).list();
        assertFalse(list.isEmpty());
    }

    @SuppressWarnings("unchecked")
    @Test
    @ExcludeIn({Target.DERBY, Target.POSTGRESQL})
    @Ignore // FIXME
    public void union2() {
        List<Tuple> rows = query().union(
                select(cat.name, cat.id).from(cat).where(cat.name.eq("Beck")).distinct(),
                select(cat.name, null).from(cat).where(cat.name.eq("Kate")).distinct())
        .list();

        assertEquals(2, rows.size());
        for (Tuple row : rows) {
            System.err.println(row);
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    @ExcludeIn(Target.DERBY)
    @Ignore // FIXME
    public void union3() {
        SAnimal cat2 = new SAnimal("cat2");
        List<Tuple> rows = query().union(
                select(cat.id, cat2.id).from(cat).innerJoin(cat2).on(cat2.id.eq(cat.id)),
                select(cat.id, null).from(cat))
        .list();

        assertEquals(12, rows.size());
        int nulls = 0;
        for (Tuple row : rows) {
            System.err.println(Collections.singletonList(row));
            if (row.get(1, Object.class) == null) {
                nulls++;
            }
        }
        assertEquals(6, nulls);
    }

    @SuppressWarnings("unchecked")
    @Test
    @ExcludeIn({Target.DERBY, Target.POSTGRESQL})
    @Ignore // FIXME
    public void union4() {
        query().union(cat,
                select(cat.name, cat.id).from(cat).where(cat.name.eq("Beck")).distinct(),
                select(cat.name, null).from(cat).where(cat.name.eq("Kate")).distinct())
        .select(cat.name, cat.id).fetch();
    }

    @SuppressWarnings("unchecked")
    @Test
    @ExcludeIn({Target.DERBY, Target.ORACLE})
    public void union5() {
        SAnimal cat2 = new SAnimal("cat2");
        List<Tuple> rows = query().union(
                select(cat.id, cat2.id).from(cat).join(cat2).on(cat2.id.eq(cat.id.add(1))),
                select(cat.id, cat2.id).from(cat).join(cat2).on(cat2.id.eq(cat.id.add(1))))
        .list();

        assertEquals(5, rows.size());
        for (Tuple row : rows) {
            int first = row.get(cat.id);
            int second = row.get(cat2.id);
            assertEquals(first + 1, second);
        }
    }

    @Test
    public void unique_result() {
        assertEquals(1,
                query().from(cat).orderBy(cat.id.asc()).limit(1).select(cat.id).fetchOne().orElse(null).intValue());
    }

    @Test
    public void unique_result_multiple() {
        assertEquals(1,
                query().from(cat).orderBy(cat.id.asc()).limit(1).select(new Expression<?>[]{cat.id}).fetchOne().orElse(null).get(cat.id).intValue());
    }

    @Test
    @ExcludeIn(Target.H2)
    public void wildcard() {
        List<Tuple> rows = query().from(cat).select(cat.all()).fetch();
        assertEquals(6, rows.size());
        print(rows);

//        rows = query().from(cat).fetch(cat.id, cat.all());
//        assertEquals(6, rows.size());
//        print(rows);
    }


}
