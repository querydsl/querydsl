package com.querydsl.core.domain;

import com.querydsl.core.annotations.QueryEntity;

@QueryEntity
public class CompanyGroup {

    private CompanyGroupPK key;

    private Company mainCompany;

    public CompanyGroupPK getKey() {
        return this.key;
    }

    public void setKey(final CompanyGroupPK aKey) {
        this.key = aKey;
    }

    public Company getMainCompany() {
        return this.mainCompany;
    }

    public void setMainCompany(final Company newMainCompany) {
        this.mainCompany = newMainCompany;
    }

}