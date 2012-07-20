package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SSymbol is a Querydsl query type for SSymbol
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SSymbol extends com.mysema.query.sql.RelationalPathBase<SSymbol> {

    private static final long serialVersionUID = 860607945;

    public static final SSymbol symbol = new SSymbol("symbol");

    public final NumberPath<Long> datatype = createNumber("datatype", Long.class);

    public final DateTimePath<java.sql.Timestamp> datetimeval = createDateTime("datetimeval", java.sql.Timestamp.class);

    public final NumberPath<java.math.BigDecimal> floatval = createNumber("floatval", java.math.BigDecimal.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> intval = createNumber("intval", Long.class);

    public final NumberPath<Integer> lang = createNumber("lang", Integer.class);

    public final StringPath lexical = createString("lexical");

    public final com.mysema.query.sql.PrimaryKey<SSymbol> primary = createPrimaryKey(id);

    public SSymbol(String variable) {
        super(SSymbol.class, forVariable(variable), "null", "symbol");
    }

    public SSymbol(Path<? extends SSymbol> path) {
        super(path.getType(), path.getMetadata(), "null", "symbol");
    }

    public SSymbol(PathMetadata<?> metadata) {
        super(SSymbol.class, metadata, "null", "symbol");
    }

}

