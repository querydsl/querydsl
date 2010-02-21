package com.mysema.query.hql.domain.sql;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * SCompany is a Querydsl query type for SCompany
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="COMPANY")
public class SCompany extends PEntity<SCompany> {

    public final PNumber<Integer> ceoId = createNumber("CEO_ID", Integer.class);

    public final PNumber<Integer> id = createNumber("ID", Integer.class);

    public final PString name = createString("NAME");

    public SCompany(String variable) {
        super(SCompany.class, forVariable(variable));
    }

    public SCompany(PEntity<? extends SCompany> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SCompany(PathMetadata<?> metadata) {
        super(SCompany.class, metadata);
    }

}

