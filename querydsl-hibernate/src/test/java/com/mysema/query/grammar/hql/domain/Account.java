package com.mysema.query.grammar.hql.domain;

import javax.persistence.Entity;

/**
 * Account provides
 *
 * @author tiwe
 * @version $Id$
 */
@Entity
public class Account {
    Person owner;
}
