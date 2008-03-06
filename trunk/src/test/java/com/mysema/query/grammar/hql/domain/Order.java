package com.mysema.query.grammar.hql.domain;

import java.util.List;

import javax.persistence.Entity;

/**
 * Order provides
 *
 * @author tiwe
 * @version $Id$
 */
@Entity
public class Order {
    protected long id;
    protected List<Item> items, lineItems;    
    protected boolean paid;
    protected Customer customer;
}
