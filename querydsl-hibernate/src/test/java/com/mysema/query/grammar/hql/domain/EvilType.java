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
    EvilType isnull, isnotnull, asc, desc, get, getType, getMetadata;
    EvilType toString, hashCode, getClass, notify, notifyAll, wait;
}
