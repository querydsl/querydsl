package com.mysema.query.grammar.hql.domain;

import java.util.Date;

import javax.persistence.Entity;

/**
 * Document provides
 *
 * @author tiwe
 * @version $Id$
 */
@Entity
public class Document {
    String name;
    Date validTo;
}
