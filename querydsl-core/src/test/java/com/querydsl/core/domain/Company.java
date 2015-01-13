package com.querydsl.core.domain;

import com.querydsl.core.annotations.QueryEntity;

@QueryEntity
public class Company {
    
    private CompanyPK key;

    public CompanyPK getKey() {
        return key;
    }

    public void setKey(final CompanyPK aKey) {
        this.key = aKey;
    }

}