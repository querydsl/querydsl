/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa;

import org.junit.Test;

public class EJBQLTest extends AbstractQueryTest{

    // Any function or operator defined by EJB-QL 3.0: substring(), trim(),
    // lower(), upper(), length(), locate(), abs(), sqrt(), bit_length(),
    // mod()
    // substring(),
    // trim(),
    // lower(),
    // upper(),
    // length(),
    // locate(),
    // abs(),
    // sqrt(),
    // bit_length(),
    // mod()
    
    @Test
    public void Trim(){
        assertToString("trim(cat.name)", cat.name.trim());   
    }
    
    @Test
    public void Lower(){
        assertToString("lower(cat.name)", cat.name.lower());    
    }
    
    @Test
    public void Upper(){
        assertToString("upper(cat.name)", cat.name.upper());   
    }

}
