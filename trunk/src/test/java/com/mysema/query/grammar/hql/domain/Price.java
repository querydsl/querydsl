package com.mysema.query.grammar.hql.domain;

import javax.persistence.Entity;

/**
 * Price provides
 *
 * @author tiwe
 * @version $Id$
 */
@Entity
public class Price {
    protected long amount;
    protected Product product;
}
