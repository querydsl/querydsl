package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SCompany_department is a Querydsl query type for SCompany_department
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SCompany_department extends com.mysema.query.sql.RelationalPathBase<SCompany_department> {

    private static final long serialVersionUID = -140756219;

    public static final SCompany_department company_department = new SCompany_department("company__department_");

    public final NumberPath<Integer> _index = createNumber("_index", Integer.class);

    public final NumberPath<Integer> company_id = createNumber("company__id", Integer.class);

    public final NumberPath<Integer> companyID = createNumber("Company_ID", Integer.class);

    public final NumberPath<Integer> departmentsID = createNumber("departments_ID", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<SCompany_department> primary = createPrimaryKey(companyID, departmentsID);

    public final com.mysema.query.sql.ForeignKey<SCompany> company_department_CompanyIDFK = createForeignKey(companyID, "id");

    public final com.mysema.query.sql.ForeignKey<SDepartment> company_department_departmentsIDFK = createForeignKey(departmentsID, "ID");

    public final com.mysema.query.sql.ForeignKey<SCompany> fk100ba610f0d30873 = createForeignKey(company_id, "id");

    public SCompany_department(String variable) {
        super(SCompany_department.class, forVariable(variable), "null", "company__department_");
    }

    public SCompany_department(Path<? extends SCompany_department> path) {
        super(path.getType(), path.getMetadata(), "null", "company__department_");
    }

    public SCompany_department(PathMetadata<?> metadata) {
        super(SCompany_department.class, metadata, "null", "company__department_");
    }

}

