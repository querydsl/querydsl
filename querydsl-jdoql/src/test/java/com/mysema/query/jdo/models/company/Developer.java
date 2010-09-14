/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jdo.models.company;

import com.mysema.query.annotations.QueryEntity;

/**
 * Developer of software for a system.
 *
 * @version $Revision: 1.1 $
 */
@QueryEntity
public class Developer extends Employee {
    private String SKILL;

    public Developer() {
    }

    public Developer(long id, String firstname, String lastname, String email,
            float sal, String serial, Integer yearsInCompany, String skill) {
        super(id, firstname, lastname, email, sal, serial, yearsInCompany);
        SKILL = skill;
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
