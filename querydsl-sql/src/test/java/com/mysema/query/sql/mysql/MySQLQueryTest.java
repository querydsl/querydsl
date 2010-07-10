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
    public void testUseIndex(){
        query.useIndex("col1_index");        
        assertEquals("select survey.NAME from SURVEY survey USE_INDEX (col1_index) order by survey.NAME asc", toString(query));
    }
    
    @Test
    public void testUseIndex2(){        
        query.useIndex("col1_index","col2_index");        
        assertEquals("select survey.NAME from SURVEY survey USE_INDEX (col1_index, col2_index) order by survey.NAME asc", toString(query));
    }
    
    @Test
    public void testHighPriority() {
        query.highPriority();
        assertEquals("select HIGH_PRIORITY survey.NAME from SURVEY survey order by survey.NAME asc", toString(query));
    }

    @Test
    public void testStraightJoin() {
        query.straightJoin();
        assertEquals("select STRAIGHT_JOIN survey.NAME from SURVEY survey order by survey.NAME asc", toString(query));
    }

    @Test
    public void testSmallResult() {
        query.smallResult();
        assertEquals("select SQL_SMALL_RESULT survey.NAME from SURVEY survey order by survey.NAME asc", toString(query));
    }

    @Test
    public void testBigResult() {
        query.bigResult();
        assertEquals("select SQL_BIG_RESULT survey.NAME from SURVEY survey order by survey.NAME asc", toString(query));
    }

    @Test
    public void testBufferResult() {
        query.bufferResult();
        assertEquals("select SQL_BUFFER_RESULT survey.NAME from SURVEY survey order by survey.NAME asc", toString(query));
    }

    @Test
    public void testCache() {
        query.cache();
        assertEquals("select SQL_CACHE survey.NAME from SURVEY survey order by survey.NAME asc", toString(query));
    }

    @Test
    public void testNoCache() {
        query.noCache();
        assertEquals("select SQL_NO_CACHE survey.NAME from SURVEY survey order by survey.NAME asc", toString(query));
    }

    @Test
    public void testCalcFoundRows() {
        query.calcFoundRows();
        assertEquals("select SQL_CALC_FOUND_ROWS survey.NAME from SURVEY survey order by survey.NAME asc", toString(query));
    }

    @Test
    public void testWithRollup() {
        query.groupBy(survey.name);
        query.withRollup();
        assertEquals("select survey.NAME from SURVEY survey group by survey.NAME WITH ROLLUP  order by survey.NAME asc", toString(query));
    }

    @Test
    public void testForUpdate() {
        query.forUpdate();
        assertEquals("select survey.NAME from SURVEY survey order by survey.NAME asc FOR UPDATE", toString(query));
    }

    @Test
    public void testIntoOutfile() {
        query.intoOutfile(new File("target/out"));
        assertEquals("select survey.NAME from SURVEY survey order by survey.NAME asc INTO OUTFILE 'target/out'", toString(query));
    }
    
    @Test
    public void testIntoDumpfile() {
        query.intoDumpfile(new File("target/out"));
        assertEquals("select survey.NAME from SURVEY survey order by survey.NAME asc INTO DUMPFILE 'target/out'", toString(query));
    }

    @Test
    public void testIntoString() {
        query.into("var1");
        assertEquals("select survey.NAME from SURVEY survey order by survey.NAME asc INTO var1", toString(query));
    }

    @Test
    public void testLockInShareMode() {
        query.lockInShareMode();
        assertEquals("select survey.NAME from SURVEY survey order by survey.NAME asc LOCK IN SHARE MODE", toString(query));
    }
    
    private String toString(MySQLQuery query){
        return query.toString().replace('\n', ' ');
    }

}
