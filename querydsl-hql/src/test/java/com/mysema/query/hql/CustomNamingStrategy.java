/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import org.hibernate.cfg.ImprovedNamingStrategy;

/**
 * CustomNamingStrategy provides.
 * 
 * @author tiwe
 * @version $Id$
 */
public class CustomNamingStrategy extends ImprovedNamingStrategy{
    /**
     * 
     */
    private static final long serialVersionUID = 945808987452961875L;

    public String classToTableName(String className) {
        return super.classToTableName(className).replace('$', '_');
    }
    public String tableName(String tableName) {
        return super.tableName(tableName).replace("$", "");
    }    
    public String columnName(String columnName) {
        return super.columnName(columnName).replace("$", "");
    }
    
}
