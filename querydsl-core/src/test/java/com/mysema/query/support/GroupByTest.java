package com.mysema.query.support;


import static junit.framework.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.commons.lang.EmptyCloseableIterator;
import com.mysema.commons.lang.IteratorAdapter;
import com.mysema.query.Tuple;
import com.mysema.query.types.Expression;
import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.expr.StringExpression;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;

public class GroupByTest {

    private final NumberExpression<Integer> postId = new NumberPath<Integer>(Integer.class, "postId");

    private final StringExpression postName = new StringPath("postName");

    private final NumberExpression<Integer> commentId = new NumberPath<Integer>(Integer.class, "commentId");

    @Test
    public void Expression_Order() {
        new GroupBy(postId, postName, commentId).transform(new AbstractProjectable(){
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
        Collection<Group> results = new GroupBy(postId, postName, commentId).transform(new AbstractProjectable(){
            public CloseableIterator<Object[]> iterate(Expression<?>[] args) {
                return iterator(
                        row(1, "post 1", 1),
                        row(2, "post 2", 4),
                        row(1, "post 1", 2),
                        row(2, "post 2", 5),
                        row(3, "post 3", 6),
                        row(null, "null post", 7),
                        row(null, "null post", 8),
                        row(1, "post 1", 3)
                );
            }
        });
        assertEquals(4, results.size());
        Iterator<Group> iter = results.iterator();
        
        Group g = iter.next();
        assertEquals(toInt(1), g.get(postId));
        assertEquals("post 1", g.get(postName));
        assertEquals(3, g.size());
        List<Integer> comments = g.getList(commentId);
        assertEquals(toInt(1), comments.get(0));
        assertEquals(toInt(2), comments.get(1));
        assertEquals(toInt(3), comments.get(2));
        
        g = iter.next();
        assertEquals(toInt(2), g.get(postId));
        assertEquals("post 2", g.get(postName));
        assertEquals(2, g.size());
        comments = g.getList(commentId);
        assertEquals(toInt(4), comments.get(0));
        assertEquals(toInt(5), comments.get(1));
        
        g = iter.next();
        assertEquals(toInt(3), g.get(postId));
        assertEquals("post 3", g.get(postName));
        assertEquals(1, g.size());
        comments = g.getList(commentId);
        assertEquals(toInt(6), comments.get(0));
        
        // Group by null value
        g = iter.next();
        assertEquals(null, g.get(postId));
        assertEquals("null post", g.get(postName));
        assertEquals(2, g.size());
        comments = g.getList(commentId);
        assertEquals(toInt(7), comments.get(0));
        assertEquals(toInt(8), comments.get(1));
    }
    
    @Test
    public void Get_Row() {
        Collection<Group> results = new GroupBy(postId, postName, commentId).transform(new AbstractProjectable(){
            public CloseableIterator<Object[]> iterate(Expression<?>[] args) {
                return iterator(
                        row(1, "post 1", 1),
                        row(null, "null post", 2)
                );
            }
        });
        assertEquals(2, results.size());
        Iterator<Group> iter = results.iterator();
        
        Group group = iter.next();
        Tuple row = group.getRow(0);
        assertEquals(toInt(1), row.get(postId));
        assertEquals("post 1", row.get(postName));
        assertEquals(toInt(1), row.get(commentId));
        
        group = iter.next();
        row = group.getRow(0);
        assertEquals(null, row.get(postId));
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void Row_Out_of_Bounds() {
        Collection<Group> results = new GroupBy(postId, postName, commentId).transform(new AbstractProjectable(){
            public CloseableIterator<Object[]> iterate(Expression<?>[] args) {
                return iterator(row(1, "post 1", 1));
            }
        });
        results.iterator().next().getRow(1);
    }
    
    private Integer toInt(int i) {
        return Integer.valueOf(i);
    }
    
    private static Object[] row(Object... row) {
        return row;
    }
    

    @SuppressWarnings("unchecked")
    private static <T> CloseableIterator<T> iterator(Object[]... rows) {
        return new IteratorAdapter(Arrays.asList(rows).iterator());
    }
}
