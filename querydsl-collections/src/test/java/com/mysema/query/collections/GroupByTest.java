package com.mysema.query.collections;


import static com.mysema.query.group.GroupBy.groupBy;
import static com.mysema.query.group.GroupBy.list;
import static com.mysema.query.group.GroupBy.map;
import static com.mysema.query.group.GroupBy.max;
import static com.mysema.query.group.GroupBy.min;
import static com.mysema.query.group.GroupBy.set;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;

import com.mysema.query.group.Group;
import com.mysema.query.types.ConstructorExpression;
import com.mysema.query.types.Projections;

public class GroupByTest {
    
    private static final List<User> users = Arrays.asList(new User("Bob"), new User("Jane"), new User("Jack"));
    
    private static final List<Post> posts = Arrays.asList(
            new Post(1, "Post 1", users.get(0)), 
            new Post(2, "Post 2", users.get(0)),
            new Post(3, "Post 3", users.get(1)));
    
    private static final List<Comment> comments = Arrays.asList( 
            new Comment(1, "Comment 1", users.get(0), posts.get(0)),
            new Comment(2, "Comment 2", users.get(1), posts.get(1)),
            new Comment(3, "Comment 3", users.get(2), posts.get(1)),
            new Comment(4, "Comment 4", users.get(0), posts.get(2)),
            new Comment(5, "Comment 5", users.get(1), posts.get(2)),
            new Comment(6, "Comment 6", users.get(2), posts.get(2)));
    
    private static final QUser user = QUser.user;
    
    private static final QComment comment = QComment.comment;
    
    private static final QPost post = QPost.post;
    
    private static final ConstructorExpression<Comment> qComment = QComment.create(comment.id, comment.text);
        
    @Test
    public void Group_Min() {
        Map<Integer, Group> results = MiniApi.from(post, posts).from(comment, comments)
            .where(comment.post.id.eq(post.id))
            .transform(groupBy(post.id).as(min(comment.text)));
        
        assertEquals("Comment 1", results.get(1).getOne(comment.text));
        assertEquals("Comment 2", results.get(2).getOne(comment.text));
        assertEquals("Comment 4", results.get(3).getOne(comment.text));
    }
    
    @Test
    public void Group_Max() {
        Map<Integer, Group> results = MiniApi.from(post, posts).from(comment, comments)
            .where(comment.post.id.eq(post.id))
            .transform(groupBy(post.id).as(max(comment.text)));
        
        assertEquals("Comment 1", results.get(1).getOne(comment.text));
        assertEquals("Comment 3", results.get(2).getOne(comment.text));
        assertEquals("Comment 6", results.get(3).getOne(comment.text));
    }
    
    @Test 
    public void Group_Order() {       
        Map<Integer, Group> results = MiniApi.from(post, posts).from(comment, comments)
            .where(comment.post.id.eq(post.id))
            .transform(groupBy(post.id).as(post.name, set(comment.id)));
        
        assertEquals(3, results.size());
    }
    
    @Test
    public void First_Set_And_List() {       
        Map<Integer, Group> results = MiniApi.from(post, posts).from(comment, comments)
            .where(comment.post.id.eq(post.id))
            .transform(groupBy(post.id).as(post.name, set(comment.id), list(comment.text)));
        
        Group group = results.get(1);
        assertEquals(toInt(1), group.getOne(post.id));
        assertEquals("Post 1", group.getOne(post.name));
        assertEquals(toSet(1), group.getSet(comment.id));
        assertEquals(Arrays.asList("Comment 1"), group.getList(comment.text));
    }
    
    @Test
    @Ignore
    public void Group_By_Null() {        
        Map<Integer, Group> results = MiniApi.from(post, posts).from(comment, comments)
            .where(comment.post.id.eq(post.id))
            .transform(groupBy(post.id).as(post.name, set(comment.id), list(comment.text)));
        
        Group group = results.get(null);
        assertNull(group.getOne(post.id));
        assertEquals("null post", group.getOne(post.name));
        assertEquals(toSet(7, 8), group.getSet(comment.id));
        assertEquals(Arrays.asList("comment 7", "comment 8"), group.getList(comment.text));
          
    }
        
//    @Test(expected=NoSuchElementException.class)
//    public void NoSuchElementException() {       
//        Map<Integer, Group> results = BASIC_RESULTS.transform(
//            groupBy(postId, postName, set(commentId), list(commentText)));
//        
//        Group group = results.get(1);
//        group.getSet(qComment);
//    }
    
