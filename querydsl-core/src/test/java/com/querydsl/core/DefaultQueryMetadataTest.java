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
package com.querydsl.core;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.querydsl.core.QueryFlag.Position;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.Param;
import com.querydsl.core.types.dsl.StringPath;

public class DefaultQueryMetadataTest {

    private final QueryMetadata metadata = new DefaultQueryMetadata();

    public DefaultQueryMetadataTest() {
        metadata.setValidate(true);
    }

    private final StringPath str = Expressions.stringPath("str");

    private final StringPath str2 = Expressions.stringPath("str2");

    @Test
    public void addWhere_with_null() {
        metadata.addWhere(null);
    }

    @Test
    public void addWhere_with_booleanBuilder() {
        metadata.addWhere(new BooleanBuilder());
    }

    @Test
    public void addHaving_with_null() {
        metadata.addHaving(null);
    }

    @Test
    public void addHaving_with_booleanBuilder() {
        metadata.addHaving(new BooleanBuilder());
    }


    @Test(expected = IllegalArgumentException.class)
    public void validation() {
        metadata.addWhere(str.isNull());
    }

    @Test
    public void validation_no_error_for_groupBy() {
        metadata.addGroupBy(str);
    }

    @Test
    public void validation_no_error_for_having() {
        metadata.addHaving(str.isNull());
    }

    @Test
    public void getGroupBy() {
        metadata.addJoin(JoinType.DEFAULT, str);
        metadata.addGroupBy(str);
        assertEquals(Arrays.asList(str), metadata.getGroupBy());
    }

    @Test
    public void getHaving() {
        metadata.addJoin(JoinType.DEFAULT, str);
        metadata.addHaving(str.isNotNull());
        assertEquals(str.isNotNull(), metadata.getHaving());
    }

    @Test
    public void getJoins() {
        metadata.addJoin(JoinType.DEFAULT, str);
        assertEquals(Arrays.asList(new JoinExpression(JoinType.DEFAULT, str)), metadata.getJoins());
    }

    @Test
    public void getJoins2() {
        metadata.addJoin(JoinType.DEFAULT, str);
        assertEquals(Arrays.asList(new JoinExpression(JoinType.DEFAULT, str)), metadata.getJoins());
    }

    @Test
    public void getModifiers() {
        QueryModifiers modifiers = new QueryModifiers(1L,2L);
        metadata.setModifiers(modifiers);
        assertEquals(modifiers, metadata.getModifiers());
    }

    @Test
    public void setLimit() {
        QueryModifiers modifiers = new QueryModifiers(1L,2L);
        metadata.setModifiers(modifiers);
        metadata.setLimit(3L);

        assertEquals(Long.valueOf(3L), metadata.getModifiers().getLimit());
        assertEquals(Long.valueOf(2L), metadata.getModifiers().getOffset());
    }

