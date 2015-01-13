package com.querydsl.jpa;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.querydsl.core.SearchResults;
import com.querydsl.core.Target;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.domain.Cat;
import com.querydsl.jpa.domain.Color;
import com.querydsl.jpa.domain.QCat;
import com.querydsl.jpa.domain.QCompany;
import com.querydsl.jpa.domain.sql.SAnimal;
import com.querydsl.sql.SQLSubQuery;
import com.querydsl.core.types.*;
import com.querydsl.core.types.expr.DateExpression;
import com.querydsl.core.types.expr.Wildcard;
import com.querydsl.core.testutil.ExcludeIn;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

public abstract class AbstractSQLTest {

    protected static final SAnimal cat = new SAnimal("cat");

    protected abstract AbstractSQLQuery<?> query();

    public static class CatDTO {

        Cat cat;

        public CatDTO(Cat cat) {
            this.cat = cat;
        }

    }

    protected SQLSubQuery sq() {
        return new SQLSubQuery();
    }

    @Test
    public void Count() {
        assertEquals(6l, query().from(cat).where(cat.dtype.eq("C")).count());
    }

    @Test
    public void Count_Via_Unique() {
        assertEquals(Long.valueOf(6), query().from(cat).where(cat.dtype.eq("C"))
                .uniqueResult(cat.id.count()));
    }

    @Test
    public void CountDistinct() {
        assertEquals(6l, query().from(cat).where(cat.dtype.eq("C")).distinct().count());
    }

    @Test
    public void Enum_Binding() {
        List<Cat> cats = query().from(cat)
                .list(Projections.bean(Cat.class, QCat.cat.color));
        assertFalse(cats.isEmpty());

        for (Cat cat : cats) {
            assertEquals(Color.BLACK, cat.getColor());
        }
    }

    @Test
    @Ignore
    public void EntityProjections() {
        List<Cat> cats = query().from(cat).orderBy(cat.name.asc())
                .list(ConstructorExpression.create(Cat.class, cat.name, cat.id));
        assertEquals(6, cats.size());
        for (Cat c : cats) {
            System.out.println(c.getName());
        }
    }

    @Test
    public void EntityQueries() {
        QCat catEntity = QCat.cat;

        List<Cat> cats = query().from(cat).orderBy(cat.name.asc()).list(catEntity);
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
                .list(catEntity);
        assertTrue(cats.isEmpty());
    }

    @Test
    public void EntityQueries3() {
        QCat catEntity = new QCat("animal_");
        query().from(catEntity).list(catEntity.toes.max());
    }

    @Test
    @NoBatooJPA
    @NoEclipseLink
    public void EntityQueries4() {
        QCat catEntity = QCat.cat;
        List<Tuple> cats = query().from(cat).list(catEntity, cat.name, cat.id);
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
        List<Tuple> cats = query().from(cat, otherCat).list(catEntity, otherCatEntity);
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
        List<CatDTO> results = query().from(cat).list(Projections.constructor(CatDTO.class, catEntity));
        assertEquals(6, results.size());

        for (CatDTO cat : results) {
            assertTrue(cat.cat instanceof Cat);
        }
    }

    @Test
    public void EntityQueries7() {
        QCompany company = QCompany.company;
        query().from(company).list(company.officialName);
    }

    @Test
    public void In() {
        assertEquals(6l, query().from(cat).where(cat.dtype.in("C", "CX")).count());
    }

    @Test
    public void Limit_Offset() {
        assertEquals(2, query().from(cat).limit(2).offset(2).list(cat.id, cat.name).size());
    }

    @Test
    public void List() {
        assertEquals(6, query().from(cat).where(cat.dtype.eq("C")).list(cat.id).size());
    }

    @Test
    public void List_Limit_And_Offset() {
        assertEquals(3, query().from(cat).offset(3).limit(3).list(cat.id).size());
    }

    @Test
    public void List_Limit_And_Offset2() {
        List<Tuple> tuples = query().from(cat).offset(3).limit(3).list(cat.id, cat.name);
        assertEquals(3, tuples.size());
        assertEquals(2, tuples.get(0).size());
    }

    @Test
    public void List_Multiple() {
        print(query().from(cat).where(cat.dtype.eq("C")).list(cat.id, cat.name, cat.bodyWeight));
    }

    @Test
    public void List_Non_Path() {
        assertEquals(6, query().from(cat).where(cat.dtype.eq("C")).list(
                cat.birthdate.year(),
                cat.birthdate.month(),
                cat.birthdate.dayOfMonth()).size());
    }

    @Test
    public void List_Results() {
        SearchResults<String> results = query().from(cat).limit(3).orderBy(cat.name.asc())
                .listResults(cat.name);
        assertEquals(Arrays.asList("Beck","Bobby","Harold"), results.getResults());
        assertEquals(6l, results.getTotal());
    }

