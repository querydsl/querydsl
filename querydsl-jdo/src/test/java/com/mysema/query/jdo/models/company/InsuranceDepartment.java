/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jdo.models.company;

import com.mysema.query.annotations.QueryEntity;

@QueryEntity
public class InsuranceDepartment extends Department {
    private String someInfo1;
    private String someInfo2;

    public InsuranceDepartment() {
    }

    public InsuranceDepartment(String name, String someInfo1, String someInfo2) {
        super(name);
    }

    public String getSomeInfo1() {
        return someInfo1;
    }

    public void setSomeInfo1(String someInfo1) {
        this.someInfo1 = someInfo1;
    }

    public String getSomeInfo2() {
        return someInfo2;
    }

    public void setSomeInfo2(String someInfo2) {
        this.someInfo2 = someInfo2;
    }
}
