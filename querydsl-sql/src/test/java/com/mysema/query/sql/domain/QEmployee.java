/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql.domain;

import java.math.BigDecimal;

import com.mysema.query.sql.Table;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.custom.CSimple;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PDate;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PTime;
import com.mysema.query.types.path.PathMetadataFactory;

@SuppressWarnings("all")
@Table("EMPLOYEE2")
public class QEmployee extends PEntity<QEmployee> {

    public Expr<Object[]> all(){
        return CSimple.create(Object[].class, "{0}.*", this);
    }
    
    public final PNumber<java.lang.Integer> id = createNumber("ID", java.lang.Integer.class);
    
    public final PString firstname = createString("FIRSTNAME");
    
    public final PString lastname = createString("LASTNAME");
    
    public final PNumber<BigDecimal> salary = createNumber("SALARY", BigDecimal.class);
    
    public final PDate<java.sql.Date> datefield = createDate("DATEFIELD", java.sql.Date.class);
    
    public final PTime<java.sql.Time> timefield = createTime("TIMEFIELD", java.sql.Time.class);
    
    public final PNumber<java.lang.Integer> superiorId = createNumber("SUPERIOR_ID", java.lang.Integer.class);

    public QEmployee(java.lang.String path) {
        super(QEmployee.class, PathMetadataFactory.forVariable(path));
    }

    public QEmployee(PathMetadata<?> metadata) {
        super(QEmployee.class, metadata);
    }
}