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
    protected Company company;
    protected long id;
    protected String userName, firstName, lastName;    
}
