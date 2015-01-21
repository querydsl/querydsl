/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.jdo.models.company;

import com.querydsl.core.annotations.QueryEntity;

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
