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
import com.mysema.query.sql.Schema;
import com.mysema.query.sql.Table;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.PathMetadataFactory;
import com.mysema.query.types.path.DatePath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;
import com.mysema.query.types.path.TimePath;

@Schema("PUBLIC")
@Table("EMPLOYEE")
public class QEmployee extends RelationalPathBase<Employee> {

    private static final long serialVersionUID = 1394463749655231079L;
    
    public static final QEmployee employee = new QEmployee("EMPLOYEE");

    public final NumberPath<Integer> id = createNumber("ID", Integer.class);

    public final StringPath firstname = createString("FIRSTNAME");

    public final StringPath lastname = createString("LASTNAME");

    public final NumberPath<BigDecimal> salary = createNumber("SALARY", BigDecimal.class);

    public final DatePath<java.sql.Date> datefield = createDate("DATEFIELD", java.sql.Date.class);

    public final TimePath<java.sql.Time> timefield = createTime("TIMEFIELD", java.sql.Time.class);

    public final NumberPath<Integer> superiorId = createNumber("SUPERIOR_ID", Integer.class);

    public final PrimaryKey<Employee> idKey = createPrimaryKey(id);

    public final ForeignKey<Employee> superiorIdKey = createForeignKey(superiorId, "ID");

    public final ForeignKey<Employee> _superiorIdKey = createInvForeignKey(id, "SUPERIOR_ID");
    
    public QEmployee(String path) {
        super(Employee.class, PathMetadataFactory.forVariable(path));
    }

    public QEmployee(PathMetadata<?> metadata) {
        super(Employee.class, metadata);
    }

}
