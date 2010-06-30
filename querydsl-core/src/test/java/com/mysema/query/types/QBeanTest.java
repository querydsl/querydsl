package com.mysema.query.types;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mysema.query.types.expr.QBean;
import com.mysema.query.types.path.PBoolean;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PathBuilder;
import com.mysema.query.types.path.PathBuilderFactory;


public class QBeanTest {
    
    public static class Entity {
        
        private String name;
        
        private int age;
        
        private boolean married;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public boolean isMarried() {
            return married;
        }

        public void setMarried(boolean married) {
            this.married = married;
        }
                
    }
    
    @Test
    public void test(){
        PathBuilder<Entity> entity = new PathBuilderFactory().create(Entity.class);
        PString name = entity.getString("name");
        PNumber<Integer> age = entity.getNumber("age", Integer.class);
        PBoolean married = entity.getBoolean("married");
        
        QBean<Entity> beanProjection = new QBean<Entity>(Entity.class, name, age, married);
        Entity bean = beanProjection.newInstance("Fritz", 30, true);
        assertEquals("Fritz", bean.getName());
        assertEquals(30, bean.getAge());
        assertEquals(true, bean.isMarried());
        
    }

}
