package com.mysema.query.collections;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.JoinType;
import com.mysema.query.QueryMetadata;

public class QueryMetadataTest extends AbstractQueryTest {
    
    @Test
    public void Reusage(){
        QueryMetadata metadata = new DefaultQueryMetadata();
        metadata.addJoin(JoinType.DEFAULT, cat);
        metadata.addWhere(cat.name.startsWith("A"));
        
        ColQuery query = new ColQueryImpl(metadata);
        query.bind(cat, cats);
        assertEquals(c3, query.list(cat));
    }

}
