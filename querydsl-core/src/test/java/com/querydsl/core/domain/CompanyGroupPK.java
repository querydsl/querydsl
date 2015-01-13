package com.querydsl.core.domain;

import java.io.Serializable;

import com.querydsl.core.annotations.QueryEmbeddable;

@SuppressWarnings("serial")
@QueryEmbeddable
public class CompanyGroupPK implements Serializable {

    private Long companyType;

    private String companyNumber;

    public Long getCompanyType() {
        return this.companyType;
    }

    public void setCompanyType(final Long aCompanyType) {
        this.companyType = aCompanyType;
    }

    public String getCompanyNumber() {
        return this.companyNumber;
    }

    public void setCompanyNumber(final String aCompanyNumber) {
        this.companyNumber = aCompanyNumber;
    }

}