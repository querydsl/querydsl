package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;


/**
 * SDepartment_employee is a Querydsl query type for SDepartment_employee
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SDepartment_employee extends com.mysema.query.sql.RelationalPathBase<SDepartment_employee> {

    private static final long serialVersionUID = 1293706549;

    public static final SDepartment_employee department_employee_ = new SDepartment_employee("department__employee_");

    public final NumberPath<Integer> department_id = createNumber("department_id", Integer.class);

    public final NumberPath<Integer> employeesId = createNumber("employeesId", Integer.class);

    public final com.mysema.query.sql.ForeignKey<SEmployee> fkc33a14ffd846a985 = createForeignKey(employeesId, "id");

    public final com.mysema.query.sql.ForeignKey<SDepartment> fkc33a14ff7d2db0e1 = createForeignKey(department_id, "id");

    public SDepartment_employee(String variable) {
        super(SDepartment_employee.class, forVariable(variable), "null", "department__employee_");
        addMetadata();
    }

    public SDepartment_employee(String variable, String schema, String table) {
        super(SDepartment_employee.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SDepartment_employee(Path<? extends SDepartment_employee> path) {
        super(path.getType(), path.getMetadata(), "null", "department__employee_");
        addMetadata();
    }

    public SDepartment_employee(PathMetadata<?> metadata) {
        super(SDepartment_employee.class, metadata, "null", "department__employee_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(department_id, ColumnMetadata.named("department__id").withIndex(1).ofType(4).withSize(10).notNull());
        addMetadata(employeesId, ColumnMetadata.named("employees_id").withIndex(2).ofType(4).withSize(10).notNull());
    }

}

