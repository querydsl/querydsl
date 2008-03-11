package com.mysema.query.grammar.hql.domain;

import javax.persistence.Entity;

import com.mysema.query.dto.DTO;

/**
 * Foo provides
 *
 * @author tiwe
 * @version $Id$
 */
@Entity
@DTO(domainType=Foo.class)
public class Foo {
    java.util.Date startDate;
    String bar;
    public Foo(){}
    public Foo(long l){}
}