    @Test
    public void setOffset() {
        QueryModifiers modifiers = new QueryModifiers(1L,1L);
        metadata.setModifiers(modifiers);
        metadata.setOffset(2L);

        assertEquals(Long.valueOf(1L), metadata.getModifiers().getLimit());
        assertEquals(Long.valueOf(2L), metadata.getModifiers().getOffset());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void getOrderBy() {
        metadata.addJoin(JoinType.DEFAULT, str);
        metadata.addOrderBy(str.asc());
        metadata.addOrderBy(str.desc());
        assertEquals(Arrays.asList(str.asc(),str.desc()), metadata.getOrderBy());
    }

    @Test
    public void getProjection() {
        metadata.addJoin(JoinType.DEFAULT, str);
        metadata.setProjection(str.append("abc"));
        assertEquals(str.append("abc"), metadata.getProjection());
    }

    @Test
    public void getWhere() {
        metadata.addJoin(JoinType.DEFAULT, str);
        metadata.addWhere(str.eq("b"));
        metadata.addWhere(str.isNotEmpty());
        assertEquals(str.eq("b").and(str.isNotEmpty()), metadata.getWhere());
    }

    @Test
    public void isDistinct() {
        assertFalse(metadata.isDistinct());
        metadata.setDistinct(true);
        assertTrue(metadata.isDistinct());
    }

    @Test
    public void isUnique() {
        assertFalse(metadata.isUnique());
        metadata.setUnique(true);
        assertTrue(metadata.isUnique());
    }

    @Test
    public void joinShouldBeCommitted() {
        DefaultQueryMetadata md = new DefaultQueryMetadata();
        md.addJoin(JoinType.DEFAULT, str);
        DefaultQueryMetadata emptyMetadata = new DefaultQueryMetadata();
        assertFalse(md.equals(emptyMetadata));
    }

    @Test
    public void clone_() {
        metadata.addJoin(JoinType.DEFAULT, str);
        metadata.addGroupBy(str);
        metadata.addHaving(str.isNotNull());
        metadata.addJoin(JoinType.DEFAULT, str2);
        QueryModifiers modifiers = new QueryModifiers(1L,2L);
        metadata.setModifiers(modifiers);
        metadata.addOrderBy(str.asc());
        metadata.setProjection(str.append("abc"));
        metadata.addWhere(str.eq("b"));
        metadata.addWhere(str.isNotEmpty());

        QueryMetadata clone = metadata.clone();
        assertEquals(metadata.getGroupBy(), clone.getGroupBy());
        assertEquals(metadata.getHaving(), clone.getHaving());
        assertEquals(metadata.getJoins(), clone.getJoins());
        assertEquals(metadata.getModifiers(), clone.getModifiers());
        assertEquals(metadata.getOrderBy(), clone.getOrderBy());
        assertEquals(metadata.getProjection(), clone.getProjection());
        assertEquals(metadata.getWhere(), clone.getWhere());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void setParam() {
        metadata.setParam(new Param(String.class, "str"), ConstantImpl.create("X"));
        assertEquals(1, metadata.getParams().size());
        assertTrue(metadata.getParams().get(new Param(String.class, "str")).equals(ConstantImpl.create("X")));
    }

    @Test
    public void addFlag() {
        QueryFlag flag = new QueryFlag(Position.START, "X");
        metadata.addFlag(flag);
        assertTrue(metadata.hasFlag(flag));
    }

    @Test
    public void equals() {
        metadata.addJoin(JoinType.DEFAULT, str);
        metadata.addGroupBy(str);
        metadata.addHaving(str.isNotNull());
        metadata.addJoin(JoinType.DEFAULT, str2);
        QueryModifiers modifiers = new QueryModifiers(1L,2L);
        metadata.setModifiers(modifiers);
        metadata.addOrderBy(str.asc());
        metadata.setProjection(str.append("abc"));
        metadata.addWhere(str.eq("b"));
        metadata.addWhere(str.isNotEmpty());

        QueryMetadata metadata2 = new DefaultQueryMetadata();
        assertFalse(metadata.equals(metadata2));
        metadata2.addJoin(JoinType.DEFAULT, str);
        assertFalse(metadata.equals(metadata2));
        metadata2.addGroupBy(str);
        assertFalse(metadata.equals(metadata2));
        metadata2.addHaving(str.isNotNull());
        assertFalse(metadata.equals(metadata2));
        metadata2.addJoin(JoinType.DEFAULT, str2);
        assertFalse(metadata.equals(metadata2));
        metadata2.setModifiers(modifiers);
        assertFalse(metadata.equals(metadata2));
        metadata2.addOrderBy(str.asc());
        assertFalse(metadata.equals(metadata2));
        metadata2.setProjection(str.append("abc"));
        assertFalse(metadata.equals(metadata2));
        metadata2.addWhere(str.eq("b"));
        metadata2.addWhere(str.isNotEmpty());
        assertTrue(metadata.equals(metadata2));
    }

    @Test
    public void hashCode_() {
        metadata.addJoin(JoinType.DEFAULT, str);
        metadata.addGroupBy(str);
        metadata.addHaving(str.isNotNull());
        metadata.addJoin(JoinType.DEFAULT, str2);
        QueryModifiers modifiers = new QueryModifiers(1L,2L);
        metadata.setModifiers(modifiers);
        metadata.addOrderBy(str.asc());
        metadata.setProjection(str.append("abc"));
        metadata.addWhere(str.eq("b"));
        metadata.addWhere(str.isNotEmpty());
        metadata.hashCode();
    }

    @Test
    public void hashCode_empty_metadata() {
        metadata.hashCode();
    }
}
