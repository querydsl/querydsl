package com.mysema.query.jpa.domain.sql;

import javax.annotation.Generated;

import com.mysema.query.sql.ColumnMetadata;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.NumberPath;
import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * SCompany_department is a Querydsl query type for SCompany_department
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SCompany_department extends com.mysema.query.sql.RelationalPathBase<SCompany_department> {

    private static final long serialVersionUID = -68475398;

    public static final SCompany_department company_department_ = new SCompany_department("company__department_");

    public final NumberPath<Integer> company_id = createNumber("company_id", Integer.class);

    public final NumberPath<Integer> departmentsId = createNumber("departmentsId", Integer.class);

    public final com.mysema.query.sql.ForeignKey<SDepartment> fk100ba6107d36c84d = createForeignKey(departmentsId, "id");

    public final com.mysema.query.sql.ForeignKey<SCompany> fk100ba610f0d30873 = createForeignKey(company_id, "id");

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

