/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa.domain;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * The Class Player.
 */
@Entity
public class Player {
    @Id
    long id;

    @ElementCollection
    List<Integer> scores;
}
