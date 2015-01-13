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

import java.math.BigDecimal;
import java.math.BigInteger;

import com.querydsl.core.annotations.QueryEntity;

@QueryEntity
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
