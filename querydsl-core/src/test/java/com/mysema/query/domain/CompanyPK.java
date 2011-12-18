package com.mysema.query.domain;

import com.mysema.query.annotations.QueryEmbeddable;

@QueryEmbeddable
public class CompanyPK {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
}
