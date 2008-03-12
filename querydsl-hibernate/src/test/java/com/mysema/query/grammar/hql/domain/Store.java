package com.mysema.query.grammar.hql.domain;

import java.util.List;

import javax.persistence.Entity;

/**
 * Store provides
 *
 * @author tiwe
 * @version $Id$
 */
@Entity
public class Store {
    List<Customer> customers;
    Location location;
}
