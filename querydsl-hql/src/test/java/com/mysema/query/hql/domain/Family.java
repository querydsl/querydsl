/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.domain;

import com.mysema.query.annotations.QueryProjection;

/**
 * The Class Family.
 */
public class Family {
    
    @QueryProjection
    public Family(Cat mother, Cat mate, Cat offspr) {

    }
}