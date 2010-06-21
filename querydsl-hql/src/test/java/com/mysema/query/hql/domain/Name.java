/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.hql.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * The Class Name.
 */
@Entity
public class Name {
    String firstName, lastName, nickName;

    @Id
    long id;
}
