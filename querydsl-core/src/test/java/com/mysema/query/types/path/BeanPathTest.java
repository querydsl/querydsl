package com.mysema.query.types.path;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.mysema.query.annotations.PropertyType;
import com.mysema.query.types.PathMetadata;

public class BeanPathTest {
    
    public static class MyBeanPath extends BeanPath<Object>{

        public MyBeanPath(PathMetadata<?> metadata) {
            super(Object.class, metadata);
        }
        
    }
    
    private BeanPath<BeanPathTest> beanPath = new BeanPath<BeanPathTest>(BeanPathTest.class, "p");
    
    @Test
    public void As_Path(){        
        SimplePath<BeanPathTest> simplePath = new SimplePath<BeanPathTest>(BeanPathTest.class, "p");
        assertNotNull(beanPath.as(simplePath));
    }
    
//    @Test
//    public void As_Class(){       
//        MyBeanPath otherPath = beanPath.as(MyBeanPath.class);
//        assertEquals(beanPath, otherPath);
//    }
    
    @Test
    public void CreateEnum(){
        assertNotNull(beanPath.createEnum("property", PropertyType.class));
    }
    
    @Test
    public void InstanceOf(){
        assertNotNull(beanPath.instanceOf(BeanPathTest.class));
    }

}
