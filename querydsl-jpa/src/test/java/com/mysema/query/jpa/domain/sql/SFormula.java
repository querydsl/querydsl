package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SFormula is a Querydsl query type for SFormula
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SFormula extends com.mysema.query.sql.RelationalPathBase<SFormula> {

    private static final long serialVersionUID = 1975056213;

    public static final SFormula formula = new SFormula("formula_");

    public final NumberPath<Integer> id = createNumber("ID", Integer.class);

    public final NumberPath<Long> parameterId = createNumber("PARAMETER_ID", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SFormula> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SParameter> formula_PARAMETERIDFK = createForeignKey(parameterId, "ID");

    public SFormula(String variable) {
        super(SFormula.class, forVariable(variable), "null", "formula_");
    }

    public SFormula(Path<? extends SFormula> path) {
        super(path.getType(), path.getMetadata(), "null", "formula_");
    }

    public SFormula(PathMetadata<?> metadata) {
        super(SFormula.class, metadata, "null", "formula_");
    }

}

