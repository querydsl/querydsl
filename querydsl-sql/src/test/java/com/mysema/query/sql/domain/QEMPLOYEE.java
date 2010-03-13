/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql.domain;

import java.math.BigDecimal;

import com.mysema.query.sql.Table;
import com.mysema.query.types.custom.CSimple;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PDate;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PTime;
import com.mysema.query.types.path.PathMetadata;
import com.mysema.query.types.path.PathMetadataFactory;

@SuppressWarnings("all")
@Table("employee2")
public class QEMPLOYEE extends PEntity<QEMPLOYEE> {

    public Expr<Object[]> all(){
        return CSimple.create(Object[].class, "{0}.*", this);
    }
    
    public final PNumber<java.lang.Integer> id = createNumber("id", java.lang.Integer.class);
    
    public final PString firstname = createString("firstname");
    
    public final PString lastname = createString("lastname");
    
    public final PNumber<BigDecimal> salary = createNumber("salary", BigDecimal.class);
    
    public final PDate<java.sql.Date> datefield = createDate("datefield", java.sql.Date.class);
    
    public final PTime<java.sql.Time> timefield = createTime("timefield", java.sql.Time.class);
    
    public final PNumber<java.lang.Integer> superiorId = createNumber("superior_id", java.lang.Integer.class);

    public QEMPLOYEE(java.lang.String path) {
        super(QEMPLOYEE.class, PathMetadataFactory.forVariable(path));
    }

    public QEMPLOYEE(PathMetadata<?> metadata) {
        super(QEMPLOYEE.class, metadata);
    }
}