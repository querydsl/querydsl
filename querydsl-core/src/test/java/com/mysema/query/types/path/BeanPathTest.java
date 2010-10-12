package com.mysema.query.types.path;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.mysema.query.annotations.PropertyType;

public class BeanPathTest {
    
    private BeanPath<BeanPathTest> beanPath = new BeanPath<BeanPathTest>(BeanPathTest.class, "p");
    
    @Test
    public void As(){        
        SimplePath<BeanPathTest> simplePath = new SimplePath<BeanPathTest>(BeanPathTest.class, "p");
        assertNotNull(beanPath.as(simplePath));
    }
    
    @Test
    public void CreateEnum(){
        assertNotNull(beanPath.createEnum("property", PropertyType.class));
    }
    
    @Test
    public void InstanceOf(){
        assertNotNull(beanPath.instanceOf(BeanPathTest.class));
    }

}
