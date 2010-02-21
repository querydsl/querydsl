package com.mysema.query.hql.domain.sql;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * SFormula is a Querydsl query type for SFormula
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="FORMULA")
public class SFormula extends PEntity<SFormula> {

    public final PNumber<Integer> id = createNumber("ID", Integer.class);

    public final PNumber<Long> parameterId = createNumber("PARAMETER_ID", Long.class);

    public SFormula(String variable) {
        super(SFormula.class, forVariable(variable));
    }

    public SFormula(PEntity<? extends SFormula> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SFormula(PathMetadata<?> metadata) {
        super(SFormula.class, metadata);
    }

}

