package com.mysema.query.grammar.hql.domain;

import javax.persistence.Entity;

/**
 * Item provides
 *
 * @author tiwe
 * @version $Id$
 */
@Entity
public class Item {
    protected long id;
    protected Product product;    
}
