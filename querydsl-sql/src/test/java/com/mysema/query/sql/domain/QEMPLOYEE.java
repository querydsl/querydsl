/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql.domain;

import java.math.BigDecimal;

import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PathMetadata;
import com.mysema.query.util.NotEmpty;

@SuppressWarnings("all")
public class QEMPLOYEE extends PEntity<java.lang.Object> {
    // stmt.execute("create table employee(id int, "
    // + "firstname VARCHAR(50), " + "lastname VARCHAR(50), "
    // + "salary decimal(10, 2), " + "superior_id int, "
    // + "CONSTRAINT PK_employee PRIMARY KEY (id), "
    // + "CONSTRAINT FK_superior FOREIGN KEY (superior_id) "
    // + "REFERENCES employee(ID))");
    public final PNumber<java.lang.Integer> id = createNumber("id", java.lang.Integer.class);
    
    public final PString firstname = createString("firstname");
    
    public final PString lastname = createString("lastname");
    
    public final PNumber<BigDecimal> salary = createNumber("salary", BigDecimal.class);
    
    public final PNumber<java.lang.Integer> superiorId = createNumber("superior_id", java.lang.Integer.class);

    public QEMPLOYEE(@NotEmpty java.lang.String path) {
        super(java.lang.Object.class, "employee2", path);
    }

    public QEMPLOYEE(PathMetadata<?> metadata) {
        super(java.lang.Object.class, "employee2", metadata);
    }
}