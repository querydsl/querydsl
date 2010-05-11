package com.mysema.query.types.query;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.QueryMetadata;
import com.mysema.query.types.SubQuery;

public class SubQueryTest {
    
    @SuppressWarnings("unchecked")
    @Test
    public void test(){
        QueryMetadata metadata = new DefaultQueryMetadata();
        List<SubQuery> subQueries = Arrays.<SubQuery>asList(
                new BooleanSubQuery(metadata),
                new ComparableSubQuery(Date.class,metadata),
                new DateSubQuery(Date.class,metadata),
                new DateTimeSubQuery(Date.class,metadata),
                new ListSubQuery(Date.class,metadata),
                new NumberSubQuery(Integer.class,metadata),
                new ObjectSubQuery(String.class,metadata),
                new StringSubQuery(metadata),
                new TimeSubQuery(Date.class,metadata)
        );
        for (SubQuery sq : subQueries){
            assertNotNull(sq.asExpr());
            assertNotNull(sq.exists());
            assertNotNull(sq.getMetadata());
            assertNotNull(sq.notExists());
        }
    }

}
