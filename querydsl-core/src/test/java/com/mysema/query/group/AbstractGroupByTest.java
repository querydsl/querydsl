package com.mysema.query.group;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.mysema.commons.lang.CloseableIterator;
import com.mysema.commons.lang.IteratorAdapter;
import com.mysema.commons.lang.Pair;
import com.mysema.query.Projectable;
import com.mysema.query.Tuple;
import com.mysema.query.support.AbstractProjectable;
import com.mysema.query.types.ConstructorExpression;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Projections;
import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.expr.StringExpression;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.SimplePath;
import com.mysema.query.types.path.StringPath;

public abstract class AbstractGroupByTest {

    protected static final SimplePath<Post> post = new SimplePath<Post>(Post.class, "post");

    protected static final SimplePath<User> user = new SimplePath<User>(User.class, "user");

    protected static final SimplePath<Comment> comment = new SimplePath<Comment>(Comment.class, "comment");

    protected static final NumberExpression<Integer> postId = new NumberPath<Integer>(Integer.class, post, "id");

    protected static final StringExpression userName = new StringPath(user, "name");

    protected static final StringExpression postName = new StringPath(post, "name");

    protected static final NumberPath<Integer> commentId = new NumberPath<Integer>(Integer.class, comment, "id");

    protected static final StringExpression commentText = new StringPath(comment, "text");

    protected static final ConstructorExpression<Comment> qComment = Projections.constructor(Comment.class, commentId, commentText);

    protected static <K, V> Pair<K, V> pair(K key, V value) {
        return new Pair<K, V>(key, value);
    }

    protected Integer toInt(int i) {
        return Integer.valueOf(i);
    }

    protected <T >Set<T> toSet(T... s) {
        return new HashSet<T>(Arrays.asList(s));
    }

    protected static Comment comment(Integer id) {
        return new Comment(id, "comment " + id);
    }

    protected static Projectable projectable(final Object[]... rows) {
        return new AbstractProjectable() {
            @Override
            public <T> CloseableIterator<T> iterate(Expression<T> arg) {
                return (CloseableIterator)iterator(rows);
            }
        };
    }

    protected static Object[] row(Object... row) {
        return row;
    }

    protected static CloseableIterator<Tuple> iterator(Object[]... rows) {
        List<Tuple> tuples = Lists.newArrayList();
        for (Object[] row : rows) {
            tuples.add(new MockTuple(row));
        }
        return new IteratorAdapter<Tuple>(tuples.iterator());
    }


}
