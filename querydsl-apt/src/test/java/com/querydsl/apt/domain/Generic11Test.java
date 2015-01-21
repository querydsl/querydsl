package com.querydsl.apt.domain;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import org.junit.Test;

public class Generic11Test {
    
    // 1    
    public interface WhatEver { }
    
    @Entity
    public static class A<T extends WhatEver> { }
    
    @Entity
    public static class B extends A { } // note the missing type parameter
    
    
    // 2    
    @MappedSuperclass
    public static abstract class WhatEver2 { } 
    
    @Entity
    public static class A2<T extends WhatEver2> { }

    @Entity
    public static class B2 extends A2 { } // note the missing type parameter

    @Test
    public void test() {
        
    }

}
