package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.forVariable;

import com.mysema.query.sql.ForeignKey;
import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.sql.Table;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.NumberPath;


/**
 * SFormula is a Querydsl query type for SFormula
 */
@Table("FORMULA")
public class SFormula extends RelationalPathBase<SFormula> {

    private static final long serialVersionUID = -227668995;

    public static final SFormula formula = new SFormula("FORMULA");

    public final NumberPath<Integer> id = createNumber("ID", Integer.class);

    public final NumberPath<Long> parameterId = createNumber("PARAMETER_ID", Long.class);

    public final PrimaryKey<SFormula> sql100819184433810 = createPrimaryKey(id);

    public final ForeignKey<SParameter> fk3ad7e94694c3fef0 = new ForeignKey<SParameter>(this, parameterId, "ID");

    public SFormula(String variable) {
        super(SFormula.class, forVariable(variable));
    }

    public SFormula(BeanPath<? extends SFormula> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SFormula(PathMetadata<?> metadata) {
        super(SFormula.class, metadata);
    }

}

