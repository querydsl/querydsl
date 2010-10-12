package com.mysema.query.types.path;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.mysema.query.annotations.PropertyType;

public class BeanPathTest {
    
    @Test
    public void CreateEnum(){
        BeanPath<BeanPathTest> beanPath = new BeanPath<BeanPathTest>(BeanPathTest.class, "p");
        assertNotNull(beanPath.createEnum("property", PropertyType.class));
    }
    
    @Test
    public void As(){
        BeanPath<BeanPathTest> beanPath = new BeanPath<BeanPathTest>(BeanPathTest.class, "p");
        SimplePath<BeanPathTest> simplePath = new SimplePath<BeanPathTest>(BeanPathTest.class, "p");
        assertNotNull(beanPath.as(simplePath));
    }

}
