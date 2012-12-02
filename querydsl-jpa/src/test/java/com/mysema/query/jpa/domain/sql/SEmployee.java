package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SEmployee is a Querydsl query type for SEmployee
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SEmployee extends com.mysema.query.sql.RelationalPathBase<SEmployee> {

    private static final long serialVersionUID = 1816002207;

    public static final SEmployee employee = new SEmployee("employee_");

    public final NumberPath<Integer> companyId = createNumber("company_id", Integer.class);

    public final StringPath firstName = createString("firstName");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath lastName = createString("lastName");

    public final NumberPath<Long> userId = createNumber("user_id", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SEmployee> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SCompany> fk9d39ef71dc953998 = createForeignKey(companyId, "id");

    public final com.mysema.query.sql.ForeignKey<SCompany> employee_COMPANYIDFK = createForeignKey(companyId, "id");

    public final com.mysema.query.sql.ForeignKey<SUser> employee_USERIDFK = createForeignKey(userId, "id");

    public final com.mysema.query.sql.ForeignKey<SUser> fk9d39ef712743b59c = createForeignKey(userId, "id");

    public final com.mysema.query.sql.ForeignKey<SDepartment_employee> _department_employee_employeesIDFK = createInvForeignKey(id, "employees_ID");

    public final com.mysema.query.sql.ForeignKey<SCompany> _fkdc405382edf003bd = createInvForeignKey(id, "ceo_id");

    public final com.mysema.query.sql.ForeignKey<SCompany> _company_CEOIDFK = createInvForeignKey(id, "ceo_id");

    public final com.mysema.query.sql.ForeignKey<SEmployeeJOBFUNCTIONS2> _employeeJOBFUNCTIONSEmployeeIDFK = createInvForeignKey(id, "Employee_ID");

    public final com.mysema.query.sql.ForeignKey<SEmployeeJobFunctions> _fk49690e2f75b8f5bc = createInvForeignKey(id, "Employee_id");

    public SEmployee(String variable) {
        super(SEmployee.class, forVariable(variable), "null", "employee_");
    }

    public SEmployee(Path<? extends SEmployee> path) {
        super(path.getType(), path.getMetadata(), "null", "employee_");
    }

    public SEmployee(PathMetadata<?> metadata) {
        super(SEmployee.class, metadata, "null", "employee_");
    }

}

