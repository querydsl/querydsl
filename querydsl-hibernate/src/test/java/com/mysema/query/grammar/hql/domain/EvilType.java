package com.mysema.query.grammar.hql.domain;

import javax.persistence.Entity;

/**
 * EvilType provides
 *
 * @author tiwe
 * @version $Id$
 */
@Entity
public class EvilType {
    protected EvilType isnull, isnotnull, asc, desc, get, getType, getMetadata;
    protected EvilType toString, hashCode, getClass, notify, notifyAll, wait;
}
