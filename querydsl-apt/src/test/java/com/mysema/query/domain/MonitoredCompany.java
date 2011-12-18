package com.mysema.query.domain;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QueryInit;

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