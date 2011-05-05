/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.domain;

import org.junit.Ignore;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QuerySupertype;

@Ignore
public class SuperclassTest {

    @QuerySupertype
    public static class SuperclassTestSuperclass{

    }

    @QuerySupertype
    public static class SuperclassTestSuperclass2{
        SuperclassTestEntity ref;
    }

    @QueryEntity
    public static class SuperclassTestEntity{
        SuperclassTestSuperclass ref;
        SuperclassTestSuperclass2 ref2;
    }

}
