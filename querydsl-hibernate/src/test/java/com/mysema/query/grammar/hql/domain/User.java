package com.mysema.query.grammar.hql.domain;

import javax.persistence.Entity;


/**
 * User provides
 *
 * @author tiwe
 * @version $Id$
 */
@Entity
public class User {
    Company company;
    long id;
    String userName, firstName, lastName;    
}
