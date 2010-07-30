/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import org.junit.Test;

import com.mysema.query.QueryFlag.Position;
import com.mysema.query.types.path.PString;

public class DefaultQueryMetadataTest {

    private QueryMetadata metadata = new DefaultQueryMetadata();

    private PString str = new PString("str");

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException{
        PString expr = new PString("str");
        metadata.addFlag(new QueryFlag(Position.AFTER_FILTERS, ""));
        metadata.addGroupBy(expr);
        metadata.addHaving(expr.isEmpty());
        metadata.addJoin(JoinType.DEFAULT, expr);
        metadata.addJoinCondition(expr.isEmpty());
        metadata.addOrderBy(expr.asc());
        metadata.addProjection(expr);
        metadata.addWhere(expr.isEmpty());
        
        // serialize metadata
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        out.writeObject(metadata);
        out.close();
        
        // deserialize metadata
        ByteArrayInputStream bain = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bain);
        QueryMetadata  metadata2 = (QueryMetadata) in.readObject();
        in.close();
        
        assertEquals(metadata.getFlags(),     metadata2.getFlags());
        assertEquals(metadata.getGroupBy(),   metadata2.getGroupBy());
        assertEquals(metadata.getHaving(),    metadata2.getHaving());
        assertEquals(metadata.getJoins(),     metadata2.getJoins());
        assertEquals(metadata.getModifiers(), metadata2.getModifiers());
        assertEquals(metadata.getOrderBy(),   metadata2.getOrderBy());
        assertEquals(metadata.getParams(),    metadata2.getParams());
        assertEquals(metadata.getProjection(),metadata2.getProjection());
        assertEquals(metadata.getWhere(),     metadata2.getWhere());
    }
    
    @Test
    public void testGetGroupBy() {
        metadata.addGroupBy(str);
        assertEquals(Arrays.asList(str), metadata.getGroupBy());
    }

    @Test
    public void testGetHaving() {
        metadata.addHaving(str.isNotNull());
        assertEquals(str.isNotNull(), metadata.getHaving());
    }

    @Test
    public void testGetJoins() {
        metadata.addJoin(JoinType.DEFAULT, str);
        assertEquals(Arrays.asList(new JoinExpression(JoinType.DEFAULT, str)),metadata.getJoins());
    }

    @Test
    public void testGetModifiers() {
        QueryModifiers modifiers = new QueryModifiers(1l,2l);
        metadata.setModifiers(modifiers);
        assertEquals(modifiers, metadata.getModifiers());
    }

    @Test
    public void setLimit(){
        QueryModifiers modifiers = new QueryModifiers(1l,2l);
        metadata.setModifiers(modifiers);
        metadata.setLimit(3l);

        assertEquals(Long.valueOf(3l), metadata.getModifiers().getLimit());
        assertEquals(Long.valueOf(2l), metadata.getModifiers().getOffset());
    }

    @Test
    public void setOffset(){
        QueryModifiers modifiers = new QueryModifiers(1l,1l);
        metadata.setModifiers(modifiers);
        metadata.setOffset(2l);

        assertEquals(Long.valueOf(1l), metadata.getModifiers().getLimit());
        assertEquals(Long.valueOf(2l), metadata.getModifiers().getOffset());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetOrderBy() {
        metadata.addOrderBy(str.asc());
        metadata.addOrderBy(str.desc());
        assertEquals(Arrays.asList(str.asc(),str.desc()), metadata.getOrderBy());
    }

    @Test
    public void testGetProjection() {
        metadata.addProjection(str, str.append("abc"));
        assertEquals(Arrays.asList(str, str.append("abc")), metadata.getProjection());
    }

    @Test
    public void testGetWhere() {
        metadata.addWhere(str.eq("b"), str.isNotEmpty());
        assertEquals(str.eq("b").and(str.isNotEmpty()), metadata.getWhere());
    }

    @Test
    public void testIsDistinct() {
        assertFalse(metadata.isDistinct());
        metadata.setDistinct(true);
        assertTrue(metadata.isDistinct());
    }

    @Test
    public void testIsUnique() {
        assertFalse(metadata.isUnique());
        metadata.setUnique(true);
        assertTrue(metadata.isUnique());
    }

    @Test
    public void testClone(){
        metadata.addGroupBy(str);
        metadata.addHaving(str.isNotNull());
        metadata.addJoin(JoinType.DEFAULT, str);
        QueryModifiers modifiers = new QueryModifiers(1l,2l);
        metadata.setModifiers(modifiers);
        metadata.addOrderBy(str.asc());
        metadata.addProjection(str, str.append("abc"));
        metadata.addWhere(str.eq("b"), str.isNotEmpty());

        QueryMetadata clone = metadata.clone();
        assertEquals(metadata.getGroupBy(), clone.getGroupBy());
        assertEquals(metadata.getHaving(), clone.getHaving());
        assertEquals(metadata.getJoins(), clone.getJoins());
        assertEquals(metadata.getModifiers(), clone.getModifiers());
        assertEquals(metadata.getOrderBy(), clone.getOrderBy());
        assertEquals(metadata.getProjection(), clone.getProjection());
        assertEquals(metadata.getWhere(), clone.getWhere());

    }
}
