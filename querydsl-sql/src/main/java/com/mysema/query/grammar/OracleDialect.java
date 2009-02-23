/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

import java.math.BigInteger;

/**
 * OracleDialect provides
 *
 * @author tiwe
 * @version $Id$
 */
public class OracleDialect extends SqlOps {
    {
        // type mappings
        addClass2TypeMappings("number(3,0)", Byte.class);
        addClass2TypeMappings("number(1,0)", Boolean.class);
        addClass2TypeMappings("number(19,0)", BigInteger.class, Long.class);
        addClass2TypeMappings("number(5,0)", Short.class);
        addClass2TypeMappings("number(10,0)", Integer.class);
        addClass2TypeMappings("double precision", Double.class);
        addClass2TypeMappings("varchar(4000 char)", String.class);
        
        // operator mappings
        add(Ops.OpMath.CEIL, "ceil(%s)");  
        add(Ops.OpMath.RANDOM, "dbms_random.value");
        add(Ops.OpMath.LOG, "ln(%s)");  
        add(Ops.OpMath.LOG10, "log(10,%s)");
        
        add(Ops.CONCAT, "%s || %s");
        add(Ops.OpString.SPACE, "lpad('',%s,' ')");
                  
        add(Ops.OpDateTime.YEAR, "extract(year from %s)");
        add(Ops.OpDateTime.MONTH, "extract(month from %s)");
        add(Ops.OpDateTime.WEEK, "to_number(to_char(%s,'WW'))");
        add(Ops.OpDateTime.DAY, "extract(day from %s)");
        
        add(Ops.OpDateTime.HOUR, "to_number(to_char(%s,'HH24'))");
        add(Ops.OpDateTime.MINUTE, "to_number(to_char(%s,'MI'))");
        add(Ops.OpDateTime.SECOND, "to_number(to_char(%s,'SS'))");
        
        add(Ops.OpDateTime.DAY_OF_MONTH, "to_number(to_char(%s,'DD'))");
        add(Ops.OpDateTime.DAY_OF_WEEK, "to_number(to_char(%s,'D'))");
        add(Ops.OpDateTime.DAY_OF_YEAR, "to_number(to_char(%s,'DDD'))");
        
        limitAndOffsetSymbols(false);
        limitTemplate("rownum < %s");
        offsetTemplate("rownum > %s");
        limitOffsetTemplate("rownum between %1$s and %3$s");
    }
}