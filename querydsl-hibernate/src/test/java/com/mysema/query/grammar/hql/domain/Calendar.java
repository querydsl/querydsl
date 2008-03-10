package com.mysema.query.grammar.hql.domain;

import java.util.Map;

import javax.persistence.Entity;

/**
 * Calendar provides
 *
 * @author tiwe
 * @version $Id$
 */
@Entity
public class Calendar {
    Map<String,java.util.Date> holidays;
}
