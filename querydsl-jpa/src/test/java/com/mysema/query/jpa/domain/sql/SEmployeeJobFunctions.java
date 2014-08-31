package com.mysema.query.jpa.domain.sql;

import javax.annotation.Generated;

import com.mysema.query.sql.ColumnMetadata;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;
import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * SEmployeeJobFunctions is a Querydsl query type for SEmployeeJobFunctions
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SEmployeeJobFunctions extends com.mysema.query.sql.RelationalPathBase<SEmployeeJobFunctions> {

    private static final long serialVersionUID = -666645347;

    public static final SEmployeeJobFunctions EmployeeJobFunctions = new SEmployeeJobFunctions("Employee_jobFunctions");

    public final NumberPath<Integer> employeeId = createNumber("employeeId", Integer.class);

    public final StringPath jobfunction = createString("jobfunction");

    public final com.mysema.query.sql.ForeignKey<SEmployee> fk49690e2f75b8f5bc = createForeignKey(employeeId, "id");

    public SEmployeeJobFunctions(String variable) {
        super(SEmployeeJobFunctions.class, forVariable(variable), "", "Employee_jobFunctions");
        addMetadata();
    }

    public SEmployeeJobFunctions(String variable, String schema, String table) {
        super(SEmployeeJobFunctions.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SEmployeeJobFunctions(Path<? extends SEmployeeJobFunctions> path) {
        super(path.getType(), path.getMetadata(), "", "Employee_jobFunctions");
        addMetadata();
    }

    public SEmployeeJobFunctions(PathMetadata<?> metadata) {
        super(SEmployeeJobFunctions.class, metadata, "", "Employee_jobFunctions");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(employeeId, ColumnMetadata.named("Employee_id").withIndex(1).ofType(4).withSize(10).notNull());
        addMetadata(jobfunction, ColumnMetadata.named("jobfunction").withIndex(2).ofType(12).withSize(255));
    }

}

