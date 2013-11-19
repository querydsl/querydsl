/*
 * Copyright 2011, Mysema Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mysema.query.group;


import static com.mysema.query.group.GroupBy.groupBy;
import static com.mysema.query.group.GroupBy.list;
import static com.mysema.query.group.GroupBy.map;
import static com.mysema.query.group.GroupBy.set;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;

import org.junit.Test;

import com.mysema.query.Projectable;
import com.mysema.query.types.Projections;

public class GroupByMapTest extends AbstractGroupByTest {

    protected static final Projectable BASIC_RESULTS = projectable(
            row(1, "post 1", 1, "comment 1"),
            row(2, "post 2", 4, "comment 4"),
            row(1, "post 1", 2, "comment 2"),
            row(2, "post 2", 5, "comment 5"),
            row(3, "post 3", 6, "comment 6"),
            row(null, "null post", 7, "comment 7"),
            row(null, "null post", 8, "comment 8"),
            row(1, "post 1", 3, "comment 3")
        );

    protected static final Projectable MAP_RESULTS = projectable(
            row(1, "post 1", pair(1, "comment 1")),
            row(1, "post 1", pair(2, "comment 2")),
            row(2, "post 2", pair(5, "comment 5")),
            row(3, "post 3", pair(6, "comment 6")),
            row(null, "null post", pair(7, "comment 7")),
            row(null, "null post", pair(8, "comment 8")),
            row(1, "post 1", pair(3, "comment 3"))
    );

    protected static final Projectable MAP2_RESULTS = projectable(
            row(1, pair(1, "comment 1")),
            row(1, pair(2, "comment 2")),
            row(2, pair(5, "comment 5")),
            row(3, pair(6, "comment 6")),
            row(null, pair(7, "comment 7")),
            row(null,  pair(8, "comment 8")),
            row(1, pair(3, "comment 3"))
    );

    protected static final Projectable POST_W_COMMENTS = projectable(
            row(1, 1, "post 1", comment(1)),
            row(1, 1, "post 1", comment(2)),
            row(2, 2, "post 2", comment(5)),
            row(3, 3, "post 3", comment(6)),
            row(null, null, "null post", comment(7)),
            row(null, null, "null post", comment(8)),
            row(1, 1, "post 1", comment(3))
    );

    protected static final Projectable POST_W_COMMENTS2 = projectable(
            row(1, "post 1", comment(1)),
            row(1, "post 1", comment(2)),
            row(2, "post 2", comment(5)),
            row(3, "post 3", comment(6)),
            row(null, "null post", comment(7)),
            row(null, "null post", comment(8)),
            row(1, "post 1", comment(3))
    );

    // [ user.name, latestPost(post.id, post.name), latestPost.comments() ]
    protected static final Projectable USERS_W_LATEST_POST_AND_COMMENTS = projectable(
            row("John", "John", 1, "post 1", comment(1)),
            row("Jane", "Jane", 2, "post 2", comment(4)),
            row("John", "John", 1, "post 1", comment(2)),
            row("Jane", "Jane", 2, "post 2", comment(5)),
            row("John", "John", 1, "post 1", comment(3))
    );

//    protected static final Projectable USERS_W_LATEST_POST_AND_COMMENTS2 = projectable(
//            row("John", 1, "post 1", comment(1)),
//            row("Jane", 2, "post 2", comment(4)),
//            row("John", 1, "post 1", comment(2)),
//            row("Jane", 2, "post 2", comment(5)),
//            row("John", 1, "post 1", comment(3))
//    );


    @Test
    public void Group_Order() {
        Map<Integer, Group> results = BASIC_RESULTS
            .transform(groupBy(postId).as(postName, set(commentId)));
        assertEquals(4, results.size());
    }

    @Test
    public void First_Set_And_List() {
        Map<Integer, Group> results = BASIC_RESULTS.transform(
            groupBy(postId).as(postName, set(commentId), list(commentText)));

        Group group = results.get(1);
        assertEquals(toInt(1), group.getOne(postId));
        assertEquals("post 1", group.getOne(postName));
        assertEquals(toSet(1, 2, 3), group.getSet(commentId));
        assertEquals(Arrays.asList("comment 1", "comment 2", "comment 3"), group.getList(commentText));
    }

    @Test
    public void Group_By_Null() {
        Map<Integer, Group> results = BASIC_RESULTS.transform(
            groupBy(postId).as(postName, set(commentId), list(commentText)));

        Group group = results.get(null);
        assertNull(group.getOne(postId));
        assertEquals("null post", group.getOne(postName));
        assertEquals(toSet(7, 8), group.getSet(commentId));
        assertEquals(Arrays.asList("comment 7", "comment 8"), group.getList(commentText));

    }

    @Test(expected=NoSuchElementException.class)
    public void NoSuchElementException() {
        Map<Integer, Group> results = BASIC_RESULTS.transform(
            groupBy(postId).as(postName, set(commentId), list(commentText)));

        Group group = results.get(1);
        group.getSet(qComment);
    }

    @Test(expected=ClassCastException.class)
    public void ClassCastException() {
        Map<Integer, Group> results = BASIC_RESULTS.transform(
            groupBy(postId).as(postName, set(commentId), list(commentText)));

        Group group = results.get(1);
        group.getList(commentId);
    }

    @Test
    public void Map() {
        Map<Integer, Group> results = MAP_RESULTS.transform(
            groupBy(postId).as(postName, map(commentId, commentText)));

        Group group = results.get(1);

        Map<Integer, String> comments = group.getMap(commentId, commentText);
        assertEquals(3, comments.size());
        assertEquals("comment 2", comments.get(2));
    }

    @Test
    public void Map2() {
        Map<Integer, Map<Integer, String>> results = MAP2_RESULTS.transform(
            groupBy(postId).as(map(commentId, commentText)));

        Map<Integer, String> comments = results.get(1);
        assertEquals(3, comments.size());
        assertEquals("comment 2", comments.get(2));
    }

    @Test
    public void Array_Access() {
        Map<Integer, Group> results = BASIC_RESULTS.transform(
            groupBy(postId).as(postName, set(commentId), list(commentText)));

        Group group = results.get(1);
        Object[] array = group.toArray();
        assertEquals(toInt(1), array[0]);
        assertEquals("post 1", array[1]);
        assertEquals(toSet(1, 2, 3), array[2]);
        assertEquals(Arrays.asList("comment 1", "comment 2", "comment 3"), array[3]);
    }

    @Test
    public void Transform_Results() {
        Map<Integer, Post> results = POST_W_COMMENTS.transform(
                groupBy(postId).as(Projections.constructor(Post.class, postId, postName, set(qComment))));

        Post post = results.get(1);
        assertNotNull(post);
        assertEquals(toInt(1), post.getId());
        assertEquals("post 1", post.getName());
        assertEquals(toSet(comment(1), comment(2), comment(3)), post.getComments());
    }

    @Test
    public void Transform_Via_GroupByProjection() {
        Map<Integer, Post> results = POST_W_COMMENTS2.transform(
                new GroupByProjection<Integer, Post>(postId, postName, set(qComment)) {
                    @Override
                    protected Post transform(Group group) {
                        return new Post(
                                group.getOne(postId),
                                group.getOne(postName),
                                group.getSet(qComment));

                    }
                });

        Post post = results.get(1);
        assertNotNull(post);
        assertEquals(toInt(1), post.getId());
        assertEquals("post 1", post.getName());
        assertEquals(toSet(comment(1), comment(2), comment(3)), post.getComments());
    }

    @Test
    public void Transform_As_Bean() {
        Map<Integer, Post> results = POST_W_COMMENTS.transform(
                groupBy(postId).as(Projections.bean(Post.class, postId, postName, set(qComment).as("comments"))));

        Post post = results.get(1);
        assertNotNull(post);
        assertEquals(toInt(1), post.getId());
        assertEquals("post 1", post.getName());
        assertEquals(toSet(comment(1), comment(2), comment(3)), post.getComments());
    }


    @Test
    public void OneToOneToMany_Projection() {
        Map<String, User> results = USERS_W_LATEST_POST_AND_COMMENTS.transform(
            groupBy(userName).as(Projections.constructor(User.class, userName,
                Projections.constructor(Post.class, postId, postName, set(qComment)))));

        assertEquals(2, results.size());

        User user = results.get("Jane");
        Post post = user.getLatestPost();
        assertEquals(toInt(2), post.getId());
        assertEquals("post 2", post.getName());
        assertEquals(toSet(comment(4), comment(5)), post.getComments());
    }

    @Test
    public void OneToOneToMany_Projection_As_Bean() {
        Map<String, User> results = USERS_W_LATEST_POST_AND_COMMENTS.transform(
            groupBy(userName).as(Projections.bean(User.class, userName,
                Projections.bean(Post.class, postId, postName, set(qComment).as("comments")).as("latestPost"))));

        assertEquals(2, results.size());

        User user = results.get("Jane");
        Post post = user.getLatestPost();
        assertEquals(toInt(2), post.getId());
        assertEquals("post 2", post.getName());
        assertEquals(toSet(comment(4), comment(5)), post.getComments());
    }

    @Test
    public void OneToOneToMany_Projection_As_Bean_And_Constructor() {
        Map<String, User> results = USERS_W_LATEST_POST_AND_COMMENTS.transform(
            groupBy(userName).as(Projections.bean(User.class, userName,
                Projections.constructor(Post.class, postId, postName, set(qComment)).as("latestPost"))));

        assertEquals(2, results.size());

        User user = results.get("Jane");
        Post post = user.getLatestPost();
        assertEquals(toInt(2), post.getId());
        assertEquals("post 2", post.getName());
        assertEquals(toSet(comment(4), comment(5)), post.getComments());
    }

}
