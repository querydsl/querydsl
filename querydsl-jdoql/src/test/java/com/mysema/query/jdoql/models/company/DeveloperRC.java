/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.jdoql.models.company;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.mysema.query.annotations.Entity;

@Entity
public class DeveloperRC extends PersonRC {
    private Long id;
    private String SKILL;
    public double salary;

    /**
     * @return Returns the id.
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     *            The id to set.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @param id
     *            The id to set.
     */
    public void setId(BigDecimal id) {
        this.id = Long.valueOf(id.longValue());
    }

    /**
     * @param id
     *            The id to set.
     */
    public void setId(BigInteger id) {
        this.id = Long.valueOf(id.longValue());
    }

    /**
     * @return Returns the sKILL.
     */
    public String getSKILL() {
        return SKILL;
    }

    /**
     * @param skill
     *            The sKILL to set.
     */
    public void setSKILL(String skill) {
        SKILL = skill;
    }
}