/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import com.mysema.query.types.path.PString;

public class DefaultQueryMetadataTest {
    
    private QueryMetadata md = new DefaultQueryMetadata();

    private PString str = new PString("str");
    
    @Test
    public void testGetGroupBy() {
        md.addGroupBy(str);
        assertEquals(Arrays.asList(str), md.getGroupBy());
    }

    @Test
    public void testGetHaving() {
        md.addHaving(str.isNotNull());
        assertEquals(str.isNotNull(), md.getHaving());
    }

    @Test
    public void testGetJoins() {
        md.addJoin(JoinType.DEFAULT, str);
        assertEquals(Arrays.asList(new JoinExpression(JoinType.DEFAULT, str)),md.getJoins());
    }
    
    @Test
    public void testGetModifiers() {
        QueryModifiers modifiers = new QueryModifiers(1l,2l);
        md.setModifiers(modifiers);
        assertEquals(modifiers, md.getModifiers());
    }
    
    @Test
    public void setLimit(){
        QueryModifiers modifiers = new QueryModifiers(1l,2l);
        md.setModifiers(modifiers);
        md.setLimit(3l);
        
        assertEquals(Long.valueOf(3l), md.getModifiers().getLimit());
        assertEquals(Long.valueOf(2l), md.getModifiers().getOffset());
    }
    
    @Test
    public void setOffset(){
        QueryModifiers modifiers = new QueryModifiers(1l,1l);
        md.setModifiers(modifiers);
        md.setOffset(2l);
        
        assertEquals(Long.valueOf(1l), md.getModifiers().getLimit());
        assertEquals(Long.valueOf(2l), md.getModifiers().getOffset());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetOrderBy() {
        md.addOrderBy(str.asc());
        md.addOrderBy(str.desc());
        assertEquals(Arrays.asList(str.asc(),str.desc()), md.getOrderBy());
    }

    @Test
    public void testGetProjection() {
        md.addProjection(str, str.append("abc"));
        assertEquals(Arrays.asList(str, str.append("abc")), md.getProjection());
    }

    @Test
    public void testGetWhere() {
        md.addWhere(str.eq("b"), str.isNotEmpty());
        assertEquals(str.eq("b").and(str.isNotEmpty()), md.getWhere()); 
    }

    @Test
    public void testIsDistinct() {
        assertFalse(md.isDistinct());
        md.setDistinct(true);
        assertTrue(md.isDistinct());
    }

    @Test
    public void testIsUnique() {
        assertFalse(md.isUnique());
        md.setUnique(true);
        assertTrue(md.isUnique());
    }

    @Test
    public void testClone(){
        md.addGroupBy(str);
        md.addHaving(str.isNotNull());
        md.addJoin(JoinType.DEFAULT, str);
        QueryModifiers modifiers = new QueryModifiers(1l,2l);
        md.setModifiers(modifiers);
        md.addOrderBy(str.asc());
        md.addProjection(str, str.append("abc"));
        md.addWhere(str.eq("b"), str.isNotEmpty());
        
        QueryMetadata clone = md.clone();
        assertEquals(md.getGroupBy(), clone.getGroupBy());
        assertEquals(md.getHaving(), clone.getHaving());
        assertEquals(md.getJoins(), clone.getJoins());
        assertEquals(md.getModifiers(), clone.getModifiers());
        assertEquals(md.getOrderBy(), clone.getOrderBy());
        assertEquals(md.getProjection(), clone.getProjection());
        assertEquals(md.getWhere(), clone.getWhere());
        
    }
}
