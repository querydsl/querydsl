package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

import com.mysema.query.sql.ForeignKey;
import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.RelationalPath;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.NumberPath;


/**
 * SParameter is a Querydsl query type for SParameter
 */
public class SParameter extends RelationalPathBase<SParameter> implements RelationalPath<SParameter> {

    private static final long serialVersionUID = 378086528;

    public static final SParameter parameter = new SParameter("PARAMETER");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final PrimaryKey<SParameter> sql100819184436610 = createPrimaryKey(id);

    public final ForeignKey<SFormula> _fk3ad7e94694c3fef0 = new ForeignKey<SFormula>(this, id, "PARAMETER_ID");

    public SParameter(String variable) {
        super(SParameter.class, forVariable(variable), null, "PARAMETER");
    }

    public SParameter(BeanPath<? extends SParameter> entity) {
        super(entity.getType(), entity.getMetadata(), null, "PARAMETER");
    }

    public SParameter(PathMetadata<?> metadata) {
        super(SParameter.class, metadata, null, "PARAMETER");
    }

}

