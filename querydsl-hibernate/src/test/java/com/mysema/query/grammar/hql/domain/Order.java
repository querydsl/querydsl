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
    long id;
    List<Item> items, lineItems;    
    List<Integer> deliveredItemIndices;
    boolean paid;
    Customer customer;
}
