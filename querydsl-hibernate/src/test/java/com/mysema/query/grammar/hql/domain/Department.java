package com.mysema.query.grammar.hql.domain;

import java.util.List;

import javax.persistence.Entity;

/**
 * Department provides
 *
 * @author tiwe
 * @version $Id$
 */
@Entity
public class Department {
    String name; 
    Company company; 
    List<Employee> employees;
}
