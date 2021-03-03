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

package com.querydsl.collections;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.core.group.guava.GuavaGroupBy;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static com.querydsl.core.group.GroupBy.list;
import static com.querydsl.core.group.guava.GuavaGroupBy.groupBy;
import static com.querydsl.core.group.guava.GuavaGroupBy.map;
import static org.junit.Assert.assertEquals;


public class GroupBy4Test {

    @QueryEntity
    public static class Table {
        String col1, col2, col3;

        public Table(String c1, String c2, String c3) {
            col1 = c1;
            col2 = c2;
            col3 = c3;
        }
    }

    @Test
    public void test() {
        List<Table> data = new ArrayList<>();
        data.add(new Table("1", "abc", "111"));
        data.add(new Table("1", "pqr", "222"));
        data.add(new Table("2", "abc", "333"));
        data.add(new Table("2", "pqr", "444"));
        data.add(new Table("3", "abc", "555"));
        data.add(new Table("3", "pqr", "666"));

        QGroupBy4Test_Table table = QGroupBy4Test_Table.table;
        Map<String, Map<String, String>> grouped = CollQueryFactory
                .from(table, data)
                .transform(groupBy(table.col1).as(map(table.col2, table.col3)));

        assertEquals(3, grouped.size());
        assertEquals(2, grouped.get("1").size());
        assertEquals(new HashSet<>(Arrays.asList("abc", "pqr")),  grouped.get("1").keySet());

    }

    @Test
    public void test2() {
        List<Table> data = Lists.newArrayList();
        data.add(new Table("1", "abc", "111"));
        data.add(new Table("1", "pqr", "222"));
        data.add(new Table("2", "abc", "333"));
        data.add(new Table("2", "pqr", "444"));
        data.add(new Table("3", "abc", "555"));
        data.add(new Table("3", "pqr", "666"));

        QGroupBy4Test_Table table = QGroupBy4Test_Table.table;
        Multimap<String, String> transform = CollQueryFactory
                .from(table, data)
                .transform(groupBy(table.col1).asMultimap(table.col2));

        Multimap<String, String> expected = HashMultimap.create(
                ImmutableMultimap.<String, String> builder()
                        .putAll("1", "abc", "pqr")
                        .putAll("2", "abc", "pqr")
                        .putAll("3", "abc", "pqr")
                        .build());

        assertEquals(expected, transform);
    }

    @Test
    public void test3() {
        List<Table> data = Lists.newArrayList();
        data.add(new Table("1", "abc", "111"));
        data.add(new Table("1", "pqr", "222"));
        data.add(new Table("2", "abc", "333"));
        data.add(new Table("2", "pqr", "444"));
        data.add(new Table("3", "abc", "555"));
        data.add(new Table("3", "pqr", "666"));

        QGroupBy4Test_Table table = QGroupBy4Test_Table.table;
        Multimap<String, Map<String, String>> transform = CollQueryFactory
                .from(table, data)
                .transform(groupBy(table.col1).asMultimap(
                        map(table.col2, table.col3)
                ));

        HashMultimap<String, Map<String, String>> expected = HashMultimap.create(ImmutableMultimap.<String, Map<String, String>>builder()
                .putAll("1", ImmutableMap.of("abc", "111"), ImmutableMap.of("pqr", "222"))
                .putAll("2", ImmutableMap.of("abc", "333"), ImmutableMap.of("pqr", "444"))
                .putAll("3", ImmutableMap.of("abc", "555"), ImmutableMap.of("pqr", "666"))
                .build()
        );

        assertEquals(expected, transform);
    }

    @Test
    public void test4() {
        List<Table> data = Lists.newArrayList();
        data.add(new Table("1", "abc", "111"));
        data.add(new Table("1", "pqr", "222"));
        data.add(new Table("2", "abc", "333"));
        data.add(new Table("2", "pqr", "444"));
        data.add(new Table("3", "abc", "555"));
        data.add(new Table("3", "pqr", "666"));


        QGroupBy4Test_Table table = QGroupBy4Test_Table.table;
        com.google.common.collect.Table<String, String, String> transform = CollQueryFactory
                .from(table, data)
                .transform(groupBy(table.col1).asTable(table.col2, table.col3));

        ImmutableTable<String, String, String> expected = ImmutableTable.<String, String, String> builder()
                .put("1", "abc", "111")
                .put("1", "pqr", "222")
                .put("2", "abc", "333")
                .put("2", "pqr", "444")
                .put("3", "abc", "555")
                .put("3", "pqr", "666")
                .build();

        assertEquals(expected, transform);
    }

    @Test
    public void test5() {
        List<Table> data = Lists.newArrayList();
        data.add(new Table("1", "abc", "111"));
        data.add(new Table("1", "pqr", "222"));
        data.add(new Table("2", "abc", "333"));
        data.add(new Table("2", "pqr", "444"));
        data.add(new Table("3", "abc", "555"));
        data.add(new Table("3", "pqr", "666"));
        data.add(new Table("3", "pqr", "777"));


        QGroupBy4Test_Table table = QGroupBy4Test_Table.table;
        com.google.common.collect.Table<String, String, List<String>> transform = CollQueryFactory
                .from(table, data)
                .transform(groupBy(table.col1).asTable(table.col2, list(table.col3)));

        ImmutableTable<String, String, List<String>> expected = ImmutableTable.<String, String, List<String>> builder()
                .put("1", "abc", ImmutableList.of("111"))
                .put("1", "pqr", ImmutableList.of("222"))
                .put("2", "abc", ImmutableList.of("333"))
                .put("2", "pqr", ImmutableList.of("444"))
                .put("3", "abc", ImmutableList.of("555"))
                .put("3", "pqr", ImmutableList.of("666", "777"))
                .build();

        assertEquals(expected, transform);
    }

