package com.mysema.query.sql;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.JoinType;
import com.mysema.query.QueryMetadata;
import com.mysema.query.sql.domain.QSurvey;
import com.mysema.testutil.Performance;

@Category(Performance.class)
public class PerformanceTest {
    
    private QueryMetadata md;
    
    private SQLTemplates templates;
    
    int iterations;
    
    @Before
    public void setUp() {
        QSurvey survey = QSurvey.survey;
        md = new DefaultQueryMetadata();
        md.addJoin(JoinType.DEFAULT, survey);
        md.addWhere(survey.id.eq(10));
        md.addProjection(survey.name);
        
        templates = new H2Templates();
        
        iterations =  1000000;
    }
    
    @Test
    public void NonNormalized() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            SQLSerializer serializer = new SQLSerializer(templates);
            serializer.setNormalize(false);
            serializer.serialize(md, false);
            serializer.getConstants();
            serializer.getConstantPaths();
            assertNotNull(serializer.toString());
        }
        System.err.println("non-normalized " + (System.currentTimeMillis() - start));
    }
    
    @Test
    public void Default() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            SQLSerializer serializer = new SQLSerializer(templates);
            serializer.serialize(md, false);
            serializer.getConstants();
            serializer.getConstantPaths();
            assertNotNull(serializer.toString());
        }
        System.err.println("default " + (System.currentTimeMillis() - start));
    }
        
}
