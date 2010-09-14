package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.forVariable;

import com.mysema.query.sql.ForeignKey;
import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.RelationalPath;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.sql.Table;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.NumberPath;


/**
 * SParameter is a Querydsl query type for SParameter
 */
@Table("PARAMETER")
public class SParameter extends RelationalPathBase<SParameter> implements RelationalPath<SParameter> {

    private static final long serialVersionUID = 378086528;

    public static final SParameter parameter = new SParameter("PARAMETER");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final PrimaryKey<SParameter> sql100819184436610 = createPrimaryKey(id);

    public final ForeignKey<SFormula> _fk3ad7e94694c3fef0 = new ForeignKey<SFormula>(this, id, "PARAMETER_ID");

    public SParameter(String variable) {
        super(SParameter.class, forVariable(variable));
    }

    public SParameter(BeanPath<? extends SParameter> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SParameter(PathMetadata<?> metadata) {
        super(SParameter.class, metadata);
    }

}

