package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SEmployeeJOBFUNCTIONS is a Querydsl query type for SEmployeeJOBFUNCTIONS
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SEmployeeJOBFUNCTIONS2 extends com.mysema.query.sql.RelationalPathBase<SEmployeeJOBFUNCTIONS2> {

    private static final long serialVersionUID = -517652323;

    public static final SEmployeeJOBFUNCTIONS2 EmployeeJOBFUNCTIONS = new SEmployeeJOBFUNCTIONS2("Employee_JOBFUNCTIONS");

    public final NumberPath<Integer> employeeID = createNumber("Employee_ID", Integer.class);

    public final StringPath jobfunction = createString("jobfunction");

    public final com.mysema.query.sql.ForeignKey<SEmployee> employeeJOBFUNCTIONSEmployeeIDFK = createForeignKey(employeeID, "id");

    public SEmployeeJOBFUNCTIONS2(String variable) {
        super(SEmployeeJOBFUNCTIONS2.class, forVariable(variable), "null", "Employee_JOBFUNCTIONS");
    }

    public SEmployeeJOBFUNCTIONS2(Path<? extends SEmployeeJOBFUNCTIONS2> path) {
        super(path.getType(), path.getMetadata(), "null", "Employee_JOBFUNCTIONS");
    }

    public SEmployeeJOBFUNCTIONS2(PathMetadata<?> metadata) {
        super(SEmployeeJOBFUNCTIONS2.class, metadata, "null", "Employee_JOBFUNCTIONS");
    }

}

