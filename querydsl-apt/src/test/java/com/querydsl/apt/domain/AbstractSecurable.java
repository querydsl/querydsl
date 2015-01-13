package com.querydsl.apt.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

@MappedSuperclass

public abstract class AbstractSecurable<U, PK extends Serializable> {

    private static final long serialVersionUID = -6151927948714098845L;

    // ~ Meta-data
    @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
    @JoinColumn(name = "SOURCE")
    private List<SecurableEntity> securityEntries;

}