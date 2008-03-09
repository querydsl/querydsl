package com.mysema.query.grammar.hql.domain;

import java.util.Collection;

import javax.persistence.Entity;

/**
 * Cat provides
 *
 * @author tiwe
 * @version $Id$
 */
@Entity
public class Cat {
    Collection<Cat> kittens;
    Cat mate;
    int bodyWeight;
    String name;
    boolean alive;    
}
