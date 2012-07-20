package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SParameter is a Querydsl query type for SParameter
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SParameter extends com.mysema.query.sql.RelationalPathBase<SParameter> {

    private static final long serialVersionUID = -221865512;

    public static final SParameter parameter = new SParameter("parameter_");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SParameter> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SFormula> _formula_PARAMETERIDFK = createInvForeignKey(id, "PARAMETER_ID");

    public SParameter(String variable) {
        super(SParameter.class, forVariable(variable), "null", "parameter_");
    }

    public SParameter(Path<? extends SParameter> path) {
        super(path.getType(), path.getMetadata(), "null", "parameter_");
    }

    public SParameter(PathMetadata<?> metadata) {
        super(SParameter.class, metadata, "null", "parameter_");
    }

}

