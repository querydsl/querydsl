/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.alias;

import org.junit.Test;

public class ComparablePropertyTest {
    
    public static class Entity{
        
        private ComparableType property;

        public ComparableType getProperty() {
            return property;
        }

        public void setProperty(ComparableType property) {
            this.property = property;
        }
        
    }
    
    public static class ComparableType implements Comparable<ComparableType>{

        @Override
        public int compareTo(ComparableType o) {
            return 0;
        }
        
        @Override
        public boolean equals(Object o){
            if (o == this){
                return true;
            }else if (o instanceof ComparableType){
                return true;
            }else{
                return false;
            }
        }
        
        @Override
        public int hashCode(){
            return super.hashCode();
        }
        
    }
    
    @Test
    public void test(){
        Entity entity = Alias.alias(Entity.class);
        Alias.$(entity.getProperty());
    }

}
