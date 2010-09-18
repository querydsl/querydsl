package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

import com.mysema.query.sql.ForeignKey;
import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.sql.Table;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;


/**
 * SDepartment is a Querydsl query type for SDepartment
 */
@Table("DEPARTMENT")
public class SDepartment extends RelationalPathBase<SDepartment> {

    private static final long serialVersionUID = -774771365;

    public static final SDepartment department = new SDepartment("DEPARTMENT");

    public final NumberPath<Integer> companyId = createNumber("COMPANY_ID", Integer.class);

    public final NumberPath<Integer> id = createNumber("ID", Integer.class);

    public final StringPath name = createString("NAME");

    public final PrimaryKey<SDepartment> sql100819184432690 = createPrimaryKey(id);

    public final ForeignKey<SCompany> fka9601f72555fdbf0 = new ForeignKey<SCompany>(this, companyId, "ID");

    public SDepartment(String variable) {
        super(SDepartment.class, forVariable(variable));
    }

    public SDepartment(BeanPath<? extends SDepartment> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SDepartment(PathMetadata<?> metadata) {
        super(SDepartment.class, metadata);
    }

}

