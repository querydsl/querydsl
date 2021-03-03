package com.querydsl.core.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

public class BeanMapTest {

    private BeanMap beanMap;

    @Before
    public void setUp() {
        beanMap = new BeanMap(new Entity());
    }

    @Test
    public void size() {
        assertEquals(4, beanMap.size());
    }

    @Test
    public void clear() {
        beanMap.clear();
        assertEquals(4, beanMap.size());
    }

    @Test
    public void primitives() {
        beanMap.put("id", 5);
        assertEquals(5, ((Entity) beanMap.getBean()).getId());
    }

    @Test
    public void beanMap() {
        assertEquals(0, new BeanMap().size());
    }

    @Test
    public void beanMapObject() {
        assertEquals(4, new BeanMap(new Entity()).size());
    }

    @Test
    public void toString_() {
        assertEquals("BeanMap<null>", new BeanMap().toString());
    }

    @Test
    public void clone_() throws CloneNotSupportedException {
        assertEquals(beanMap, beanMap.clone());
    }

    @Test
    public void putAllWriteable() {

    }

    @Test
    public void containsKeyString() {
        assertTrue(beanMap.containsKey("id"));
    }

    @Test
    public void containsValueObject() {

    }

    @Test
    public void getString() {
        beanMap.put("firstName", "John");
        assertEquals("John", beanMap.get("firstName"));
    }

    @Test
    public void keySet() {
        assertEquals(new HashSet<>(Arrays.asList("id", "class", "firstName", "lastName")), beanMap.keySet());
    }

    @Test
    public void entrySet() {
        beanMap.put("firstName", "John");
        assertFalse(beanMap.entrySet().isEmpty());
    }

    @Test
    @Ignore
    public void values() {
        beanMap.put("firstName", "John");
        assertEquals(Arrays.asList(0, null, Entity.class, "John"), beanMap.values());
    }

    @Test
    public void getType() {

    }

    @Test
    public void getBean() {
        assertEquals(Entity.class, beanMap.getBean().getClass());
    }

    @Test
    public void setBean() {
        Entity entity = new Entity();
        beanMap.setBean(entity);
        assertTrue(entity == beanMap.getBean());
    }

}
