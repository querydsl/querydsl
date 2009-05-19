package com.mysema.query.collections;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections15.IteratorUtils;
import org.junit.Test;

import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.grammar.GrammarWithAlias;
import com.mysema.query.grammar.types.Path.PNumber;

public class PagingTest extends AbstractQueryTest{

	private List<Integer> ints = Arrays.asList(1,2,3,4,5,6,7,8,9); 
	
	private PNumber<Integer> var = GrammarWithAlias.$(0);

    @Test
    public void test(){
        assertResultSize(9, 9, null);
        assertResultSize(9, 2, new QueryModifiers(2l,null));
        assertResultSize(9, 2, new QueryModifiers(2l,0l));
        assertResultSize(9, 2, new QueryModifiers(2l,3l));        
        assertResultSize(9, 9, new QueryModifiers(20l,null));
        assertResultSize(9, 9, new QueryModifiers(20l,0l));
        assertResultSize(9, 5, new QueryModifiers(20l,4l));
        assertResultSize(9, 0, new QueryModifiers(10l,9l));
    }
    
    private void assertResultSize(int total, int size, QueryModifiers modifiers){        
        // via list
        assertEquals(size, createQuery(modifiers).list(var).size());
        
        // via results
        SearchResults<?> results = createQuery(modifiers).listResults(var);
        assertEquals(total, results.getTotal());
        assertEquals(size, results.getResults().size());
                
        // via count (ignore limit and offset)
        assertEquals(total, createQuery(modifiers).count());
        
        // via iterator
        assertEquals(size, IteratorUtils.toList(createQuery(modifiers).iterate(var)).size());
    }
    
    private ColQuery createQuery(QueryModifiers modifiers){
    	ColQuery query = new ColQuery().from(var, ints);
        if (modifiers != null) query.restrict(modifiers);
        return query;
    }
}
