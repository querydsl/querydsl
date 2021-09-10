/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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

import static com.querydsl.core.group.GroupBy.*;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.*;

import com.querydsl.core.util.Pair;
import org.junit.Test;

import com.querydsl.core.ResultTransformer;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;

import java.math.MathContext;
import java.math.RoundingMode;

public class GroupByMapTest extends AbstractGroupByTest {

    @Test
    public void compile() {
        StringExpression str = Expressions.stringPath("str");
        GroupExpression<String, String> strGroup = new GOne<String>(str);
        GroupBy.sortedMap(strGroup, str, null);
        GroupBy.sortedMap(str, strGroup, null);
    }

    @Test
    public void group_order() {
        Map<Integer, Group> results = BASIC_RESULTS
            .transform(groupBy(postId).as(postName, set(commentId)));
        assertEquals(4, results.size());
    }

    @Test
    public void set_by_sorted() {
        Map<Integer, Group> results = BASIC_RESULTS_UNORDERED
                .transform(groupBy(postId).as(postName, sortedSet(commentId)));

        Group group = results.get(1);
        Iterator<Integer> it = group.getSet(commentId).iterator();
        assertEquals(1, it.next().intValue());
        assertEquals(2, it.next().intValue());
        assertEquals(3, it.next().intValue());
    }

    @Test
    public void set_by_sorted_reverse() {
        Map<Integer, Group> results = BASIC_RESULTS_UNORDERED
                .transform(groupBy(postId).as(postName, sortedSet(commentId, Comparator.reverseOrder())));

        Group group = results.get(1);
        Iterator<Integer> it = group.getSet(commentId).iterator();
        assertEquals(3, it.next().intValue());
        assertEquals(2, it.next().intValue());
        assertEquals(1, it.next().intValue());
    }

    @Test
    public void first_set_and_list() {
        Map<Integer, Group> results = BASIC_RESULTS.transform(
            groupBy(postId).as(postName, set(commentId), list(commentText)));

        Group group = results.get(1);
        assertEquals(toInt(1), group.getOne(postId));
        assertEquals("post 1", group.getOne(postName));
        assertEquals(toSet(1, 2, 3), group.getSet(commentId));
        assertEquals(Arrays.asList("comment 1", "comment 2", "comment 3"), group.getList(commentText));
    }

    @Test
    public void group_by_null() {
        Map<Integer, Group> results = BASIC_RESULTS.transform(
            groupBy(postId).as(postName, set(commentId), list(commentText)));

        Group group = results.get(null);
        assertNull(group.getOne(postId));
        assertEquals("null post", group.getOne(postName));
        assertEquals(toSet(7, 8), group.getSet(commentId));
        assertEquals(Arrays.asList("comment 7", "comment 8"), group.getList(commentText));

    }

    @Test(expected = NoSuchElementException.class)
    public void noSuchElementException() {
        Map<Integer, Group> results = BASIC_RESULTS.transform(
            groupBy(postId).as(postName, set(commentId), list(commentText)));

        Group group = results.get(1);
        group.getSet(qComment);
    }

    @Test(expected = ClassCastException.class)
    public void classCastException() {
        Map<Integer, Group> results = BASIC_RESULTS.transform(
            groupBy(postId).as(postName, set(commentId), list(commentText)));

        Group group = results.get(1);
        group.getList(commentId);
    }

    @Test
    public void map1() {
        Map<Integer, Group> results = MAP_RESULTS.transform(
            groupBy(postId).as(postName, map(commentId, commentText)));

        Group group = results.get(1);

        Map<Integer, String> comments = group.getMap(commentId, commentText);
        assertEquals(3, comments.size());
        assertEquals("comment 2", comments.get(2));
    }

    @Test
    public void map_sorted() {
        Map<Integer, Group> results = MAP_RESULTS.transform(
                groupBy(postId).as(postName, sortedMap(commentId, commentText)));

        Group group = results.get(1);

        Iterator<Map.Entry<Integer, String>> it = group.getMap(commentId, commentText).entrySet().iterator();
        assertEquals(1, it.next().getKey().intValue());
        assertEquals(2, it.next().getKey().intValue());
        assertEquals(3, it.next().getKey().intValue());
    }

    @Test
    public void map_sorted_reverse() {
        Map<Integer, Group> results = MAP_RESULTS.transform(
                groupBy(postId).as(postName, sortedMap(commentId, commentText, Comparator.reverseOrder())));

        Group group = results.get(1);

        Iterator<Map.Entry<Integer, String>> it = group.getMap(commentId, commentText).entrySet().iterator();
        assertEquals(3, it.next().getKey().intValue());
        assertEquals(2, it.next().getKey().intValue());
        assertEquals(1, it.next().getKey().intValue());
    }


    @Test
    public void map2() {
        Map<Integer, Map<Integer, String>> results = MAP2_RESULTS.transform(
            groupBy(postId).as(map(commentId, commentText)));

        Map<Integer, String> comments = results.get(1);
        assertEquals(3, comments.size());
        assertEquals("comment 2", comments.get(2));
    }

