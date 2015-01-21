package com.querydsl.core.group;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.mysema.commons.lang.CloseableIterator;
import com.mysema.commons.lang.IteratorAdapter;
import com.mysema.commons.lang.Pair;
import com.querydsl.core.Projectable;
import com.querydsl.core.Tuple;
import com.querydsl.core.support.AbstractProjectable;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.expr.NumberExpression;
import com.querydsl.core.types.expr.StringExpression;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.SimplePath;
import com.querydsl.core.types.path.StringPath;

public abstract class AbstractGroupByTest {

    protected static final Projectable BASIC_RESULTS = projectable(
            row(null, "null post", 7, "comment 7"),
            row(null, "null post", 8, "comment 8"),
            row(1, "post 1", 1, "comment 1"),
            row(1, "post 1", 2, "comment 2"),
            row(1, "post 1", 3, "comment 3"),
            row(2, "post 2", 4, "comment 4"),
            row(2, "post 2", 5, "comment 5"),
            row(3, "post 3", 6, "comment 6")
    );

    protected static final Projectable MAP_RESULTS = projectable(
            row(null, "null post", pair(7, "comment 7")),
            row(null, "null post", pair(8, "comment 8")),
            row(1, "post 1", pair(1, "comment 1")),
            row(1, "post 1", pair(2, "comment 2")),
            row(1, "post 1", pair(3, "comment 3")),
            row(2, "post 2", pair(5, "comment 5")),
            row(3, "post 3", pair(6, "comment 6"))
    );

    protected static final Projectable MAP2_RESULTS = projectable(
            row(null, pair(7, "comment 7")),
            row(null,  pair(8, "comment 8")),
            row(1, pair(1, "comment 1")),
            row(1, pair(2, "comment 2")),
            row(1, pair(3, "comment 3")),
            row(2, pair(5, "comment 5")),
            row(3, pair(6, "comment 6"))
    );

    protected static final Projectable MAP3_RESULTS = projectable(
        row(1, pair(1, pair(1, "comment 1"))),
        row(1, pair(1, pair(2, "comment 2"))),
        row(2, pair(2, pair(5, "comment 5"))),
        row(3, pair(3, pair(6, "comment 6"))),
        row(null, pair(null, pair(7, "comment 7"))),
        row(null, pair(null,  pair(8, "comment 8"))),
        row(1, pair(1, pair(3, "comment 3")))
    );

    protected static final Projectable MAP4_RESULTS = projectable(
        row(1, pair(pair(1, "comment 1"), "post 1")),
        row(1, pair(pair(1, "comment 2"), "post 1")),
        row(2, pair(pair(2, "comment 5"), "post 2")),
        row(3, pair(pair(3, "comment 6"), "post 3")),
        row(null, pair(pair(null, "comment 7"), "null post")),
        row(null, pair(pair(null, "comment 8"), "null post")),
        row(1, pair(pair(1, "comment 3"), "post 1"))
    );

    protected static final Projectable POST_W_COMMENTS = projectable(
            row(null, null, "null post", comment(7)),
            row(null, null, "null post", comment(8)),
            row(1, 1, "post 1", comment(1)),
            row(1, 1, "post 1", comment(2)),
            row(1, 1, "post 1", comment(3)),
            row(2, 2, "post 2", comment(5)),
            row(3, 3, "post 3", comment(6))
    );

    protected static final Projectable POST_W_COMMENTS2 = projectable(
            row(null, "null post", comment(7)),
            row(null, "null post", comment(8)),
            row(1, "post 1", comment(1)),
            row(1, "post 1", comment(2)),
            row(1, "post 1", comment(3)),
            row(2, "post 2", comment(5)),
            row(3, "post 3", comment(6))
    );

    // [ user.name, latestPost(post.id, post.name), latestPost.comments() ]
    protected static final Projectable USERS_W_LATEST_POST_AND_COMMENTS = projectable(
            row("Jane", "Jane", 2, "post 2", comment(4)),
            row("Jane", "Jane", 2, "post 2", comment(5)),
            row("John", "John", 1, "post 1", comment(1)),
            row("John", "John", 1, "post 1", comment(2)),
            row("John", "John", 1, "post 1", comment(3))
    );

//    protected static final Projectable USERS_W_LATEST_POST_AND_COMMENTS2 = projectable(
//            row("John", 1, "post 1", comment(1)),
//            row("Jane", 2, "post 2", comment(4)),
//            row("John", 1, "post 1", comment(2)),
//            row("Jane", 2, "post 2", comment(5)),
//            row("John", 1, "post 1", comment(3))
//    );

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

            @Override
            public CloseableIterator<Tuple> iterate(Expression<?>... args) {
                return iterator(rows);
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

    public abstract void Group_Order();

    public abstract void First_Set_And_List();

    public abstract void Group_By_Null();

    public abstract void NoSuchElementException();

    public abstract void ClassCastException();

    public abstract void Map();

    public abstract void Map2();
    
    public abstract void Map3();

    public abstract void Map4();

    public abstract void Array_Access();

    public abstract void Transform_Results();

    public abstract void Transform_As_Bean();

    public abstract void OneToOneToMany_Projection();
    
    public abstract void OneToOneToMany_Projection_As_Bean();

    public abstract void OneToOneToMany_Projection_As_Bean_And_Constructor();
}
