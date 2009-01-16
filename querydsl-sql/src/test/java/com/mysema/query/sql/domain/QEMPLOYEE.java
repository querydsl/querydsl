/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql.domain;

import java.math.BigDecimal;

import com.mysema.query.grammar.types.Path;
import com.mysema.query.grammar.types.PathMetadata;

public class QEMPLOYEE extends Path.PEntity<java.lang.Object>{
//    stmt.execute("create table employee(id int, "
//            + "firstname VARCHAR(50), " + "lastname VARCHAR(50), "
//            + "salary decimal(10, 2), " + "superior_id int, "
//            + "CONSTRAINT PK_employee PRIMARY KEY (id), "
//            + "CONSTRAINT FK_superior FOREIGN KEY (superior_id) "
//            + "REFERENCES employee(ID))");
    public final Path.PComparable<java.lang.Integer> id = _comparable("id",java.lang.Integer.class);
    public final Path.PString firstname = _string("firstname");
    public final Path.PString lastname = _string("lastname");
	public final Path.PComparable<BigDecimal> salary = _comparable("salary",BigDecimal.class);
	public final Path.PComparable<java.lang.Integer> superiorId = _comparable("superior_id",java.lang.Integer.class);
	
    public QEMPLOYEE(java.lang.String path) {
      	super(java.lang.Object.class, "employee", path);
    }
    public QEMPLOYEE(PathMetadata<?> metadata) {
     	super(java.lang.Object.class, "employee", metadata);
    }
}