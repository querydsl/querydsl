package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SDepartment_employee is a Querydsl query type for SDepartment_employee
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SDepartment_employee extends com.mysema.query.sql.RelationalPathBase<SDepartment_employee> {

    private static final long serialVersionUID = -235362198;

    public static final SDepartment_employee department_employee = new SDepartment_employee("department__employee_");

    public final NumberPath<Integer> _index = createNumber("_index", Integer.class);

    public final NumberPath<Integer> department_id = createNumber("department__id", Integer.class);

    public final NumberPath<Integer> departmentID = createNumber("Department_ID", Integer.class);

    public final NumberPath<Integer> employeesID = createNumber("employees_ID", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<SDepartment_employee> primary = createPrimaryKey(departmentID, employeesID);

    public final com.mysema.query.sql.ForeignKey<SEmployee> department_employee_employeesIDFK = createForeignKey(employeesID, "id");

    public final com.mysema.query.sql.ForeignKey<SDepartment> department_employee_DepartmentIDFK = createForeignKey(departmentID, "ID");

    public final com.mysema.query.sql.ForeignKey<SDepartment> fkc33a14ff7d2db0e1 = createForeignKey(department_id, "ID");

    public SDepartment_employee(String variable) {
        super(SDepartment_employee.class, forVariable(variable), "null", "department__employee_");
    }

    public SDepartment_employee(Path<? extends SDepartment_employee> path) {
        super(path.getType(), path.getMetadata(), "null", "department__employee_");
    }

    public SDepartment_employee(PathMetadata<?> metadata) {
        super(SDepartment_employee.class, metadata, "null", "department__employee_");
    }

}