    @Test
    public void test6() {
        List<Table> data = Lists.newArrayList();
        data.add(new Table("1", "abc", "111"));
        data.add(new Table("1", "pqr", "222"));
        data.add(new Table("2", "abc", "333"));
        data.add(new Table("2", "pqr", "444"));
        data.add(new Table("3", "abc", "555"));
        data.add(new Table("3", "pqr", "666"));
        data.add(new Table("3", "pqr", "777"));


        QGroupBy4Test_Table table = QGroupBy4Test_Table.table;
        Map<String, Multimap<String, String>> transform = CollQueryFactory
                .from(table, data)
                .transform(groupBy(table.col1).as(GuavaGroupBy.multimap(table.col2, table.col3)));

        ImmutableMap<String, Multimap<String, String>> expected = ImmutableMap.<String, Multimap<String, String>> builder()
                .put("1", HashMultimap.create(ImmutableMultimap.<String, String> builder().putAll("abc", "111").putAll("pqr", "222").build()))
                .put("2", HashMultimap.create(ImmutableMultimap.<String, String> builder().putAll("abc", "333").putAll("pqr", "444").build()))
                .put("3", HashMultimap.create(ImmutableMultimap.<String, String> builder().putAll("abc", "555").putAll("pqr", "666", "777").build()))
                .build();

        assertEquals(expected, transform);
    }

    @Test
    public void test7() {
        List<Table> data = Lists.newArrayList();
        data.add(new Table("1", "abc", "111"));
        data.add(new Table("1", "pqr", "222"));
        data.add(new Table("2", "abc", "333"));
        data.add(new Table("2", "pqr", "444"));
        data.add(new Table("3", "abc", "555"));
        data.add(new Table("3", "pqr", "666"));
        data.add(new Table("3", "pqr", "777"));

        QGroupBy4Test_Table table = QGroupBy4Test_Table.table;
        Map<String, com.google.common.collect.Table<String, String, Map<String, List<String>>>> transform = CollQueryFactory
                .from(table, data)
                .transform(groupBy(table.col1).as(GuavaGroupBy.table(
                        table.col1, table.col2, map(table.col2, list(table.col3)))));

        ImmutableMap<String, com.google.common.collect.Table<String, String, Map<String, List<String>>>> expected =
                ImmutableMap.<String, com.google.common.collect.Table<String, String, Map<String, List<String>>>>builder()
                .put("1", ImmutableTable.<String, String, Map<String, List<String>>>builder()
                        .put("1", "abc", ImmutableMap.<String, List<String>> of("abc", ImmutableList.of("111")))
                        .put("1", "pqr", ImmutableMap.<String, List<String>> of("pqr", ImmutableList.of("222")))
                        .build())
                .put("2", ImmutableTable.<String, String, Map<String, List<String>>>builder()
                        .put("2", "abc", ImmutableMap.<String, List<String>> of("abc", ImmutableList.of("333")))
                        .put("2", "pqr", ImmutableMap.<String, List<String>> of("pqr", ImmutableList.of("444")))
                        .build())
                .put("3", ImmutableTable.<String, String, Map<String, List<String>>>builder()
                        .put("3", "abc", ImmutableMap.<String, List<String>> of("abc", ImmutableList.of("555")))
                        .put("3", "pqr", ImmutableMap.<String, List<String>> of("pqr", ImmutableList.of("666", "777")))
                        .build())
                .build();

        assertEquals(expected, transform);
    }

    @Test
    public void test8() {
        List<Table> data = Lists.newArrayList();
        data.add(new Table("1", "abc", "111"));
        data.add(new Table("1", "pqr", "222"));
        data.add(new Table("2", "abc", "333"));
        data.add(new Table("2", "pqr", "444"));
        data.add(new Table("3", "abc", "555"));
        data.add(new Table("3", "pqr", "666"));
        data.add(new Table("3", "pqr", "777"));

        QGroupBy4Test_Table table = QGroupBy4Test_Table.table;
        com.google.common.collect.Table<String, Map<String, String>, List<String>> transform = CollQueryFactory
                .from(table, data)
                .transform(groupBy(table.col1).asTable(map(table.col1, table.col2), GuavaGroupBy.list(table.col3)));

        ImmutableTable<String, Map<String, String>, List<String>> expected = ImmutableTable.<String, Map<String, String>, List<String>> builder()
                .put("1", ImmutableMap.of("1", "abc"), ImmutableList.of("111"))
                .put("1", ImmutableMap.of("1", "pqr"), ImmutableList.of("222"))
                .put("2", ImmutableMap.of("2", "abc"), ImmutableList.of("333"))
                .put("2", ImmutableMap.of("2", "pqr"), ImmutableList.of("444"))
                .put("3", ImmutableMap.of("3", "abc"), ImmutableList.of("555"))
                .put("3", ImmutableMap.of("3", "pqr"), ImmutableList.of("666", "777"))
                .build();

        assertEquals(expected, transform);
    }

}
