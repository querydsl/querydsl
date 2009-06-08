/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;


/**
 * StandardTest defines the test cases that should be implemented by all modules
 * 
 * @author tiwe
 *
 */
public interface StandardTest {
    
    void booleanFilters();
    
    void collectionFilters();
    
    void collectionProjections();
    
    void dateProjections();
    
    void dateTimeProjections();
    
    void listFilters();
    
    void listProjections();
    
    void mapFilters();

    void mapProjections();
    
    void numericCasts();

    void numericFilters();
    
    void numericMatchingFilters();
    
    void numericProjections();

    void stringFilters();

    void stringMatchingFilters();

    void stringProjections();
    
    void timeProjections();
    
}