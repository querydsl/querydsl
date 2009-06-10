package com.mysema.query.hql;

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
        toString("trim(cat.name)", cat.name.trim());
        toString("lower(cat.name)", cat.name.lower());
        toString("upper(cat.name)", cat.name.upper());
        // cat.name.length();
    }

}
