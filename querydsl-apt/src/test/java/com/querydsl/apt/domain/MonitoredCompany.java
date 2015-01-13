package com.querydsl.apt.domain;

import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.core.annotations.QueryInit;
import com.querydsl.core.domain.CompanyGroup;

@QueryEntity
public class MonitoredCompany {

    private Long key;

    @QueryInit("mainCompany")
    private CompanyGroup companyGroup;

    public Long getKey() {
        return key;
    }

    public void setKey(final Long aKey) {
        this.key = aKey;
    }

    public CompanyGroup getCompanyGroup() {
        return companyGroup;
    }

    public void setCompanyGroup(CompanyGroup aCompanyGroup) {
        this.companyGroup = aCompanyGroup;
    }

}