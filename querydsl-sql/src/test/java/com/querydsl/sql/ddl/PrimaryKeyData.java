/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.querydsl.sql.ddl;

import java.util.ArrayList;
import java.util.Arrays;
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
        columns.addAll(Arrays.asList(c));
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
