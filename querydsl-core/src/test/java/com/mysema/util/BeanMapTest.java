package com.mysema.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class BeanMapTest {

    private BeanMap beanMap;

    @Before
    public void setUp() {
        beanMap = new BeanMap(new Entity());
    }

    @Test
    public void Size() {
        assertEquals(4, beanMap.size());
    }

    @Test
    public void Clear() {
        beanMap.clear();
        assertEquals(4, beanMap.size());
    }

    @Test
    public void Primitives() {
        beanMap.put("id", 5);
        assertEquals(5, ((Entity)beanMap.getBean()).getId());
    }

    @Test
    public void BeanMap() {
        assertEquals(0, new BeanMap().size());
    }

    @Test
    public void BeanMapObject() {
        assertEquals(4, new BeanMap(new Entity()).size());
    }

    @Test
    public void ToString() {
        assertEquals("BeanMap<null>", new BeanMap().toString());
    }

    @Test
    public void Clone() throws CloneNotSupportedException {
        assertEquals(beanMap, beanMap.clone());
    }

    @Test
    public void PutAllWriteable() {

    }

    @Test
    public void ContainsKeyString() {
        assertTrue(beanMap.containsKey("id"));
    }

    @Test
    public void ContainsValueObject() {

    }

    @Test
    public void GetString() {
        beanMap.put("firstName", "John");
        assertEquals("John", beanMap.get("firstName"));
    }

    @Test
    public void KeySet() {
        assertEquals(Sets.newHashSet("id", "class", "firstName", "lastName"), beanMap.keySet());
    }

    @Test
    public void EntrySet() {
        beanMap.put("firstName", "John");
        assertFalse(beanMap.entrySet().isEmpty());
    }

    @Test
    public void Values() {
        beanMap.put("firstName", "John");
        assertEquals(Lists.newArrayList(0, null, Entity.class, "John"), beanMap.values());
    }

    @Test
    public void GetType() {

    }

    @Test
    public void GetBean() {
        assertEquals(Entity.class, beanMap.getBean().getClass());
    }

    @Test
    public void SetBean() {
        Entity entity = new Entity();
        beanMap.setBean(entity);
        assertTrue(entity == beanMap.getBean());
    }

}