    @Test
    @ExcludeIn(Target.H2)
    public void List_Wildcard() {
        assertEquals(6l, query().from(cat).where(cat.dtype.eq("C")).list(Wildcard.all).size());
    }

    @Test
    public void List_With_Count() {
        print(query().from(cat).where(cat.dtype.eq("C")).groupBy(cat.name)
                .list(cat.name, cat.id.count()));
    }

    @Test
    public void List_With_Limit() {
        assertEquals(3, query().from(cat).limit(3).list(cat.id).size());
    }

    @Test
    @ExcludeIn({Target.H2, Target.MYSQL})
    public void List_With_Offset() {
        assertEquals(3, query().from(cat).offset(3).list(cat.id).size());
    }

    @Test
    @ExcludeIn(Target.HSQLDB)
    public void No_From() {
        assertNotNull(query().singleResult(DateExpression.currentDate()));
    }

    @Test
    public void Null_As_UniqueResult() {
        assertNull(query().from(cat).where(cat.name.eq(UUID.randomUUID().toString()))
                .uniqueResult(cat.name));
    }

    private void print(Iterable<Tuple> rows) {
        for (Tuple row : rows) {
            System.out.println(row);
        }
    }

    @Test
    public void Projections_DuplicateColumns() {
        SAnimal cat = new SAnimal("cat");
        assertEquals(1, query().from(cat).list(new QList(cat.count(), cat.count())).size());
    }

    @Test
    public void Single_Result() {
        query().from(cat).singleResult(cat.id);
    }

    @Test
    public void Single_Result_Multiple() {
        query().from(cat).singleResult(new Expression[]{cat.id});
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Union() throws SQLException {
        SubQueryExpression<Integer> sq1 = sq().from(cat).unique(cat.id.max());
        SubQueryExpression<Integer> sq2 = sq().from(cat).unique(cat.id.min());
        List<Integer> list = query().union(sq1, sq2).list();
        assertFalse(list.isEmpty());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Union_All() {
        SubQueryExpression<Integer> sq1 = sq().from(cat).unique(cat.id.max());
        SubQueryExpression<Integer> sq2 = sq().from(cat).unique(cat.id.min());
        List<Integer> list = query().unionAll(sq1, sq2).list();
        assertFalse(list.isEmpty());
    }

    @Test
    @ExcludeIn({Target.DERBY, Target.POSTGRES})
    public void Union2() {
        List<Tuple> rows = query().union(
            new SQLSubQuery().from(cat).where(cat.name.eq("Beck")).distinct().list(cat.name, cat.id),
            new SQLSubQuery().from(cat).where(cat.name.eq("Kate")).distinct().list(cat.name, null))
        .list();

        assertEquals(2, rows.size());
        for (Tuple row : rows) {
            System.err.println(row);
        }
    }

    @Test
    @ExcludeIn(Target.DERBY)
    public void Union3() {
        SAnimal cat2 = new SAnimal("cat2");
        List<Tuple> rows = query().union(
            new SQLSubQuery().from(cat).innerJoin(cat2).on(cat2.id.eq(cat.id)).list(cat.id, cat2.id),
            new SQLSubQuery().from(cat).list(cat.id, null))
        .list();

        assertEquals(12, rows.size());
        int nulls = 0;
        for (Tuple row : rows) {
            System.err.println(Arrays.asList(row));
            if (row.get(1, Object.class) == null) nulls++;
        }
        assertEquals(6, nulls);
    }

    @Test
    @ExcludeIn({Target.DERBY, Target.POSTGRES})
    public void Union4() {
        query().union(cat,
            new SQLSubQuery().from(cat).where(cat.name.eq("Beck")).distinct().list(cat.name, cat.id),
            new SQLSubQuery().from(cat).where(cat.name.eq("Kate")).distinct().list(cat.name, null))
        .list(cat.name, cat.id);
    }

    @Test
    @ExcludeIn({Target.DERBY, Target.ORACLE})
    public void Union5() {
        SAnimal cat2 = new SAnimal("cat2");
        List<Tuple> rows = query().union(
            new SQLSubQuery().from(cat).join(cat2).on(cat2.id.eq(cat.id.add(1))).list(cat.id, cat2.id),
            new SQLSubQuery().from(cat).join(cat2).on(cat2.id.eq(cat.id.add(1))).list(cat.id, cat2.id))
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
        query().from(cat).limit(1).uniqueResult(cat.id);
    }

    @Test
    public void Unique_Result_Multiple() {
        query().from(cat).limit(1).uniqueResult(new Expression[]{cat.id});
    }

    @Test
    @ExcludeIn(Target.H2)
    public void Wildcard() {
        List<Tuple> rows = query().from(cat).list(cat.all());
        assertEquals(6, rows.size());
        print(rows);

//        rows = querydsl().from(cat).list(cat.id, cat.all());
//        assertEquals(6, rows.size());
//        print(rows);
    }


}
