package com.mysema.query.jpa.domain.sql;

import com.mysema.query.sql.ColumnMetadata;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * SEmployee is a Querydsl query type for SEmployee
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SEmployee extends com.mysema.query.sql.RelationalPathBase<SEmployee> {

    private static final long serialVersionUID = 461493664;

    public static final SEmployee employee_ = new SEmployee("employee_");

    public final NumberPath<Integer> companyId = createNumber("companyId", Integer.class);

    public final StringPath firstName = createString("firstName");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath lastName = createString("lastName");

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SEmployee> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SCompany> fk9d39ef71dc953998 = createForeignKey(companyId, "id");

    public final com.mysema.query.sql.ForeignKey<SUser> fk9d39ef712743b59c = createForeignKey(userId, "id");

    public final com.mysema.query.sql.ForeignKey<SCompany> _fkdc405382edf003bd = createInvForeignKey(id, "ceo_id");

    public final com.mysema.query.sql.ForeignKey<SEmployeeJobFunctions> _fk49690e2f75b8f5bc = createInvForeignKey(id, "Employee_id");

    public final com.mysema.query.sql.ForeignKey<SDepartment_employee> _fkc33a14ffd846a985 = createInvForeignKey(id, "employees_id");

    public SEmployee(String variable) {
        super(SEmployee.class, forVariable(variable), "null", "employee_");
        addMetadata();
    }

    public SEmployee(String variable, String schema, String table) {
        super(SEmployee.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SEmployee(Path<? extends SEmployee> path) {
        super(path.getType(), path.getMetadata(), "null", "employee_");
        addMetadata();
    }

    public SEmployee(PathMetadata<?> metadata) {
        super(SEmployee.class, metadata, "null", "employee_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(companyId, ColumnMetadata.named("company_id").withIndex(4).ofType(4).withSize(10));
        addMetadata(firstName, ColumnMetadata.named("firstName").withIndex(2).ofType(12).withSize(255));
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(4).withSize(10).notNull());
        addMetadata(lastName, ColumnMetadata.named("lastName").withIndex(3).ofType(12).withSize(255));
        addMetadata(userId, ColumnMetadata.named("user_id").withIndex(5).ofType(-5).withSize(19));
    }

}

