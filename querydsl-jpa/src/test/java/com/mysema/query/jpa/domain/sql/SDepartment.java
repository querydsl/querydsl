package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SDepartment is a Querydsl query type for SDepartment
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SDepartment extends com.mysema.query.sql.RelationalPathBase<SDepartment> {

    private static final long serialVersionUID = 2101551875;

    public static final SDepartment department = new SDepartment("DEPARTMENT_");

    public final NumberPath<Integer> companyId = createNumber("COMPANY_ID", Integer.class);

    public final NumberPath<Integer> id = createNumber("ID", Integer.class);

    public final StringPath name = createString("NAME");

    public final com.mysema.query.sql.PrimaryKey<SDepartment> sql120219232323110 = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SCompany> fk1f3a274ddc953998 = createForeignKey(companyId, "ID");

    public SDepartment(String variable) {
        super(SDepartment.class, forVariable(variable), "APP", "DEPARTMENT_");
    }

    public SDepartment(Path<? extends SDepartment> entity) {
        super(entity.getType(), entity.getMetadata(), "APP", "DEPARTMENT_");
    }

    public SDepartment(PathMetadata<?> metadata) {
        super(SDepartment.class, metadata, "APP", "DEPARTMENT_");
    }

}

