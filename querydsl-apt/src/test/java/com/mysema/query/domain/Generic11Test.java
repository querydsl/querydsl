package com.mysema.query.domain;

import javax.persistence.Entity;

import org.junit.Test;

public class Generic11Test {
    
    public interface WhatEver {
        
    }
    
    @Entity
    public static class A<T extends WhatEver> {

    }

    @Entity
    public static class B extends A { // note the missing type parameter

    }
    
    @Test
    public void test() {
        
    }

}
