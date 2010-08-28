/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.support;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class DummyEntity {

    private DummyEntity other;
    
    private List<DummyEntity> list;
    
    private Set<DummyEntity> set;
    
    private Map<String,DummyEntity> map;

    public List<DummyEntity> getList() {
        return list;
    }

    public Set<DummyEntity> getSet() {
        return set;
    }

    public Map<String, DummyEntity> getMap() {
        return map;
    }

    public DummyEntity getOther() {
        return other;
    }
    
}
