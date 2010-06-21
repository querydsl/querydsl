/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jdoql.models.company;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.mysema.query.annotations.QueryEntity;

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
