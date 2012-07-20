package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SDocumentprop is a Querydsl query type for SDocumentprop
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SDocumentprop extends com.mysema.query.sql.RelationalPathBase<SDocumentprop> {

    private static final long serialVersionUID = 423175791;

    public static final SDocumentprop documentprop = new SDocumentprop("documentprop_");

    public final NumberPath<Double> documentid = createNumber("DOCUMENTID", Double.class);

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final StringPath propname = createString("PROPNAME");

    public final StringPath propvalue = createString("PROPVALUE");

    public final StringPath propvaluedetails = createString("PROPVALUEDETAILS");

    public final com.mysema.query.sql.PrimaryKey<SDocumentprop> primary = createPrimaryKey(id);

    public SDocumentprop(String variable) {
        super(SDocumentprop.class, forVariable(variable), "null", "documentprop_");
    }

    public SDocumentprop(Path<? extends SDocumentprop> path) {
        super(path.getType(), path.getMetadata(), "null", "documentprop_");
    }

    public SDocumentprop(PathMetadata<?> metadata) {
        super(SDocumentprop.class, metadata, "null", "documentprop_");
    }

}

