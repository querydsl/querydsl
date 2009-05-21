/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql.domain;

import java.math.BigDecimal;

import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PathMetadata;

public class QEMPLOYEE extends PEntity<java.lang.Object>{
//    stmt.execute("create table employee(id int, "
//            + "firstname VARCHAR(50), " + "lastname VARCHAR(50), "
//            + "salary decimal(10, 2), " + "superior_id int, "
//            + "CONSTRAINT PK_employee PRIMARY KEY (id), "
//            + "CONSTRAINT FK_superior FOREIGN KEY (superior_id) "
//            + "REFERENCES employee(ID))");
    public final PNumber<java.lang.Integer> id = _number("id",java.lang.Integer.class);
    public final PString firstname = _string("firstname");
    public final PString lastname = _string("lastname");
	public final PNumber<BigDecimal> salary = _number("salary",BigDecimal.class);
	public final PNumber<java.lang.Integer> superiorId = _number("superior_id",java.lang.Integer.class);
	
    public QEMPLOYEE(java.lang.String path) {
      	super(java.lang.Object.class, "employee", path);
    }
    public QEMPLOYEE(PathMetadata<?> metadata) {
     	super(java.lang.Object.class, "employee", metadata);
    }
}