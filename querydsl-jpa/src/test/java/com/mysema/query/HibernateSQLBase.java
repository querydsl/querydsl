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
package com.mysema.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;
import org.junit.runner.RunWith;

import com.mysema.query.jpa.domain.Cat;
import com.mysema.query.jpa.domain.QCat;
import com.mysema.query.jpa.domain.sql.SAnimal;
import com.mysema.query.jpa.hibernate.sql.HibernateSQLQuery;
import com.mysema.query.sql.SQLSubQuery;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.types.ConstructorExpression;
import com.mysema.query.types.Expression;
import com.mysema.query.types.SubQueryExpression;
import com.mysema.query.types.expr.Wildcard;
import com.mysema.testutil.ExcludeIn;
import com.mysema.testutil.HibernateTestRunner;

@RunWith(HibernateTestRunner.class)
public class HibernateSQLBase {
    
    @Rule
    public static MethodRule targetRule = new TargetRule();
    
    private final SQLTemplates templates = Mode.getSQLTemplates();
    
    private final SAnimal cat = new SAnimal("cat");
    
    private Session session;
    
    protected HibernateSQLQuery query() {
        return new HibernateSQLQuery(session, templates);
    }
    
    protected SQLSubQuery sq() {
        return new SQLSubQuery();
    }

    public void setSession(Session session) {
        this.session = session;
    }

    @Before
    public void setUp() {
        if (query().from(cat).notExists()) {
            session.save(new Cat("Beck",1));
            session.save(new Cat("Kate",2));
            session.save(new Cat("Kitty",3));
            session.save(new Cat("Bobby",4));
            session.save(new Cat("Harold",5));
            session.save(new Cat("Tim",6));
            session.flush();    
        }        
    }
    
    @Test
    public void In() {
        assertEquals(6l, query().from(cat).where(cat.dtype.in("C", "CX")).count());
    }

    @Test
    public void Count() {
        assertEquals(6l, query().from(cat).where(cat.dtype.eq("C")).count());
    }
    
    @Test
    @ExcludeIn(Target.H2)
    public void Count_Via_Unique() {
        assertEquals(Long.valueOf(6), query().from(cat).where(cat.dtype.eq("C")).uniqueResult(cat.id.count()));
    }
    
    @Test
    public void CountDistinct() {
        assertEquals(6l, query().from(cat).where(cat.dtype.eq("C")).countDistinct());
    }
    
    @Test
    public void List() {
        assertEquals(6, query().from(cat).where(cat.dtype.eq("C")).list(cat.id).size());
    }
    
    @Test
    @ExcludeIn(Target.H2)
    public void List_Wildcard() {
        assertEquals(6l, query().from(cat).where(cat.dtype.eq("C")).list(Wildcard.all).size());
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
    public void List_Limit_And_Offset() {
        assertEquals(3, query().from(cat).offset(3).limit(3).list(cat.id).size());    
    }
    
    @Test
    public void List_Multiple() {
        print(query().from(cat).where(cat.dtype.eq("C")).list(cat.id, cat.name, cat.bodyWeight));    
    }
    
    @Test
    public void List_Results() {
        SearchResults<String> results = query().from(cat).limit(3).orderBy(cat.name.asc()).listResults(cat.name);
        assertEquals(Arrays.asList("Beck","Bobby","Harold"), results.getResults());
        assertEquals(6l, results.getTotal());        
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
    public void Single_Result() {
        query().from(cat).singleResult(cat.id);    
    }
    
    @Test
    public void Single_Result_Multiple() {
        query().from(cat).singleResult(new Expression[]{cat.id});    
    }

    @Test
    public void EntityQueries() {
        SAnimal cat = new SAnimal("cat");
        SAnimal mate = new SAnimal("mate");
        QCat catEntity = QCat.cat;

        // 1
        List<Cat> cats = query().from(cat).orderBy(cat.name.asc()).list(catEntity);
        assertEquals(6, cats.size());
        for (Cat c : cats) System.out.println(c.getName());

        // 2
        cats = query().from(cat)
            .innerJoin(mate).on(cat.mateId.eq(mate.id))
            .where(cat.dtype.eq("C"), mate.dtype.eq("C"))
            .list(catEntity);
        assertTrue(cats.isEmpty());
    }
    
    @Test
    public void EntityQueries_CreateQuery() {
        SAnimal cat = new SAnimal("cat");
        QCat catEntity = QCat.cat;
        
        Query query = query().from(cat).createQuery(catEntity);
        assertEquals(6, query.list().size());
    }

    @Test
    @ExcludeIn(Target.MYSQL)
    public void EntityQueries_CreateQuery2() {
        SAnimal cat = new SAnimal("CAT");
        QCat catEntity = QCat.cat;
        
        Query query = query().from(cat).createQuery(catEntity);
        assertEquals(6, query.list().size());
    }
    
    @Test
    @ExcludeIn(Target.ORACLE)
    public void EntityProjections() {
        SAnimal cat = new SAnimal("cat");

        List<Cat> cats = query().from(cat).orderBy(cat.name.asc())
            .list(ConstructorExpression.create(Cat.class, cat.name, cat.id));
        assertEquals(6, cats.size());
        for (Cat c : cats) System.out.println(c.getName());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Union() throws SQLException {
        SAnimal cat = new SAnimal("cat");
        SubQueryExpression<Integer> sq1 = sq().from(cat).unique(cat.id.max());
        SubQueryExpression<Integer> sq2 = sq().from(cat).unique(cat.id.min());
        List<Integer> list = query().union(sq1, sq2).list();
        assertFalse(list.isEmpty());
    }
    
    @Test
    @SuppressWarnings("unchecked")
    public void Union_All() {
        SAnimal cat = new SAnimal("cat");
        SubQueryExpression<Integer> sq1 = sq().from(cat).unique(cat.id.max());
        SubQueryExpression<Integer> sq2 = sq().from(cat).unique(cat.id.min());
        List<Integer> list = query().unionAll(sq1, sq2).list();
        assertFalse(list.isEmpty());
    }
    
    @Test
    @ExcludeIn(Target.H2)
    public void Wildcard() {
        SAnimal cat = new SAnimal("cat");

        List<Tuple> rows = query().from(cat).list(cat.all());
        assertEquals(6, rows.size());
        print(rows);

//        rows = query().from(cat).list(cat.id, cat.all());
//        assertEquals(6, rows.size());
//        print(rows);
    }

    private void print(Iterable<Tuple> rows) {
        for (Tuple row : rows) {
            System.out.println(row);
        }
    }
    
}
