package com.mysema.query.support;


import static junit.framework.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.commons.lang.IteratorAdapter;
import com.mysema.query.Projectable;
import com.mysema.query.support.GroupBy2.Group2;
import com.mysema.query.types.Expression;
import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.expr.StringExpression;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;

public class GroupBy2Test {

    private final NumberExpression<Integer> postId = new NumberPath<Integer>(Integer.class, "postId");

    private final StringExpression postName = new StringPath("postName");

    private final NumberExpression<Integer> commentId = new NumberPath<Integer>(Integer.class, "commentId");

    private final StringExpression commentText = new StringPath("commentText");

    static class PostWithComments {
        public Integer id;
        public String name;
        public Set<Integer> comments;
        public PostWithComments(Integer id, String name, Set<Integer> comments) {
            this.id = id;
            this.name = name;
            this.comments = comments;
        }
    }
    
    private static final Projectable BASIC_RESULTS = projectable(
            row(1, "post 1", 1, "comment 1"),
            row(2, "post 2", 4, "comment 4"),
            row(1, "post 1", 2, "comment 2"),
            row(2, "post 2", 5, "comment 5"),
            row(3, "post 3", 6, "comment 6"),
            row(null, "null post", 7, "comment 7"),
            row(null, "null post", 8, "comment 8"),
            row(1, "post 1", 3, "comment 3")
        );
    
    @Test 
    public void Group_Order() {
        Map<Integer, Group2> results = 
            GroupBy2.groupBy(postId).first(postName).set(commentId).transform(BASIC_RESULTS);
                
        assertEquals(4, results.size());
    }
    
    @Test
    public void First_Set_And_List() {
        Map<Integer, Group2> results = 
            GroupBy2.groupBy(postId).first(postName).set(commentId).list(commentText).transform(BASIC_RESULTS);

        Group2 group = results.get(1);
        assertEquals(toInt(1),          group.first(postId));
        assertEquals("post 1",          group.first(postName));
        assertEquals(toSet(1, 2, 3),    group.set(commentId));
        assertEquals(Arrays.asList("comment 1", "comment 2", "comment 3"),    group.list(commentText));
    }

    private static Projectable projectable(final Object[]... rows) {
        return new AbstractProjectable(){
            public CloseableIterator<Object[]> iterate(Expression<?>[] args) {
                return iterator(rows);
            }
        };
    }
    
    private Integer toInt(int i) {
        return Integer.valueOf(i);
    }
    
    private <T >Set<T> toSet(T... s) {
        return new HashSet<T>(Arrays.asList(s));
    }
    
    private static Object[] row(Object... row) {
        return row;
    }
    
    private static CloseableIterator<Object[]> iterator(Object[]... rows) {
        return new IteratorAdapter<Object[]>(Arrays.asList(rows).iterator());
    }
}