    @Test(expected=ClassCastException.class)
    public void ClassCastException() {        
        Map<Integer, Group> results = MiniApi.from(post, posts).from(comment, comments)
            .where(comment.post.id.eq(post.id))
            .transform(groupBy(post.id).as(post.name, set(comment.id), list(comment.text)));
        
        Group group = results.get(1);
        group.getList(comment.id);
    }
    
    @Test
    @Ignore
    public void Map() {
        Map<Integer, Group> results = MiniApi.from(post, posts).from(comment, comments)
            .where(comment.post.id.eq(post.id))
            .transform(groupBy(post.id).as(post.name, map(comment.id, comment.text)));
        
        Group group = results.get(1);         
        Map<Integer, String> comments = group.getMap(comment.id, comment.text);
        assertEquals(1, comments.size());
//        assertEquals("comment 2", comments.get(2));
    }

    @Test
    public void Array_Access() {        
        Map<Integer, Group> results = MiniApi.from(post, posts).from(comment, comments)
            .where(comment.post.id.eq(post.id))
            .transform(groupBy(post.id).as(post.name, set(comment.id), list(comment.text)));
        
        Group group = results.get(1);
        Object[] array = group.toArray();
        assertEquals(toInt(1), array[0]);
        assertEquals("Post 1", array[1]);
        assertEquals(toSet(1), array[2]);
        assertEquals(Arrays.asList("Comment 1"), array[3]);
    }
    
    @Test
    public void Transform_Results() {        
        Map<Integer, Post> results = MiniApi.from(post, posts).from(comment, comments)
            .where(comment.post.id.eq(post.id))
            .transform(groupBy(post.id).as(QPost.create(post.id, post.name, set(qComment))));
        
        Post post = results.get(1);
        assertNotNull(post);
        assertEquals(1, post.getId());
        assertEquals("Post 1", post.getName());
        assertEquals(1, post.getComments().size());
    }
    
    @Test
    public void Transform_As_Bean() {
        Map<Integer, Post> results = MiniApi.from(post, posts).from(comment, comments)
            .where(comment.post.id.eq(post.id))
            .transform(groupBy(post.id).as(Projections.bean(Post.class, post.id, post.name, set(qComment).as("comments"))));
        
        Post post = results.get(1);
        assertNotNull(post);
        assertEquals(1, post.getId());
        assertEquals("Post 1", post.getName());
        assertEquals(1, post.getComments().size());
    }
        
    
    @Test
    public void OneToOneToMany_Projection() {
        Map<String, User> results = MiniApi.from(user, users).from(post, posts).from(comment, comments)
            .where(user.name.eq(post.user.name), post.id.eq(comment.post.id))
            .transform(groupBy(user.name).as(Projections.constructor(User.class, user.name, 
                    QPost.create(post.id, post.name, set(qComment)))));                    
        
        assertEquals(2, results.size());
        
        User user = results.get("Jane");
        Post post = user.getLatestPost();
        assertEquals(3, post.getId());
        assertEquals("Post 3", post.getName());
        assertEquals(3, post.getComments().size());
    }
    
    
    @Test
    public void OneToOneToMany_Projection_As_Bean() {
        Map<String, User> results = MiniApi.from(user, users).from(post, posts).from(comment, comments)
            .where(user.name.eq(post.user.name), post.id.eq(comment.post.id))
            .transform(groupBy(user.name).as(Projections.bean(User.class, user.name, 
                    Projections.bean(Post.class, post.id, post.name, set(qComment).as("comments")).as("latestPost"))));
        
        assertEquals(2, results.size());
        
        User user = results.get("Jane");
        Post post = user.getLatestPost();
        assertEquals(3, post.getId());
        assertEquals("Post 3", post.getName());
        assertEquals(3, post.getComments().size());
    }
    
    @Test
    public void OneToOneToMany_Projection_As_Bean_And_Constructor() {
        Map<String, User> results = MiniApi.from(user, users).from(post, posts).from(comment, comments)
            .where(user.name.eq(post.user.name), post.id.eq(comment.post.id))
            .transform(groupBy(user.name).as(Projections.bean(User.class, user.name, 
                QPost.create(post.id, post.name, set(qComment)).as("latestPost"))));
        
        assertEquals(2, results.size());
        
        User user = results.get("Jane");
        Post post = user.getLatestPost();
        assertEquals(3, post.getId());
        assertEquals("Post 3", post.getName());
        assertEquals(3, post.getComments().size());
    }
    
    private Integer toInt(int i) {
        return Integer.valueOf(i);
    }
    
    private <T >Set<T> toSet(T... s) {
        return new HashSet<T>(Arrays.asList(s));
    }
    
    
}
