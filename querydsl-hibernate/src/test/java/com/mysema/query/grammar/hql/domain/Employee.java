package com.mysema.query.grammar.hql.domain;

import javax.persistence.Entity;

/**
 * Employee provides
 *
 * @author tiwe
 * @version $Id$
 */
@Entity
public class Employee {
    String firstName, lastName; 
    Company company;
}
