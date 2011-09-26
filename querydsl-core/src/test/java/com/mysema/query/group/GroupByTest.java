package com.mysema.query.group;


import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

import java.util.AbstractSet;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.lang.mutable.MutableInt;
import org.junit.Test;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.commons.lang.IteratorAdapter;
import com.mysema.commons.lang.Pair;
import com.mysema.query.Projectable;
import com.mysema.query.support.AbstractProjectable;
import com.mysema.query.types.ConstructorExpression;
import com.mysema.query.types.Expression;
import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.expr.StringExpression;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;

public class GroupByTest {

    private static final NumberExpression<Integer> postId = new NumberPath<Integer>(Integer.class, "postId");

    private static final StringExpression userName = new StringPath("userName");

    private static final StringExpression postName = new StringPath("postName");

    private static final NumberExpression<Integer> commentId = new NumberPath<Integer>(Integer.class, "commentId");

    private static final StringExpression commentText = new StringPath("commentText");

    private static final GroupDefinition<Integer, String> constant = new AbstractGroupDefinition<Integer, String>(commentId) {

        @Override
        public GroupCollector<String> createGroupCollector() {
            return new GroupCollector<String>() {

                @Override
                public void add(Object o) {
                }

                @Override
                public String get() {
                    return "constant";
                }
            };
        }
    };
    
    private static final ConstructorExpression<Comment> qComment = 
        new ConstructorExpression<GroupByTest.Comment>(
                GroupByTest.Comment.class, 
                new Class[] { Integer.class, String.class },
                commentId, commentText
        );
    
    /**
     * TODO: How to project an inner group, i.e. [User] 1->1 [Post] 1->N [Comment]
     */
    public static class User {
        public final String name;
        public final Post latestPost;
        public User(String name, Post latestPost) {
            this.name = name;
            this.latestPost = latestPost;
        }
    }

    public static class Post {
        public final Integer id;
        public final String name;
        public final Set<Comment> comments;
        public Post(Integer id, String name, Set<Comment> comments) {
            this.id = id;
            this.name = name;
            this.comments = comments;
        }
    }
    
