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
    protected Collection<Cat> kittens;
    protected Cat mate;
    protected int bodyWeight;
    protected String name;
    protected boolean alive;    
}
