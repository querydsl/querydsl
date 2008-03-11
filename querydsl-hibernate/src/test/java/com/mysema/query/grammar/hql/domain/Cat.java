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
public class Cat extends Animal{
    Collection<Cat> kittens;
    Cat mate;   
    Color eyecolor;
    int breed;
}
