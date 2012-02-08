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

import static com.mysema.query.Constants.survey;
import static com.mysema.query.Constants.survey2;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mysema.query.sql.SQLSubQuery;
import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.sql.domain.QEmployee;
import com.mysema.query.sql.domain.QSurvey;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathImpl;
import com.mysema.testutil.ExcludeIn;

public abstract class InsertBaseTest extends AbstractBaseTest{

    private void reset() throws SQLException{
        delete(survey).execute();
        insert(survey).values(1, "Hello World").execute();
    }

    @Before
    public void setUp() throws SQLException{
        reset();
    }

    @After
    public void tearDown() throws SQLException{
        reset();
    }

    @Test
    public void Insert_With_Columns() {
        assertEquals(1, insert(survey)
                .columns(survey.id, survey.name)
                .values(3, "Hello").execute());
    }
    
    @Test
    public void Insert_Without_Columns() {
        assertEquals(1, insert(survey).values(4, "Hello").execute());
            
    }
    
    @Test
    public void Insert_With_SubQuery() {
        int count = (int)query().from(survey).count();
        assertEquals(count, insert(survey)
            .columns(survey.id, survey.name)
            .select(sq().from(survey2).list(survey2.id.add(20), survey2.name))
            .execute());
    }
    
    @Test
    public void Insert_With_SubQuery_Via_Constructor() {
        int count = (int)query().from(survey).count();
        SQLInsertClause insert = insert(survey, sq().from(survey2));
        insert.set(survey.id, survey2.id.add(20));
        insert.set(survey.name, survey2.name);
        assertEquals(count, insert.execute());
    }    
    
    @Test
    public void Insert_With_SubQuery_Without_Columns() {
        int count = (int)query().from(survey).count();
        assertEquals(count, insert(survey)
            .select(sq().from(survey2).list(survey2.id.add(10), survey2.name))
            .execute());
        
    }
        
    @Test
    public void Insert_Batch(){
        SQLInsertClause insert = insert(survey)
            .set(survey.id, 5)
            .set(survey.name, "55")
            .addBatch();
        
        insert.set(survey.id, 6)
            .set(survey.name, "66")
            .addBatch();
     
        assertEquals(2, insert.execute());
        
        assertEquals(1l, query().from(survey).where(survey.name.eq("55")).count());
        assertEquals(1l, query().from(survey).where(survey.name.eq("66")).count());
    }
    
    @Test
    public void Insert_Nulls_In_Batch() {
//        QFoo f= QFoo.foo;
//        SQLInsertClause sic = new SQLInsertClause(c, new H2Templates(), f);
//        sic.columns(f.c1,f.c2).values(null,null).addBatch();
//        sic.columns(f.c1,f.c2).values(null,1).addBatch();
//        sic.execute();
        SQLInsertClause sic = insert(survey);
        sic.columns(survey.id, survey.name).values(null, null).addBatch();
        sic.columns(survey.id, survey.name).values(null, 1).addBatch();
        System.out.println("start batch");
        sic.execute();
        System.out.println("end batch");
        
    }
    
    @Test
    public void Like_with_Escape(){
        SQLInsertClause insert = insert(survey);
        insert.set(survey.id, 5).set(survey.name, "aaa").addBatch();
        insert.set(survey.id, 6).set(survey.name, "a_").addBatch();    
        insert.set(survey.id, 7).set(survey.name, "a%").addBatch();
        assertEquals(3, insert.execute());
        
        assertEquals(1l, query().from(survey).where(survey.name.like("a\\%")).count());
        assertEquals(1l, query().from(survey).where(survey.name.like("a\\_")).count());
        assertEquals(3l, query().from(survey).where(survey.name.like("a%")).count());
        assertEquals(2l, query().from(survey).where(survey.name.like("a_")).count());
        
        assertEquals(1l, query().from(survey).where(survey.name.startsWith("a_")).count());
        assertEquals(1l, query().from(survey).where(survey.name.startsWith("a%")).count());
    }
    
    @Test
    public void InsertBatch_with_Subquery(){
        SQLInsertClause insert = insert(survey)
            .columns(survey.id, survey.name)
            .select(sq().from(survey2).list(survey2.id.add(20), survey2.name))
            .addBatch();
        
        insert(survey)
            .columns(survey.id, survey.name)
            .select(sq().from(survey2).list(survey2.id.add(40), survey2.name))
            .addBatch();
        
        insert.execute();
//        assertEquals(1, insert.execute());
    }

    @Test
    @ExcludeIn({Target.HSQLDB, Target.DERBY, Target.POSTGRES})
    public void Insert_With_Keys() throws SQLException{
        ResultSet rs = insert(survey).set(survey.name, "Hello World").executeWithKeys();
        assertTrue(rs.next());
        assertTrue(rs.getObject(1) != null);
        rs.close();
    }
    
    @Test
    @ExcludeIn({Target.HSQLDB, Target.DERBY, Target.POSTGRES})
    public void Insert_With_Keys_Projected() throws SQLException{
        assertNotNull(insert(survey).set(survey.name, "Hello you").executeWithKey(survey.id));
    }
    
    @Test
    @ExcludeIn({Target.HSQLDB, Target.DERBY, Target.POSTGRES})
    public void Insert_With_Keys_Projected2() throws SQLException{
        Path<Object> idPath = new PathImpl<Object>(Object.class, "id");
        Object id = insert(survey).set(survey.name, "Hello you").executeWithKey(idPath);
        assertNotNull(id);
    }

    @Test
    public void Insert_Null_With_Columns() {
        assertEquals(1, insert(survey)
                .columns(survey.id, survey.name)
                .values(3, null).execute());    
    }

    @Test
    public void Insert_Null_Without_Columns() {
        assertEquals(1, insert(survey)
                .values(4, null).execute());        
    }
    
    @Test
    public void Insert_With_Set() {
        assertEquals(1, insert(survey)
                .set(survey.id, 5)
                .set(survey.name, (String)null)
                .execute());   
    }
    
    @Test
    public void Insert_Alternative_Syntax(){
        // with columns
        assertEquals(1, insert(survey)
            .set(survey.id, 3)
            .set(survey.name, "Hello")
            .execute());
    }

    @Test
    public void Complex1(){
        // related to #584795
        QSurvey survey = new QSurvey("survey");
        QEmployee emp1 = new QEmployee("emp1");
        QEmployee emp2 = new QEmployee("emp2");
        SQLInsertClause insert = insert(survey);
        insert.columns(survey.id, survey.name);
        insert.select(new SQLSubQuery().from(survey)
          .innerJoin(emp1)
           .on(survey.id.eq(emp1.id))
          .innerJoin(emp2)
           .on(emp1.superiorId.eq(emp2.superiorId), emp1.firstname.eq(emp2.firstname))
          .list(survey.id, emp2.firstname));
        
        insert.execute();
    }
    

}
