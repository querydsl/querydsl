/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql.support;

import java.util.ArrayList;
import java.util.List;

import com.mysema.commons.lang.Assert;

/**
 * @author tiwe
 *
 */
public class PrimaryKeyData {

    private final String name;

    private final List<String> columns = new ArrayList<String>();

    public PrimaryKeyData(String name) {
        this.name = Assert.notNull(name,"name");
    }

    public void add(String column){
        columns.add(column);
    }

    public String getName() {
        return name;
    }

    public List<String> getColumns() {
        return columns;
    }

}
