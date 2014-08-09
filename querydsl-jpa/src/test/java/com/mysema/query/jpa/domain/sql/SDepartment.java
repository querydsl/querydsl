package com.mysema.query.jpa.domain.sql;

import javax.annotation.Generated;

import com.mysema.query.sql.ColumnMetadata;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;
import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * SDepartment is a Querydsl query type for SDepartment
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SDepartment extends com.mysema.query.sql.RelationalPathBase<SDepartment> {

    private static final long serialVersionUID = 723598780;

    public static final SDepartment department_ = new SDepartment("department_");

    public final NumberPath<Integer> companyId = createNumber("companyId", Integer.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public final com.mysema.query.sql.PrimaryKey<SDepartment> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SCompany> fk1f3a274ddc953998 = createForeignKey(companyId, "id");

    public final com.mysema.query.sql.ForeignKey<SCompany_department> _fk100ba6107d36c84d = createInvForeignKey(id, "departments_id");

    public final com.mysema.query.sql.ForeignKey<SDepartment_employee> _fkc33a14ff7d2db0e1 = createInvForeignKey(id, "department__id");

    public SDepartment(String variable) {
        super(SDepartment.class, forVariable(variable), "", "department_");
        addMetadata();
    }

    public SDepartment(String variable, String schema, String table) {
        super(SDepartment.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SDepartment(Path<? extends SDepartment> path) {
        super(path.getType(), path.getMetadata(), "", "department_");
        addMetadata();
    }

    public SDepartment(PathMetadata<?> metadata) {
        super(SDepartment.class, metadata, "", "department_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(companyId, ColumnMetadata.named("company_id").withIndex(3).ofType(4).withSize(10));
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(4).withSize(10).notNull());
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(12).withSize(255));
    }

}

