package com.querydsl.collections;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.map;
import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.querydsl.core.annotations.QueryEntity;


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
        List<Table> data = Lists.newArrayList();
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
        assertEquals(ImmutableSet.of("abc", "pqr"),  grouped.get("1").keySet());

    }

}
