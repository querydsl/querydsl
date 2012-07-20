package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SEmployeeJobfunctions is a Querydsl query type for SEmployeeJobfunctions
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SEmployeeJobfunctions extends com.mysema.query.sql.RelationalPathBase<SEmployeeJobfunctions> {

    private static final long serialVersionUID = 1624353981;

    public static final SEmployeeJobfunctions employeeJobfunctions = new SEmployeeJobfunctions("employee_jobfunctions");

    public SEmployeeJobfunctions(String variable) {
        super(SEmployeeJobfunctions.class, forVariable(variable), "app", "employee_jobfunctions");
    }

    public SEmployeeJobfunctions(Path<? extends SEmployeeJobfunctions> path) {
        super(path.getType(), path.getMetadata(), "app", "employee_jobfunctions");
    }

    public SEmployeeJobfunctions(PathMetadata<?> metadata) {
        super(SEmployeeJobfunctions.class, metadata, "app", "employee_jobfunctions");
    }

}

