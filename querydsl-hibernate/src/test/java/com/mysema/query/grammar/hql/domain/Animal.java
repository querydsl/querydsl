package com.mysema.query.grammar.hql.domain;

import javax.persistence.Entity;

/**
 * Animal provides
 *
 * @author tiwe
 * @version $Id$
 */
@Entity
public class Animal {
    int bodyWeight, id, weight, toes;
    String name;
    Color color;
    boolean alive;    
    java.util.Date birthdate;
}
