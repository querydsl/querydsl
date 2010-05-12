package com.mysema.query.lucene;

import static org.junit.Assert.*;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.TermQuery;
import org.junit.Test;


public class QueryElementTest {

    @Test
    public void test(){
        QueryElement element = new QueryElement(new TermQuery(new Term("str","text")));        
        assertEquals("str:text",element.toString());
        assertEquals(element.getQuery().hashCode(), element.hashCode());
        
        QueryElement element2 = new QueryElement(new TermQuery(new Term("str","text")));
        assertEquals(element2, element);
    }
}
