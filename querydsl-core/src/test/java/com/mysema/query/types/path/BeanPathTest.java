package com.mysema.query.types.path;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.annotation.Nullable;

import org.junit.Test;

import com.mysema.query.annotations.PropertyType;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.PathMetadataFactory;

public class BeanPathTest {
    
    public static class MyBeanPath extends BeanPath<BeanPathTest>{

        private static final long serialVersionUID = 6225684967115368814L;

        public MyBeanPath(PathMetadata<?> metadata) {
            super(BeanPathTest.class, metadata);
        }
        
        public MyBeanPath(PathMetadata<?> metadata, @Nullable PathInits inits) {
            super(BeanPathTest.class, metadata);
        }
        
    }
    
    private BeanPath<BeanPathTest> beanPath = new BeanPath<BeanPathTest>(BeanPathTest.class, "p");
    
    @Test
    public void As_Path(){        
        SimplePath<BeanPathTest> simplePath = new SimplePath<BeanPathTest>(BeanPathTest.class, "p");
        assertNotNull(beanPath.as(simplePath));
    }
    
    @Test
    public void As_Class(){       
        MyBeanPath otherPath = beanPath.as(MyBeanPath.class);
        assertEquals(beanPath, otherPath);
    }
    
    @Test
    public void As_Class_Cached(){       
        MyBeanPath otherPath = beanPath.as(MyBeanPath.class);
        assertEquals(beanPath, otherPath);
        assertTrue(otherPath == beanPath.as(MyBeanPath.class));
    }
    
    @Test
    public void As_Class_with_Inits(){       
        beanPath = new BeanPath<BeanPathTest>(BeanPathTest.class, PathMetadataFactory.forVariable("p"), PathInits.DEFAULT);
        MyBeanPath otherPath = beanPath.as(MyBeanPath.class);
        assertEquals(beanPath, otherPath);
    }
    
    @Test
    public void As_Class_with_Inits_Cached(){       
        beanPath = new BeanPath<BeanPathTest>(BeanPathTest.class, PathMetadataFactory.forVariable("p"), PathInits.DEFAULT);
        MyBeanPath otherPath = beanPath.as(MyBeanPath.class);
        assertEquals(beanPath, otherPath);
        assertTrue(otherPath == beanPath.as(MyBeanPath.class));
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
