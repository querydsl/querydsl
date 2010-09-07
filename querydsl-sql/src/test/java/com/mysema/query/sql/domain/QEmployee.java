/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql.domain;

import java.math.BigDecimal;

import com.mysema.query.sql.ForeignKey;
import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.sql.Table;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.PDate;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PTime;
import com.mysema.query.types.path.PathMetadataFactory;

@Table("EMPLOYEE2")
public class QEmployee extends RelationalPathBase<Employee> {

    private static final long serialVersionUID = 1394463749655231079L;
    
    public static final QEmployee employee = new QEmployee("EMPLOYEE2");

    public final PNumber<Integer> id = createNumber("ID", Integer.class);

    public final PString firstname = createString("FIRSTNAME");

    public final PString lastname = createString("LASTNAME");

    public final PNumber<BigDecimal> salary = createNumber("SALARY", BigDecimal.class);

    public final PDate<java.sql.Date> datefield = createDate("DATEFIELD", java.sql.Date.class);

    public final PTime<java.sql.Time> timefield = createTime("TIMEFIELD", java.sql.Time.class);

    public final PNumber<Integer> superiorId = createNumber("SUPERIOR_ID", Integer.class);

    public final PrimaryKey<Employee> idKey = createPrimaryKey(this, id);

    public final ForeignKey<Employee> superiorIdKey = createForeignKey(superiorId, "ID");
    
    public QEmployee(String path) {
        super(Employee.class, PathMetadataFactory.forVariable(path));
    }

    public QEmployee(PathMetadata<?> metadata) {
        super(Employee.class, metadata);
    }

}
