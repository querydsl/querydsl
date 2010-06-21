package com.mysema.query.domain;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QueryMethod;

public class Inheritance10Test {

    @QueryEntity
    public static class Supertype<K,V>{

        private Map<K,V> fields;

        public Map<K, V> getFields() {
            return fields;
        }

        public void setFields(Map<K, V> fields) {
            this.fields = fields;
        }

        @QueryMethod("{0}.get({1})")
        public V get(K key){
            return null;
        }

    }

    @QueryEntity
    public static class Entity1 extends Supertype<Long,String>{

    }

    @QueryEntity
    public static class Entity2 extends Supertype<Long,Entity2>{

    }

    @Test
    public void Supertype_fields(){
        assertEquals(Object.class, QInheritance10Test_Supertype.supertype.fields.getKeyType());
        assertEquals(Object.class, QInheritance10Test_Supertype.supertype.fields.getValueType());

        assertEquals(Long.class, QInheritance10Test_Entity1.entity1.fields.getKeyType());
        assertEquals(String.class, QInheritance10Test_Entity1.entity1.fields.getValueType());

        assertEquals(Long.class, QInheritance10Test_Entity2.entity2.fields.getKeyType());
        assertEquals(Entity2.class, QInheritance10Test_Entity2.entity2.fields.getValueType());
    }

}
