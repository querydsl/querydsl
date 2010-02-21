package com.mysema.query.hql.domain.sql;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * SDepartment is a Querydsl query type for SDepartment
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="DEPARTMENT")
public class SDepartment extends PEntity<SDepartment> {

    public final PNumber<Integer> companyId = createNumber("COMPANY_ID", Integer.class);

    public final PNumber<Integer> id = createNumber("ID", Integer.class);

    public final PString name = createString("NAME");

    public SDepartment(String variable) {
        super(SDepartment.class, forVariable(variable));
    }

    public SDepartment(PEntity<? extends SDepartment> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SDepartment(PathMetadata<?> metadata) {
        super(SDepartment.class, metadata);
    }

}

