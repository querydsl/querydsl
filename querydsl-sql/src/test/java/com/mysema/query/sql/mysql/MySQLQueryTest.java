/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql.mysql;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.mysema.query.sql.MySQLTemplates;
import com.mysema.query.sql.domain.QSurvey;

public class MySQLQueryTest {
    
    private MySQLQuery query;
    
    private QSurvey survey = new QSurvey("survey");
    
    @Before
    public void setUp(){
        query = new MySQLQuery(null, new MySQLTemplates().newLineToSingleSpace());
        query.from(survey);
        query.orderBy(survey.name.asc());
        query.getMetadata().addProjection(survey.name);
    }

//  SELECT * FROM table1 USE INDEX (col1_index,col2_index)
//  WHERE col1=1 AND col2=2 AND col3=3;
    
    @Test
    public void UseIndex(){
        query.useIndex("col1_index");        
        assertEquals("select survey.NAME from SURVEY survey use_index (col1_index) order by survey.NAME asc", toString(query));
    }
    
    @Test
    public void UseIndex2(){        
        query.useIndex("col1_index","col2_index");        
        assertEquals("select survey.NAME from SURVEY survey use_index (col1_index, col2_index) order by survey.NAME asc", toString(query));
    }
    
    @Test
    public void HighPriority() {
        query.highPriority();
        assertEquals("select high_priority survey.NAME from SURVEY survey order by survey.NAME asc", toString(query));
    }

    @Test
    public void StraightJoin() {
        query.straightJoin();
        assertEquals("select straight_join survey.NAME from SURVEY survey order by survey.NAME asc", toString(query));
    }

    @Test
    public void SmallResult() {
        query.smallResult();
        assertEquals("select sql_small_result survey.NAME from SURVEY survey order by survey.NAME asc", toString(query));
    }

    @Test
    public void BigResult() {
        query.bigResult();
        assertEquals("select sql_big_result survey.NAME from SURVEY survey order by survey.NAME asc", toString(query));
    }

    @Test
    public void BufferResult() {
        query.bufferResult();
        assertEquals("select sql_buffer_result survey.NAME from SURVEY survey order by survey.NAME asc", toString(query));
    }

    @Test
    public void Cache() {
        query.cache();
        assertEquals("select sql_cache survey.NAME from SURVEY survey order by survey.NAME asc", toString(query));
    }

    @Test
    public void NoCache() {
        query.noCache();
        assertEquals("select sql_no_cache survey.NAME from SURVEY survey order by survey.NAME asc", toString(query));
    }

    @Test
    public void CalcFoundRows() {
        query.calcFoundRows();
        assertEquals("select sql_calc_found_rows survey.NAME from SURVEY survey order by survey.NAME asc", toString(query));
    }

    @Test
    public void WithRollup() {
        query.groupBy(survey.name);
        query.withRollup();
        assertEquals("select survey.NAME from SURVEY survey group by survey.NAME with rollup  order by survey.NAME asc", toString(query));
    }

    @Test
    public void ForUpdate() {
        query.forUpdate();
        assertEquals("select survey.NAME from SURVEY survey order by survey.NAME asc for update", toString(query));
    }

    @Test
    public void IntoOutfile() {
        query.intoOutfile(new File("target/out"));
        assertEquals("select survey.NAME from SURVEY survey order by survey.NAME asc into outfile 'target" + File.separator + "out'", toString(query));
    }
    
    @Test
    public void IntoDumpfile() {
        query.intoDumpfile(new File("target/out"));
        assertEquals("select survey.NAME from SURVEY survey order by survey.NAME asc into dumpfile 'target" + File.separator + "out'", toString(query));
    }

    @Test
    public void IntoString() {
        query.into("var1");
        assertEquals("select survey.NAME from SURVEY survey order by survey.NAME asc into var1", toString(query));
    }

    @Test
    public void LockInShareMode() {
        query.lockInShareMode();
        assertEquals("select survey.NAME from SURVEY survey order by survey.NAME asc lock in share mode", toString(query));
    }
    
    private String toString(MySQLQuery query){
        return query.toString().replace('\n', ' ');
    }

}
