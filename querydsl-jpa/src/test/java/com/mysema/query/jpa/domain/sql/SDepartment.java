package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SDepartment is a Querydsl query type for SDepartment
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SDepartment extends com.mysema.query.sql.RelationalPathBase<SDepartment> {

    private static final long serialVersionUID = 2101551875;

    public static final SDepartment department = new SDepartment("department_");

    public final NumberPath<Integer> companyId = createNumber("COMPANY_ID", Integer.class);

    public final NumberPath<Integer> id = createNumber("ID", Integer.class);

    public final StringPath name = createString("NAME");

    public final com.mysema.query.sql.PrimaryKey<SDepartment> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SCompany> department_COMPANYIDFK = createForeignKey(companyId, "id");

    public final com.mysema.query.sql.ForeignKey<SDepartment_employee> _department_employee_DepartmentIDFK = createInvForeignKey(id, "Department_ID");

    public final com.mysema.query.sql.ForeignKey<SCompany_department> _company_department_departmentsIDFK = createInvForeignKey(id, "departments_ID");

    public final com.mysema.query.sql.ForeignKey<SDepartment_employee> _fkc33a14ff7d2db0e1 = createInvForeignKey(id, "department__id");

    public SDepartment(String variable) {
        super(SDepartment.class, forVariable(variable), "null", "department_");
    }

    public SDepartment(Path<? extends SDepartment> path) {
        super(path.getType(), path.getMetadata(), "null", "department_");
    }

    public SDepartment(PathMetadata<?> metadata) {
        super(SDepartment.class, metadata, "null", "department_");
    }

}

