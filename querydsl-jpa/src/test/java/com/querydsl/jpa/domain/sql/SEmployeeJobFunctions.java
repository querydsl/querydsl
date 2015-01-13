package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.StringPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SEmployeeJobFunctions is a Querydsl querydsl type for SEmployeeJobFunctions
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SEmployeeJobFunctions extends com.querydsl.sql.RelationalPathBase<SEmployeeJobFunctions> {

    private static final long serialVersionUID = -666645347;

    public static final SEmployeeJobFunctions EmployeeJobFunctions = new SEmployeeJobFunctions("Employee_jobFunctions");

    public final NumberPath<Integer> employeeId = createNumber("employeeId", Integer.class);

    public final StringPath jobfunction = createString("jobfunction");

    public final com.querydsl.sql.ForeignKey<SEmployee> fk49690e2f75b8f5bc = createForeignKey(employeeId, "id");

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

