package com.mysema.query.grammar.hql.domain;

import java.util.List;

import javax.persistence.Entity;


/**
 * Company provides
 *
 * @author tiwe
 * @version $Id$
 */
@Entity
public class Company {
    long id;
    String name;
    List<Department> departments;
    Employee ceo;
}
