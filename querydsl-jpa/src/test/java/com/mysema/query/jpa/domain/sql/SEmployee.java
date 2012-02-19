package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SEmployee is a Querydsl query type for SEmployee
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SEmployee extends com.mysema.query.sql.RelationalPathBase<SEmployee> {

    private static final long serialVersionUID = 1816002207;

    public static final SEmployee employee = new SEmployee("EMPLOYEE_");

    public final NumberPath<Integer> companyId = createNumber("COMPANY_ID", Integer.class);

    public final StringPath firstname = createString("FIRSTNAME");

    public final NumberPath<Integer> id = createNumber("ID", Integer.class);

    public final StringPath lastname = createString("LASTNAME");

    public final NumberPath<Long> userId = createNumber("USER_ID", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SEmployee> sql120219232324010 = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SCompany> fk9d39ef71dc953998 = createForeignKey(companyId, "ID");

    public final com.mysema.query.sql.ForeignKey<SUser> fk9d39ef712743b59c = createForeignKey(userId, "ID");

    public final com.mysema.query.sql.ForeignKey<SCompany> _fkdc405382edf003bd = createInvForeignKey(id, "CEO_ID");

    public final com.mysema.query.sql.ForeignKey<SEmployeeJobfunctions> _fk49690e2f75b8f5bc = createInvForeignKey(id, "EMPLOYEE_ID");

    public SEmployee(String variable) {
        super(SEmployee.class, forVariable(variable), "APP", "EMPLOYEE_");
    }

    public SEmployee(Path<? extends SEmployee> entity) {
        super(entity.getType(), entity.getMetadata(), "APP", "EMPLOYEE_");
    }

    public SEmployee(PathMetadata<?> metadata) {
        super(SEmployee.class, metadata, "APP", "EMPLOYEE_");
    }

}

