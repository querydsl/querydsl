/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql.domain;

import java.math.BigDecimal;

import com.mysema.query.types.path.PDate;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PTime;
import com.mysema.query.types.path.PathMetadata;

@SuppressWarnings("all")
public class QEMPLOYEE extends PEntity<java.lang.Object> {

    public final PNumber<java.lang.Integer> id = createNumber("id", java.lang.Integer.class);
    
    public final PString firstname = createString("firstname");
    
    public final PString lastname = createString("lastname");
    
    public final PNumber<BigDecimal> salary = createNumber("salary", BigDecimal.class);
    
    public final PDate<java.sql.Date> datefield = createDate("datefield", java.sql.Date.class);
    
    public final PTime<java.sql.Time> timefield = createTime("timefield", java.sql.Time.class);
    
    public final PNumber<java.lang.Integer> superiorId = createNumber("superior_id", java.lang.Integer.class);

    public QEMPLOYEE(java.lang.String path) {
        super(java.lang.Object.class, "employee2", PathMetadata.forVariable(path));
    }

    public QEMPLOYEE(PathMetadata<?> metadata) {
        super(java.lang.Object.class, "employee2", metadata);
    }
}