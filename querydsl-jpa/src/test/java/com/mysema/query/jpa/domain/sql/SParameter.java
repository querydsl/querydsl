package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SParameter is a Querydsl query type for SParameter
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SParameter extends com.mysema.query.sql.RelationalPathBase<SParameter> {

    private static final long serialVersionUID = -221865512;

    public static final SParameter parameter = new SParameter("PARAMETER_");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SParameter> sql120219232327410 = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SFormula> _fk1c4adbb924189298 = createInvForeignKey(id, "PARAMETER_ID");

    public SParameter(String variable) {
        super(SParameter.class, forVariable(variable), "APP", "PARAMETER_");
    }

    public SParameter(Path<? extends SParameter> entity) {
        super(entity.getType(), entity.getMetadata(), "APP", "PARAMETER_");
    }

    public SParameter(PathMetadata<?> metadata) {
        super(SParameter.class, metadata, "APP", "PARAMETER_");
    }

}

