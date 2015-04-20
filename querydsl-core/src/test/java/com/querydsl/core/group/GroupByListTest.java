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
package com.querydsl.core.group;


import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static com.querydsl.core.group.GroupBy.map;
import static com.querydsl.core.group.GroupBy.set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.junit.Test;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.commons.lang.IteratorAdapter;
import com.mysema.commons.lang.Pair;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;

public class GroupByListTest extends AbstractGroupByTest {
    
    @Test
    public void Group_Order() {
        List<Group> results = BASIC_RESULTS
            .transform(groupBy(postId).list(postName, set(commentId)));

        assertEquals(4, results.size());
    }

    @Test
    public void First_Set_And_List() {
        List<Group> results = BASIC_RESULTS.transform(
            groupBy(postId).list(postName, set(commentId), list(commentText)));

        assertEquals(4, results.size());

        Group group = results.get(1);
        assertEquals(toInt(1), group.getOne(postId));
        assertEquals("post 1", group.getOne(postName));
        assertEquals(toSet(1, 2, 3), group.getSet(commentId));
        assertEquals(Arrays.asList("comment 1", "comment 2", "comment 3"), group.getList(commentText));
    }

    @Test
    public void Group_By_Null() {
        List<Group> results = BASIC_RESULTS.transform(
            groupBy(postId).list(postName, set(commentId), list(commentText)));

        assertEquals(4, results.size());

        Group group = results.get(0);
        assertNull(group.getOne(postId));
        assertEquals("null post", group.getOne(postName));
        assertEquals(toSet(7, 8), group.getSet(commentId));
        assertEquals(Arrays.asList("comment 7", "comment 8"), group.getList(commentText));

    }

    @Test(expected=NoSuchElementException.class)
    public void NoSuchElementException() {
        List<Group> results = BASIC_RESULTS.transform(
            groupBy(postId).list(postName, set(commentId), list(commentText)));

        assertEquals(4, results.size());

        Group group = results.get(1);
        group.getSet(qComment);
    }

    @Test(expected=ClassCastException.class)
    public void ClassCastException() {
        List<Group> results = BASIC_RESULTS.transform(
            groupBy(postId).list(postName, set(commentId), list(commentText)));

        assertEquals(4, results.size());

        Group group = results.get(1);
        group.getList(commentId);
    }

    @Test
    public void Map() {
        List<Group> results = MAP_RESULTS.transform(
            groupBy(postId).list(postName, map(commentId, commentText)));

        assertEquals(4, results.size());

        Group group = results.get(1);
        Map<Integer, String> comments = group.getMap(commentId, commentText);
        assertEquals(3, comments.size());
        assertEquals("comment 2", comments.get(2));
    }

    @Test
    public void Map2() {
        List<Map<Integer, String>> results = MAP2_RESULTS.transform(
            groupBy(postId).list(map(commentId, commentText)));

        assertEquals(4, results.size());

        Map<Integer, String> comments = results.get(1);
        assertEquals(3, comments.size());
        assertEquals("comment 2", comments.get(2));
    }
 
