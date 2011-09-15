package com.mysema.query.support;


import static com.mysema.query.support.GroupBy2.list;
import static com.mysema.query.support.GroupBy2.set;
import static junit.framework.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.commons.lang.EmptyCloseableIterator;
import com.mysema.commons.lang.IteratorAdapter;
import com.mysema.query.Projectable;
import com.mysema.query.Tuple;
import com.mysema.query.types.Expression;
import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.expr.StringExpression;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;

public class GroupBy2Test {

    private final NumberExpression<Integer> postId = new NumberPath<Integer>(Integer.class, "postId");

    private final StringExpression postName = new StringPath("postName");

    private final NumberExpression<Integer> commentId = new NumberPath<Integer>(Integer.class, "commentId");

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
    
    @Test
    public void Expression_Order_And_Type() {
        new GroupBy2(postId, postName, set(commentId)).transform(new AbstractProjectable(){
            public CloseableIterator<Object[]> iterate(Expression<?>[] args) {
                assertEquals(postId, args[0]);
                assertEquals(postName, args[1]);
                assertEquals(commentId, args[2]);
                return new EmptyCloseableIterator<Object[]>();
            }
        });
    }
    
    /**
     * <ol>
     * <li>Order of groups by first row of a group
     * <li>Rows belonging to a group may appear in any order
     * <li>Group of null is handled correctly
     * </ol>
     */
    @Test
    public void Multiple_Groups() {
        Collection<Tuple> results = new GroupBy2(postId, postName, list(commentId)).transform(
            projectable(
                row(1, "post 1", 1),
                row(2, "post 2", 4),
                row(1, "post 1", 2),
                row(2, "post 2", 5),
                row(3, "post 3", 6),
                row(null, "null post", 7),
                row(null, "null post", 8),
                row(1, "post 1", 3)
            )
        );
        assertEquals(4, results.size());
        Iterator<Tuple> iter = results.iterator();
        
        Tuple g = iter.next();
        assertEquals(toInt(1), g.get(postId));
        assertEquals("post 1", g.get(postName));
        List<Integer> comments = g.get(list(commentId));
        assertEquals(toInt(1), comments.get(0));
        assertEquals(toInt(2), comments.get(1));
        assertEquals(toInt(3), comments.get(2));
        
        g = iter.next();
        assertEquals(toInt(2), g.get(postId));
        assertEquals("post 2", g.get(postName));
        comments = g.get(list(commentId));
        assertEquals(toInt(4), comments.get(0));
        assertEquals(toInt(5), comments.get(1));
        
        g = iter.next();
        assertEquals(toInt(3), g.get(postId));
        assertEquals("post 3", g.get(postName));
        comments = g.get(list(commentId));
        assertEquals(toInt(6), comments.get(0));
        
        // Group by null value
        g = iter.next();
        assertEquals(null, g.get(postId));
        assertEquals("null post", g.get(postName));
        comments = g.get(list(commentId));
        assertEquals(toInt(7), comments.get(0));
        assertEquals(toInt(8), comments.get(1));
    }
    
    private static <T> Set<T> toSet(T... o) {
        return new HashSet<T>(Arrays.asList(o));
    }
    
    @Test
    public void Group_As_Set() {
        Collection<Tuple> results = new GroupBy2(postId, postName, set(commentId)).transform(projectable(
            row(1, "post 1", 1),
            row(null, "null post", 2)
        ));
        assertEquals(2, results.size());
        Iterator<Tuple> iter = results.iterator();
        
        Tuple group = iter.next();
        assertEquals(toInt(1), group.get(postId));
        assertEquals("post 1", group.get(postName));
        assertEquals(toSet(1), group.get(set(commentId)));
        
        group = iter.next();
        assertEquals(null, group.get(postId));
    }

    private Projectable projectable(final Object[]... rows) {
        return new AbstractProjectable(){
            public CloseableIterator<Object[]> iterate(Expression<?>[] args) {
                return iterator(rows);
            }
        };
    }
    
    private Integer toInt(int i) {
        return Integer.valueOf(i);
    }
    
    private static Object[] row(Object... row) {
        return row;
    }
    
    private static CloseableIterator<Object[]> iterator(Object[]... rows) {
        return new IteratorAdapter<Object[]>(Arrays.asList(rows).iterator());
    }
}
