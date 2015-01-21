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
public class PersonRC {
    // persistent member variables
    public long personNum;
    // persistent member variables
    private String globalNum;

    private String firstName;
    private String lastName;

    /**
     * @return Returns the firstName.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName
     *            The firstName to set.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return Returns the globalNum.
     */
    public String getGlobalNum() {
        return globalNum;
    }

    /**
     * @param globalNum
     *            The globalNum to set.
     */
    public void setGlobalNum(String globalNum) {
        this.globalNum = globalNum;
    }

    /**
     * @return Returns the lastName.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName
     *            The lastName to set.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @param num
     *            The personNum to set.
     */
    public void setPersonNum(long num) {
        this.personNum = num;
    }

    /**
     * @param num
     *            The personNum to set.
     */
    public void setPersonNum(BigDecimal num) {
        this.personNum = num.longValue();
    }

    /**
     * @param num
     *            The personNum to set.
     */
    public void setPersonNum(BigInteger num) {
        this.personNum = num.longValue();
    }
}