    @Test
    public void map3() {
        Map<Integer, Map<Integer, Map<Integer, String>>> actual = MAP3_RESULTS.transform(
            groupBy(postId).as(map(postId, map(commentId, commentText))));

        Map<Integer, Map<Integer, Map<Integer, String>>> expected = new LinkedHashMap<Integer, Map<Integer, Map<Integer, String>>>();
        for (Iterator<Tuple> iterator = MAP3_RESULTS.iterate(); iterator.hasNext();) {
            Tuple tuple = iterator.next();
            Object[] array = tuple.toArray();

            Map<Integer, Map<Integer, String>> posts = expected.get(array[0]);
            if (posts == null) {
                posts = new LinkedHashMap<Integer, Map<Integer,String>>();
                expected.put((Integer) array[0], posts);
            }
            @SuppressWarnings("unchecked")
            Pair<Integer, Pair<Integer, String>> pair = (Pair<Integer, Pair<Integer, String>>) array[1];
            Integer first = pair.getFirst();
            Map<Integer, String> comments = posts.computeIfAbsent(first, k -> new LinkedHashMap<Integer, String>());
            Pair<Integer, String> second = pair.getSecond();
            comments.put(second.getFirst(), second.getSecond());
        }
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void map4() {
        Map<Integer, Map<Map<Integer, String>, String>> actual = MAP4_RESULTS.transform(
            groupBy(postId).as(map(map(postId, commentText), postName)));

        Map<Integer, Map<Map<Integer, String>, String>> expected = new LinkedHashMap<Integer, Map<Map<Integer, String>, String>>();
        for (Iterator<Tuple> iterator = MAP4_RESULTS.iterate(); iterator.hasNext();) {
            Tuple tuple = iterator.next();
            Object[] array = tuple.toArray();

            Map<Map<Integer, String>, String> comments = expected.get(array[0]);
            if (comments == null) {
                comments = new LinkedHashMap<Map<Integer, String>, String>();
                expected.put((Integer) array[0], comments);
            }
            @SuppressWarnings("unchecked")
            Pair<Pair<Integer, String>, String> pair = (Pair<Pair<Integer, String>, String>) array[1];
            Pair<Integer, String> first = pair.getFirst();
            Map<Integer, String> posts = Collections.singletonMap(first.getFirst(), first.getSecond());
            comments.put(posts, pair.getSecond());
        }
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void array_access() {
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
    public void transform_results() {
        Map<Integer, Post> results = POST_W_COMMENTS.transform(
                groupBy(postId).as(Projections.constructor(Post.class, postId, postName, set(qComment))));

        Post post = results.get(1);
        assertNotNull(post);
        assertEquals(toInt(1), post.getId());
        assertEquals("post 1", post.getName());
        assertEquals(toSet(comment(1), comment(2), comment(3)), post.getComments());
    }

    @Test
    public void transform_via_groupByProjection() {
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
    public void transform_as_bean() {
        Map<Integer, Post> results = POST_W_COMMENTS.transform(
                groupBy(postId).as(Projections.bean(Post.class, postId, postName, set(qComment).as("comments"))));

        Post post = results.get(1);
        assertNotNull(post);
        assertEquals(toInt(1), post.getId());
        assertEquals("post 1", post.getName());
        assertEquals(toSet(comment(1), comment(2), comment(3)), post.getComments());
    }


    @Test
    public void oneToOneToMany_projection() {
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
    public void oneToOneToMany_projection_as_bean() {
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
    public void oneToOneToMany_projection_as_bean_and_constructor() {
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

    @Test
    public void signature() {
        StringPath str = Expressions.stringPath("str");
        NumberPath<BigDecimal> bigd = Expressions.numberPath(BigDecimal.class, "bigd");

        ResultTransformer<Map<String, SortedMap<BigDecimal, SortedMap<BigDecimal, Map<String, String>>>>> resultTransformer = GroupBy.groupBy(str).as(
                GroupBy.sortedMap(bigd,
                        GroupBy.sortedMap(bigd,
                                GroupBy.map(str, str),
                                Comparator.nullsLast(Comparator.naturalOrder())),
                           Comparator.nullsFirst(Comparator.naturalOrder())));
        assertNotNull(resultTransformer);
    }

    @Test
    public void average_with_default_math_context() {
        Map<Integer, Double> results = POSTS_W_COMMENTS_SCORE
                .transform(groupBy(postId).as(avg(score)));
        assertEquals(1.5, results.get(null), 0.0);
        assertEquals(((1.5 + 2.0 + 0.5) / 3), results.get(1), 0.0);
        assertEquals(((1.0 + 2.0) / 2), results.get(2), 0.0);
    }

    @Test
    public void average_with_user_provided_math_context() {
        MathContext oneDigitMathContext = new MathContext(2, RoundingMode.HALF_EVEN);
        Map<Integer, Double> results = POSTS_W_COMMENTS_SCORE
                .transform(groupBy(postId).as(avg(score, oneDigitMathContext)));
        assertEquals(1.5, results.get(null), 0.0);
        assertEquals(1.3, results.get(1), 0.0);
        assertEquals(1.5, results.get(2), 0.0);
    }
}
