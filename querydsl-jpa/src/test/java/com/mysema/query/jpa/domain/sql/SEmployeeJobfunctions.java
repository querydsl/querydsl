package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SEmployeeJobfunctions is a Querydsl query type for SEmployeeJobfunctions
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SEmployeeJobfunctions extends com.mysema.query.sql.RelationalPathBase<SEmployeeJobfunctions> {

    private static final long serialVersionUID = 1624353981;

    public static final SEmployeeJobfunctions employeeJobfunctions = new SEmployeeJobfunctions("EMPLOYEE_JOBFUNCTIONS");

    public final NumberPath<Integer> employeeId = createNumber("EMPLOYEE_ID", Integer.class);

    public final StringPath jobfunction = createString("JOBFUNCTION");

    public final com.mysema.query.sql.ForeignKey<SEmployee> fk49690e2f75b8f5bc = createForeignKey(employeeId, "ID");

    public SEmployeeJobfunctions(String variable) {
        super(SEmployeeJobfunctions.class, forVariable(variable), "APP", "EMPLOYEE_JOBFUNCTIONS");
    }

    public SEmployeeJobfunctions(Path<? extends SEmployeeJobfunctions> entity) {
        super(entity.getType(), entity.getMetadata(), "APP", "EMPLOYEE_JOBFUNCTIONS");
    }

    public SEmployeeJobfunctions(PathMetadata<?> metadata) {
        super(SEmployeeJobfunctions.class, metadata, "APP", "EMPLOYEE_JOBFUNCTIONS");
    }

}

