package com.mysema.query.domain;

import com.mysema.query.annotations.QuerySupertype;

@QuerySupertype
public class CommonPersistence {
    private Long version;

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
        
}