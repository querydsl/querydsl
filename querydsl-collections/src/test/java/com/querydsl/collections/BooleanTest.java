package com.querydsl.collections;

import java.util.Collections;

import org.junit.Test;

import com.querydsl.core.alias.Alias;
import static com.querydsl.core.alias.Alias.*;

public class BooleanTest {
    
    public static class Entity {
        
        private boolean boolean1 = true;
        
        private Boolean boolean2 = Boolean.TRUE;

        public boolean isBoolean1() {
            return boolean1;
        }

        public Boolean getBoolean2() {
            return boolean2;
        }
        
    }
    
    @Test
    public void Primitive_Boolean() {
        Entity entity = Alias.alias(Entity.class);
        CollQueryFactory.from(entity, Collections.singleton(new Entity()))
            .where($(entity.isBoolean1()).eq(Boolean.TRUE))
            .count();
    }
    
    @Test
    public void Object_Boolean() {
        Entity entity = Alias.alias(Entity.class);
        CollQueryFactory.from(entity, Collections.singleton(new Entity()))
            .where($(entity.getBoolean2()).eq(Boolean.TRUE))
            .count();
        
    }

}
