/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.ddl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tiwe
 *
 */
public class PrimaryKeyData {

    private final String name;

    private final List<String> columns = new ArrayList<String>();

    public PrimaryKeyData(String name) {
        this.name = name;
    }
    
    public PrimaryKeyData(String name, String[] c) {
        this.name = name;
        for (String column : c) {
            columns.add(column);
        }
    }

    public void add(String column) {
        columns.add(column);
    }

    public String getName() {
        return name;
    }

    public List<String> getColumns() {
        return columns;
    }

}
