/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import java.math.BigDecimal;

import com.mysema.query.types.Ops;

/**
 * MySQLTemplates is an SQL dialect for MySQL
 *
 * tested with MySQL CE 5.1
 *
 * @author tiwe
 *
 */
public class MySQLTemplates extends SQLTemplates {

    public MySQLTemplates(){
        this(false);
    }

    public MySQLTemplates(boolean quote){
        super("`", quote);
        addClass2TypeMappings("bool", Boolean.class);
        addClass2TypeMappings("int", Integer.class);
        
        addClass2TypeMappings("decimal",
                Double.class,
                Float.class,
                BigDecimal.class);
//        addClass2TypeMappings("text", String.class);

        add(Ops.CONCAT, "concat({0}, {1})",0);
        add(Ops.MATCHES, "{0} regexp {1}");
        add(Ops.DateTimeOps.YEAR_MONTH, "extract(year_month from {0})");

    }

}
