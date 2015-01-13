package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SCompany_department is a Querydsl querydsl type for SCompany_department
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SCompany_department extends com.querydsl.sql.RelationalPathBase<SCompany_department> {

    private static final long serialVersionUID = -68475398;

    public static final SCompany_department company_department_ = new SCompany_department("company__department_");

    public final NumberPath<Integer> company_id = createNumber("company_id", Integer.class);

    public final NumberPath<Integer> departmentsId = createNumber("departmentsId", Integer.class);

    public final com.querydsl.sql.ForeignKey<SDepartment> fk100ba6107d36c84d = createForeignKey(departmentsId, "id");

    public final com.querydsl.sql.ForeignKey<SCompany> fk100ba610f0d30873 = createForeignKey(company_id, "id");

    public SCompany_department(String variable) {
        super(SCompany_department.class, forVariable(variable), "", "company__department_");
        addMetadata();
    }

    public SCompany_department(String variable, String schema, String table) {
        super(SCompany_department.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SCompany_department(Path<? extends SCompany_department> path) {
        super(path.getType(), path.getMetadata(), "", "company__department_");
        addMetadata();
    }

    public SCompany_department(PathMetadata<?> metadata) {
        super(SCompany_department.class, metadata, "", "company__department_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(company_id, ColumnMetadata.named("company__id").withIndex(1).ofType(4).withSize(10).notNull());
        addMetadata(departmentsId, ColumnMetadata.named("departments_id").withIndex(2).ofType(4).withSize(10).notNull());
    }

}

