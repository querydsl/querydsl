package com.querydsl.jpa;

import static com.querydsl.sql.SQLExpressions.select;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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
    public void Count() {
        assertEquals(6L, query().from(cat).where(cat.dtype.eq("C")).fetchCount());
    }

    @Test
    public void Count_Via_Unique() {
        assertEquals(Long.valueOf(6), query().from(cat).where(cat.dtype.eq("C"))
                .select(cat.id.count()).fetchFirst());
    }

    @Test
    public void CountDistinct() {
        assertEquals(6L, query().from(cat).where(cat.dtype.eq("C")).distinct().fetchCount());
    }

    @Test
    public void Enum_Binding() {
        List<Cat> cats = query().from(cat)
                .select(Projections.bean(Cat.class, QCat.cat.color)).fetch();
        assertFalse(cats.isEmpty());

        for (Cat cat : cats) {
            assertEquals(Color.BLACK, cat.getColor());
        }
    }

    @Test
    @Ignore
    public void EntityProjections() {
        List<Cat> cats = query().from(cat).orderBy(cat.name.asc())
                .select(Projections.constructor(Cat.class, cat.name, cat.id)).fetch();
        assertEquals(6, cats.size());
        for (Cat c : cats) {
            System.out.println(c.getName());
        }
    }

    @Test
    public void EntityQueries() {
        QCat catEntity = QCat.cat;

        List<Cat> cats = query().from(cat).orderBy(cat.name.asc()).select(catEntity).fetch();
        assertEquals(6, cats.size());
        for (Cat c : cats) {
            System.out.println(c.getName());
        }
    }

    @Test
    public void EntityQueries2() {
        SAnimal mate = new SAnimal("mate");
        QCat catEntity = QCat.cat;

        List<Cat> cats = query().from(cat)
                .innerJoin(mate).on(cat.mateId.eq(mate.id))
                .where(cat.dtype.eq("C"), mate.dtype.eq("C"))
                .select(catEntity).fetch();
        assertTrue(cats.isEmpty());
    }

    @Test
    public void EntityQueries3() {
        QCat catEntity = new QCat("animal_");
        query().from(catEntity).select(catEntity.toes.max()).fetch();
    }

    @Test
    @NoBatooJPA
    @NoEclipseLink
    public void EntityQueries4() {
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
    public void EntityQueries5() {
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
    public void EntityQueries6() {
        QCat catEntity = QCat.cat;
        List<CatDTO> results = query().from(cat).select(Projections.constructor(CatDTO.class, catEntity)).fetch();
        assertEquals(6, results.size());

        for (CatDTO cat : results) {
            assertTrue(cat.cat instanceof Cat);
        }
    }

    @Test
    public void EntityQueries7() {
        QCompany company = QCompany.company;
        query().from(company).select(company.officialName).fetch();
    }

    @Test
    public void In() {
        assertEquals(6L, query().from(cat).where(cat.dtype.in("C", "CX")).fetchCount());
    }

    @Test
    public void Limit_Offset() {
        assertEquals(2, query().from(cat).limit(2).offset(2).select(cat.id, cat.name).fetch().size());
    }

    @Test
    public void List() {
        assertEquals(6, query().from(cat).where(cat.dtype.eq("C")).select(cat.id).fetch().size());
    }

    @Test
    public void List_Limit_And_Offset() {
        assertEquals(3, query().from(cat).offset(3).limit(3).select(cat.id).fetch().size());
    }

    @Test
    public void List_Limit_And_Offset2() {
        List<Tuple> tuples = query().from(cat).offset(3).limit(3).select(cat.id, cat.name).fetch();
        assertEquals(3, tuples.size());
        assertEquals(2, tuples.get(0).size());
    }

    @Test
    public void List_Multiple() {
        print(query().from(cat).where(cat.dtype.eq("C")).select(cat.id, cat.name, cat.bodyWeight).fetch());
    }

    @Test
    public void List_Non_Path() {
        assertEquals(6, query().from(cat).where(cat.dtype.eq("C")).select(
                cat.birthdate.year(),
                cat.birthdate.month(),
                cat.birthdate.dayOfMonth()).fetch().size());
    }

    @Test
    public void List_Results() {
        QueryResults<String> results = query().from(cat).limit(3).orderBy(cat.name.asc())
                .select(cat.name).fetchResults();
        assertEquals(Arrays.asList("Beck","Bobby","Harold"), results.getResults());
        assertEquals(6L, results.getTotal());
    }

    @Test
    @ExcludeIn(Target.H2)
    public void List_Wildcard() {
        assertEquals(6, query().from(cat).where(cat.dtype.eq("C")).select(Wildcard.all).fetch().size());
    }

    @Test
    public void List_With_Count() {
        print(query().from(cat).where(cat.dtype.eq("C")).groupBy(cat.name)
                .select(cat.name, cat.id.count()).fetch());
    }

    @Test
    public void List_With_Limit() {
        assertEquals(3, query().from(cat).limit(3).select(cat.id).fetch().size());
    }

    @Test
    @ExcludeIn({Target.H2, Target.MYSQL})
    public void List_With_Offset() {
        assertEquals(3, query().from(cat).offset(3).select(cat.id).fetch().size());
    }

    @Test
    @ExcludeIn(Target.HSQLDB)
    public void No_From() {
        assertNotNull(query().select(DateExpression.currentDate()).fetchFirst());
    }

    @Test
    public void Null_As_UniqueResult() {
        assertNull(query().from(cat).where(cat.name.eq(UUID.randomUUID().toString()))
                .select(cat.name).fetchOne());
    }

    private void print(Iterable<Tuple> rows) {
        for (Tuple row : rows) {
            System.out.println(row);
        }
    }

    @Test
    public void Projections_DuplicateColumns() {
        SAnimal cat = new SAnimal("cat");
        assertEquals(1, query().from(cat).select(Projections.list(cat.count(), cat.count())).fetch().size());
    }

    @Test
    public void Single_Result() {
        query().from(cat).select(cat.id).fetchFirst();
    }

    @Test
    public void Single_Result_Multiple() {
        query().from(cat).select(new Expression[]{cat.id}).fetchFirst();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Union() throws SQLException {
        SubQueryExpression<Integer> sq1 = select(cat.id.max()).from(cat);
        SubQueryExpression<Integer> sq2 = select(cat.id.min()).from(cat);
        List<Integer> list = query().union(sq1, sq2).list();
        assertFalse(list.isEmpty());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Union_All() {
        SubQueryExpression<Integer> sq1 = select(cat.id.max()).from(cat);
        SubQueryExpression<Integer> sq2 = select(cat.id.min()).from(cat);
        List<Integer> list = query().unionAll(sq1, sq2).list();
        assertFalse(list.isEmpty());
    }

    @Test
    @ExcludeIn({Target.DERBY, Target.POSTGRESQL})
    @Ignore // FIXME
    public void Union2() {
        List<Tuple> rows = query().union(
                select(cat.name, cat.id).from(cat).where(cat.name.eq("Beck")).distinct(),
                select(cat.name, null).from(cat).where(cat.name.eq("Kate")).distinct())
        .list();

        assertEquals(2, rows.size());
        for (Tuple row : rows) {
            System.err.println(row);
        }
    }

    @Test
    @ExcludeIn(Target.DERBY)
    @Ignore // FIXME
    public void Union3() {
        SAnimal cat2 = new SAnimal("cat2");
        List<Tuple> rows = query().union(
                select(cat.id, cat2.id).from(cat).innerJoin(cat2).on(cat2.id.eq(cat.id)),
                select(cat.id, null).from(cat))
        .list();

        assertEquals(12, rows.size());
        int nulls = 0;
        for (Tuple row : rows) {
            System.err.println(Arrays.asList(row));
            if (row.get(1, Object.class) == null) {
                nulls++;
            }
        }
        assertEquals(6, nulls);
    }

    @Test
    @ExcludeIn({Target.DERBY, Target.POSTGRESQL})
    @Ignore // FIXME
    public void Union4() {
        query().union(cat,
                select(cat.name, cat.id).from(cat).where(cat.name.eq("Beck")).distinct(),
                select(cat.name, null).from(cat).where(cat.name.eq("Kate")).distinct())
        .select(cat.name, cat.id).fetch();
    }

    @Test
    @ExcludeIn({Target.DERBY, Target.ORACLE})
    public void Union5() {
        SAnimal cat2 = new SAnimal("cat2");
        List<Tuple> rows = query().union(
                select(cat.id, cat2.id).from(cat).join(cat2).on(cat2.id.eq(cat.id.add(1))),
                select(cat.id, cat2.id).from(cat).join(cat2).on(cat2.id.eq(cat.id.add(1))))
        .list();

        assertEquals(5, rows.size());
        for (Tuple row : rows) {
            int first = row.get(cat.id).intValue();
            int second = row.get(cat2.id).intValue();
            assertEquals(first + 1, second);
        }
    }

    @Test
    public void Unique_Result() {
        query().from(cat).limit(1).select(cat.id).fetchOne();
    }

    @Test
    public void Unique_Result_Multiple() {
        query().from(cat).limit(1).select(new Expression[]{cat.id}).fetchOne();
    }

    @Test
    @ExcludeIn(Target.H2)
    public void Wildcard() {
        List<Tuple> rows = query().from(cat).select(cat.all()).fetch();
        assertEquals(6, rows.size());
        print(rows);

//        rows = query().from(cat).fetch(cat.id, cat.all());
//        assertEquals(6, rows.size());
//        print(rows);
    }


}
