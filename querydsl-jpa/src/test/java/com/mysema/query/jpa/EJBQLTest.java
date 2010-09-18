/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa;

import org.junit.Test;

public class EJBQLTest extends AbstractQueryTest{

    @Test
    public void testEJBQL3Functions() {
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

        assertToString("trim(cat.name)", cat.name.trim());
        assertToString("lower(cat.name)", cat.name.lower());
        assertToString("upper(cat.name)", cat.name.upper());
        // cat.name.length();
    }

}