    public static class Comment {
        public final Integer id;
        public final String text;
        public Comment(Integer id, String text) {
            this.id = id;
            this.text = text; 
        }
        public int hashCode() {
            return 31*id.hashCode() + text.hashCode();
        }
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (o instanceof Comment) {
                Comment other = (Comment) o;
                return this.id.equals(other.id) && this.text.equals(other.text);
            } else {
                return false;
            }
        }
    }

    private static <K, V> Pair<K, V> pair(K key, V value) {
        return new Pair<K, V>(key, value);
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
    
    private static final Projectable MAP_RESULTS = projectable(
            row(1, "post 1", pair(1, "comment 1")),
            row(1, "post 1", pair(2, "comment 2")),
            row(2, "post 2", pair(5, "comment 5")),
            row(3, "post 3", pair(6, "comment 6")),
            row(null, "null post", pair(7, "comment 7")),
            row(null, "null post", pair(8, "comment 8")),
            row(1, "post 1", pair(3, "comment 3"))
    );

    private static final Projectable POST_W_COMMENTS = projectable(
            row(1, "post 1", comment(1)),
            row(1, "post 1", comment(2)),
            row(2, "post 2", comment(5)),
            row(3, "post 3", comment(6)),
            row(null, "null post", comment(7)),
            row(null, "null post", comment(8)),
            row(1, "post 1", comment(3))
    );

    private static final Projectable POSTS = projectable(
            row(1, "post 1"),
            row(2, "post 2"),
            row(3, "post 3")
    );

    private static final Projectable POST_COMMENTS = projectable(
            row(1, comment(1)),
            row(1, comment(2)),
            row(2, comment(5)),
            row(3, comment(6)),
            row(1, comment(3))
    );
    
    // [ user.name, latestPost(post.id, post.name), latestPost.comments() ]
    private static final Projectable USERS_W_LATEST_POST_AND_COMMENTS = projectable(
            row("John", 1, "post 1", comment(1)),
            row("Jane", 2, "post 2", comment(4)),
            row("John", 1, "post 1", comment(2)),
            row("Jane", 2, "post 2", comment(5)),
            row("John", 1, "post 1", comment(3))
    );

    @Test 
    public void Group_Order() {
        Map<Integer, Group> results = 
            GroupBy.create(postId).withOne(postName).withSet(commentId)
            .transform(BASIC_RESULTS);
                
        assertEquals(4, results.size());
    }
    
    @Test
    public void First_Set_And_List() {
        Map<Integer, Group> results = 
            GroupBy.create(postId).withOne(postName).withSet(commentId).withList(commentText)
            .transform(BASIC_RESULTS);

        Group group = results.get(1);
        assertEquals(toInt(1), group.getOne(postId));
        assertEquals("post 1", group.getOne(postName));
        assertEquals(toSet(1, 2, 3), group.getSet(commentId));
        assertEquals(Arrays.asList("comment 1", "comment 2", "comment 3"), group.getList(commentText));
    }
    
    @Test
    public void Group_By_Null() {
        Map<Integer, Group> results = 
            GroupBy.create(postId).withOne(postName).withSet(commentId).withList(commentText)
            .transform(BASIC_RESULTS);

        Group group = results.get(null);
        assertNull(group.getOne(postId));
        assertEquals("null post", group.getOne(postName));
        assertEquals(toSet(7, 8), group.getSet(commentId));
        assertEquals(Arrays.asList("comment 7", "comment 8"), group.getList(commentText));
    }
    
    @Test
    public void With_Constant_Column() {
        Map<Integer, Group> results = 
            GroupBy.create(postId).withOne(postName).withGroup(constant)
            .transform(BASIC_RESULTS);
        
        Group group = results.get(1);
        assertEquals("constant", group.getGroup(constant));
    }
    
    @Test(expected=NoSuchElementException.class)
    public void NoSuchElementException() {
        Map<Integer, Group> results = 
            GroupBy.create(postId).withOne(postName).withSet(commentId).withList(commentText)
            .transform(BASIC_RESULTS);

        Group group = results.get(1);
        group.getSet(qComment);
    }
    
    @Test(expected=ClassCastException.class)
    public void ClassCastException() {
        Map<Integer, Group> results = 
            GroupBy.create(postId).withOne(postName).withSet(commentId).withList(commentText)
            .transform(BASIC_RESULTS);

        Group group = results.get(1);
        group.getList(commentId);
    }
    
    @Test
    public void Map() {
        Map<Integer, Group> results = 
            GroupBy.create(postId).withOne(postName).withMap(commentId, commentText)
            .transform(MAP_RESULTS);

        Group group = results.get(1);
        
        Map<Integer, String> comments = group.getMap(commentId, commentText);
        assertEquals(3, comments.size());
        assertEquals("comment 2", comments.get(2));
    }

    @Test
    public void Array_Access() {
        Map<Integer, Group> results = 
            GroupBy.create(postId).withOne(postName).withSet(commentId).withList(commentText)
            .transform(BASIC_RESULTS);

        Group group = results.get(1);
        Object[] array = group.toArray();
        assertEquals(toInt(1), array[0]);
        assertEquals("post 1", array[1]);
        assertEquals(toSet(1, 2, 3), array[2]);
        assertEquals(Arrays.asList("comment 1", "comment 2", "comment 3"), array[3]);
    }
    
    @Test
    public void Transform_Results() {
        Map<Integer, Post> results = 
            // NOTE: This kind of group maps directly to a constructor. It is straight 
            // forward to implement this like: GroupBy...as(Post.class)
            GroupBy.create(postId, postName).withSet(qComment)

            .withValueTransformer(new Transformer<Group, Post>() {
                public Post transform(Group group) {
                    return new Post(group.getOne(postId), group.getOne(postName), group.getSet(qComment));
                }
            })
            
            .transform(POST_W_COMMENTS);

        Post post = results.get(1);
        assertNotNull(post);
        assertEquals(toInt(1), post.id);
        assertEquals("post 1", post.name);
        assertEquals(toSet(comment(1), comment(2), comment(3)), post.comments);
    }
    
    @Test
    public void OneToOneToMany_Projection() {
        
        final Map<String, User> results = 
            // NOTE: This kind of projection cannot be mapped directly to simple GroupBy...as(User.class)
            // Using some kind of constructor expressions would be nice...
            GroupBy.create(userName, postId, postName).withSet(qComment)
            
            .withValueTransformer(new Transformer<Group, User>() {
                public User transform(Group group) {
                    return new User(group.getOne(userName), 
                            new Post(group.getOne(postId), group.getOne(postName), 
                                    group.getSet(qComment)));
                }
            })
            
            .transform(USERS_W_LATEST_POST_AND_COMMENTS);

        assertEquals(2, results.size());
        
        User user = results.get("Jane");
        Post post = user.latestPost;
        assertEquals(toInt(2), post.id);
        assertEquals("post 2", post.name);
        assertEquals(toSet(comment(4), comment(5)), post.comments);
    }
    
    /**
     * Proof-of-concept for lazy batch load in order to avoid N+1 queries.
     */
    @Test
    public void Lazy_Batch_Loader_POC() {
        final MutableInt commentAccessCount = new MutableInt(0);
        final MutableInt commentFetchCount = new MutableInt(0);
        
        final Map<Integer, Post> results = 
            GroupBy.create(postId, postName)
            
            // NOTE: As this should be ProcessorFactory as it's stateful
            .withProcessor(new GroupProcessor<Integer, Map<Integer, Post>>() {

                // postId -> comment
                private Map<Integer, Set<Comment>> comments;
                
                private final Set<Integer> postIds = new HashSet<Integer>();
                
                /**
                 * Collect all postIds
                 */
                @Override
                public boolean accept(Object[] row) {
                    postIds.add((Integer) row[0]);
                    return true;
                }
                
                /**
                 * Batch fetch all comments :
                 * 
                 * <pre>
                 * select * from comment where postId in ({ postIds })
                 * </pre>
                 */
                private void initialize() {
                    if (comments == null) {
                        comments = GroupBy.create(postId).withSet(qComment)
                            .withValueTransformer(new Transformer<Group, Set<Comment>>() {
    
                                @Override
                                public Set<Comment> transform(Group input) {
                                    return input.getSet(qComment);
                                }
                                
                            })
                            .transform(POST_COMMENTS);
                        commentFetchCount.increment();
                    }
               }
                
                /**
                 * Initialize all comments and return comments for postId
                 */
                private Set<Comment> getComments(Integer postId) {
                    initialize();
                    commentAccessCount.increment();
                    return comments.get(postId);
                }
                
                /**
                 * Transform input Groups into Posts with lazily fetched comments. 
                 * All Comments for Posts in this result are fetched by one query. 
                 * 
                 * @return
                 */
                @Override
                public Map<Integer, Post> transform(Map<Integer, Group> input) {

                    // Transform Groups into Posts with lazy collection of Comments
                    return ValueTransformerMap.create(input, new Transformer<Group, Post>() {

                        public Post transform(final Group group) {
                            
                            final Integer pid = group.getOne(postId);
                            
                            return new Post(
                                    pid, 
                                    group.getOne(postName),
                                    // Lazy collection wrapper
                                    new AbstractSet<Comment>() {
                                        
                                        @Override
                                        public Iterator<Comment> iterator() {
                                            return getComments(pid).iterator();
                                        }
                                        
                                        @Override
                                        public int size() {
                                            return getComments(pid).size();
                                        }
                                        
                                    });
                        }
                    });
                }
                
            })
            
            .transform(POSTS);
        
        assertEquals(3, results.size());

        // Post 1
        Post post = results.get(1);
        assertEquals(0, commentFetchCount.intValue());
        assertEquals(0, commentAccessCount.intValue());

        assertEquals(3, post.comments.size()); // Initialize collection
        assertEquals(1, commentFetchCount.intValue());
        assertEquals(1, commentAccessCount.intValue());
        
        Iterator<Comment> iter = post.comments.iterator();
        assertEquals("comment 1", iter.next().text);
        assertEquals("comment 2", iter.next().text);
        assertEquals("comment 3", iter.next().text);
        assertEquals(1, commentFetchCount.intValue()); // No increase in fetchCount
        assertEquals(2, commentAccessCount.intValue()); // One access more
        
        // Post 2
        post = results.get(2);

        assertEquals(1, post.comments.size()); 
        assertEquals(1, commentFetchCount.intValue()); // No increase in fetchCount
        assertEquals(3, commentAccessCount.intValue()); // One access more
        
        iter = post.comments.iterator();
        assertEquals("comment 5", iter.next().text);
        assertEquals(1, commentFetchCount.intValue()); // No increase in fetchCount
        assertEquals(4, commentAccessCount.intValue()); // One access more
        
        // Post 3
        post = results.get(3);

        assertEquals(1, post.comments.size()); 
        assertEquals(1, commentFetchCount.intValue()); // No increase in fetchCount
        assertEquals(5, commentAccessCount.intValue()); // One access more
        
        // NOTE: With simple lazy Post.comments implementation that fetches comments for one
        // post at a time, this sequence would have required 1+3 database queries.
        // With lazy batch fetch, it required only 1+1.
    }
    
    private static Comment comment(Integer id) {
        return new Comment(id, "comment " + id);
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