    @Test
    public void Map3() {        
        List<Map<Integer, Map<Integer, String>>> actual = MAP3_RESULTS.transform(
            groupBy(postId).list(map(postId, map(commentId, commentText))));
        
        Object postId = null;
        Map<Integer, Map<Integer, String>> posts = null;
        List<Map<Integer, Map<Integer, String>>> expected = new LinkedList<Map<Integer, Map<Integer, String>>>();
        for (Iterator<Tuple> iterator = MAP3_RESULTS.iterate(); iterator.hasNext();) {
            Tuple tuple = iterator.next();
            Object[] array = tuple.toArray();
            
            if (posts == null || !(postId == array[0] || postId != null && postId.equals(array[0]))) {
                posts = new LinkedHashMap<Integer, Map<Integer,String>>();
                expected.add(posts);
            }
            postId = array[0];
            @SuppressWarnings("unchecked")
            Pair<Integer, Pair<Integer, String>> pair = (Pair<Integer, Pair<Integer, String>>) array[1];
            Integer first = pair.getFirst();
            Map<Integer, String> comments = posts.get(first);
            if (comments == null) {
                comments = new LinkedHashMap<Integer, String>();
                posts.put(first, comments);
            }
            Pair<Integer, String> second = pair.getSecond();
            comments.put(second.getFirst(), second.getSecond());
        }      
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void Map4() {    
        CloseableIterator<Map<Map<Integer, String>, String>> results = MAP4_RESULTS.transform(
            groupBy(postId).iterate(map(map(postId, commentText), postName)));
        List<Map<Map<Integer, String>, String>> actual = IteratorAdapter.asList(results);
        
        Object commentId = null;
        Map<Map<Integer, String>, String> comments = null;
        List<Map<Map<Integer, String>, String>> expected = new LinkedList<Map<Map<Integer, String>, String>>();
        for (Iterator<Tuple> iterator = MAP4_RESULTS.iterate(); iterator.hasNext();) {
            Tuple tuple = iterator.next();
            Object[] array = tuple.toArray();
 
            if (comments == null || !(commentId == array[0] || commentId != null && commentId.equals(array[0]))) {
                comments = new LinkedHashMap<Map<Integer, String>, String>();
                expected.add(comments);
            }
            commentId = array[0];
            @SuppressWarnings("unchecked")
            Pair<Pair<Integer, String>, String> pair = (Pair<Pair<Integer, String>, String>) array[1];
            Pair<Integer, String> first = pair.getFirst(); 
            Map<Integer, String> posts = Collections.singletonMap(first.getFirst(), first.getSecond());
            comments.put(posts, pair.getSecond());
        }      
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void Array_Access() {
        List<Group> results = BASIC_RESULTS.transform(
            groupBy(postId).list(postName, set(commentId), list(commentText)));

        assertEquals(4, results.size());

        Group group = results.get(1);
        Object[] array = group.toArray();
        assertEquals(toInt(1), array[0]);
        assertEquals("post 1", array[1]);
        assertEquals(toSet(1, 2, 3), array[2]);
        assertEquals(Arrays.asList("comment 1", "comment 2", "comment 3"), array[3]);
    }

    @Test
    public void Transform_Results() {
        List<Post> results = POST_W_COMMENTS.transform(
                groupBy(postId).list(Projections.constructor(Post.class, postId, postName, set(qComment))));

        assertEquals(4, results.size());

        Post post = results.get(1);
        assertNotNull(post);
        assertEquals(toInt(1), post.getId());
        assertEquals("post 1", post.getName());
        assertEquals(toSet(comment(1), comment(2), comment(3)), post.getComments());
    }

    @Test
    public void Transform_As_Bean() {
        List<Post> results = POST_W_COMMENTS.transform(
                groupBy(postId).list(Projections.bean(Post.class, postId, postName, set(qComment).as("comments"))));

        assertEquals(4, results.size());

        Post post = results.get(1);
        assertNotNull(post);
        assertEquals(toInt(1), post.getId());
        assertEquals("post 1", post.getName());
        assertEquals(toSet(comment(1), comment(2), comment(3)), post.getComments());
    }


    @Test
    public void OneToOneToMany_Projection() {
        List<User> results = USERS_W_LATEST_POST_AND_COMMENTS.transform(
            groupBy(userName).list(Projections.constructor(User.class, userName,
                Projections.constructor(Post.class, postId, postName, set(qComment)))));

        assertEquals(2, results.size());

        User user = results.get(0);
        Post post = user.getLatestPost();
        assertEquals(toInt(2), post.getId());
        assertEquals("post 2", post.getName());
        assertEquals(toSet(comment(4), comment(5)), post.getComments());
    }

    @Test
    public void OneToOneToMany_Projection_As_Bean() {
        List<User> results = USERS_W_LATEST_POST_AND_COMMENTS.transform(
            groupBy(userName).list(Projections.bean(User.class, userName,
                Projections.bean(Post.class, postId, postName, set(qComment).as("comments")).as("latestPost"))));

        assertEquals(2, results.size());

        User user = results.get(0);
        Post post = user.getLatestPost();
        assertEquals(toInt(2), post.getId());
        assertEquals("post 2", post.getName());
        assertEquals(toSet(comment(4), comment(5)), post.getComments());
    }

    @Test
    public void OneToOneToMany_Projection_As_Bean_And_Constructor() {
        List<User> results = USERS_W_LATEST_POST_AND_COMMENTS.transform(
            groupBy(userName).list(Projections.bean(User.class, userName,
                Projections.constructor(Post.class, postId, postName, set(qComment)).as("latestPost"))));

        assertEquals(2, results.size());

        User user = results.get(0);
        Post post = user.getLatestPost();
        assertEquals(toInt(2), post.getId());
        assertEquals("post 2", post.getName());
        assertEquals(toSet(comment(4), comment(5)), post.getComments());
    }

}
