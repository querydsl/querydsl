package com.mysema.query.grammar.hql.domain;

import javax.persistence.Entity;

/**
 * Person provides
 *
 * @author tiwe
 * @version $Id$
 */
@Entity
public class Person {
    PersonId id;
    Nationality nationality;
    java.util.Date birthDay;
}
