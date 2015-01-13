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
package com.querydsl.core;

import java.util.Arrays;

import com.querydsl.core.QueryFlag.Position;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.expr.Param;
import com.querydsl.core.types.path.StringPath;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultQueryMetadataTest {

    private final QueryMetadata metadata = new DefaultQueryMetadata();

    private final StringPath str = new StringPath("str");
    
    private final StringPath str2 = new StringPath("str2");

    @Test
    public void AddWhere_With_Null() {
        metadata.addWhere((Predicate)null);
    }
    
    @Test
    public void AddWhere_With_BooleanBuilder() {
        metadata.addWhere(new BooleanBuilder());
    }
    
    @Test
    public void AddHaving_With_Null() {
        metadata.addHaving((Predicate)null);
    }
    
    @Test
    public void AddHaving_With_BooleanBuilder() {
        metadata.addHaving(new BooleanBuilder());
    }
    
    
    @Test(expected=IllegalArgumentException.class)
    public void Validation() {
        metadata.addWhere(str.isNull());
    }

    @Test
    public void Validation_No_Error_For_GroupBy() {
        metadata.addGroupBy(str);
    }

    @Test
    public void Validation_No_Error_For_Having() {
        metadata.addHaving(str.isNull());
    }
    
    @Test
    public void GetGroupBy() {
        metadata.addJoin(JoinType.DEFAULT, str);
        metadata.addGroupBy(str);
        assertEquals(Arrays.asList(str), metadata.getGroupBy());
    }

    @Test
    public void GetHaving() {
        metadata.addJoin(JoinType.DEFAULT, str);
        metadata.addHaving(str.isNotNull());
        assertEquals(str.isNotNull(), metadata.getHaving());
    }

    @Test
    public void GetJoins() {
        metadata.addJoin(JoinType.DEFAULT, str);
        assertEquals(Arrays.asList(new JoinExpression(JoinType.DEFAULT, str)), metadata.getJoins());
    }
    
    @Test
    public void GetJoins2() {
        metadata.addJoin(JoinType.DEFAULT, str);
        assertEquals(Arrays.asList(new JoinExpression(JoinType.DEFAULT, str)), metadata.getJoins());
    }

    @Test
    public void GetModifiers() {
        QueryModifiers modifiers = new QueryModifiers(1l,2l);
        metadata.setModifiers(modifiers);
        assertEquals(modifiers, metadata.getModifiers());
    }

    @Test
    public void setLimit() {
        QueryModifiers modifiers = new QueryModifiers(1l,2l);
        metadata.setModifiers(modifiers);
        metadata.setLimit(3l);

        assertEquals(Long.valueOf(3l), metadata.getModifiers().getLimit());
        assertEquals(Long.valueOf(2l), metadata.getModifiers().getOffset());
    }

    @Test
    public void setOffset() {
        QueryModifiers modifiers = new QueryModifiers(1l,1l);
        metadata.setModifiers(modifiers);
        metadata.setOffset(2l);

        assertEquals(Long.valueOf(1l), metadata.getModifiers().getLimit());
        assertEquals(Long.valueOf(2l), metadata.getModifiers().getOffset());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void GetOrderBy() {
        metadata.addJoin(JoinType.DEFAULT, str);
        metadata.addOrderBy(str.asc());
        metadata.addOrderBy(str.desc());
        assertEquals(Arrays.asList(str.asc(),str.desc()), metadata.getOrderBy());
    }

    @Test
    public void GetProjection() {
        metadata.addJoin(JoinType.DEFAULT, str);
        metadata.addProjection(str);
        metadata.addProjection(str.append("abc"));
        assertEquals(Arrays.asList(str, str.append("abc")), metadata.getProjection());
    }

    @Test
    public void GetWhere() {
        metadata.addJoin(JoinType.DEFAULT, str);
        metadata.addWhere(str.eq("b"));
        metadata.addWhere(str.isNotEmpty());
        assertEquals(str.eq("b").and(str.isNotEmpty()), metadata.getWhere());
    }

    @Test
    public void IsDistinct() {
        assertFalse(metadata.isDistinct());
        metadata.setDistinct(true);
        assertTrue(metadata.isDistinct());
    }

    @Test
    public void IsUnique() {
        assertFalse(metadata.isUnique());
        metadata.setUnique(true);
        assertTrue(metadata.isUnique());
    }

    @Test
    public void JoinShouldBeCommitted() {
        DefaultQueryMetadata md = new DefaultQueryMetadata();
        md.addJoin(JoinType.DEFAULT, str);
        DefaultQueryMetadata emptyMetadata = new DefaultQueryMetadata();
        assertFalse(md.equals(emptyMetadata));
    }

    @Test
    public void Clone() {
        metadata.addJoin(JoinType.DEFAULT, str);
        metadata.addGroupBy(str);
        metadata.addHaving(str.isNotNull());
        metadata.addJoin(JoinType.DEFAULT, str2);
        QueryModifiers modifiers = new QueryModifiers(1l,2l);
        metadata.setModifiers(modifiers);
        metadata.addOrderBy(str.asc());
        metadata.addProjection(str);
        metadata.addProjection(str.append("abc"));
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
    public void SetParam() {
        metadata.setParam(new Param(String.class, "str"), ConstantImpl.create("X"));
        assertEquals(1, metadata.getParams().size());
        assertTrue(metadata.getParams().get(new Param(String.class, "str")).equals(ConstantImpl.create("X")));
    }
    
    @Test
    public void AddFlag() {
        QueryFlag flag = new QueryFlag(Position.START, "X");
        metadata.addFlag(flag);
        assertTrue(metadata.hasFlag(flag));
    }
    
    @Test
    public void Equals() {
        metadata.addJoin(JoinType.DEFAULT, str);
        metadata.addGroupBy(str);
        metadata.addHaving(str.isNotNull());
        metadata.addJoin(JoinType.DEFAULT, str2);
        QueryModifiers modifiers = new QueryModifiers(1l,2l);
        metadata.setModifiers(modifiers);
        metadata.addOrderBy(str.asc());
        metadata.addProjection(str);
        metadata.addProjection(str.append("abc"));
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
        metadata2.addProjection(str);
        metadata2.addProjection(str.append("abc"));
        assertFalse(metadata.equals(metadata2));
        metadata2.addWhere(str.eq("b"));
        metadata2.addWhere(str.isNotEmpty());
        assertTrue(metadata.equals(metadata2));
    }
    
    @Test
    public void HashCode() {
        metadata.addJoin(JoinType.DEFAULT, str);
        metadata.addGroupBy(str);
        metadata.addHaving(str.isNotNull());
        metadata.addJoin(JoinType.DEFAULT, str2);
        QueryModifiers modifiers = new QueryModifiers(1l,2l);
        metadata.setModifiers(modifiers);
        metadata.addOrderBy(str.asc());
        metadata.addProjection(str);
        metadata.addProjection(str.append("abc"));
        metadata.addWhere(str.eq("b"));
        metadata.addWhere(str.isNotEmpty());
        metadata.hashCode();        
    }
    
    @Test
    public void HashCode_Empty_Metadata() {
        metadata.hashCode();
    }
}
