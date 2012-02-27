package com.mysema.query.domain;

import java.util.ArrayList;
import java.util.List;

import com.mysema.query.annotations.QueryEmbedded;
import com.mysema.query.annotations.QueryEntity;

@QueryEntity
public class Superclass {
    
    @QueryEmbedded
    private List<IdNamePair<String>> fooOfSuperclass = new ArrayList<IdNamePair<String>>();

    public List<IdNamePair<String>> getFooOfSuperclass() {
        return fooOfSuperclass;
    }

    public void setFooOfSuperclass(List<IdNamePair<String>> fooOfSuperclass) {
        this.fooOfSuperclass = fooOfSuperclass;
    }
    
    
}