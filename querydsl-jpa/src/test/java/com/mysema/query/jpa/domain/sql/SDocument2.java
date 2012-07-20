package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SDocument2 is a Querydsl query type for SDocument2
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SDocument2 extends com.mysema.query.sql.RelationalPathBase<SDocument2> {

    private static final long serialVersionUID = -1232783194;

    public static final SDocument2 document2 = new SDocument2("document2_");

    public final NumberPath<Double> createdby = createNumber("CREATEDBY", Double.class);

    public final DateTimePath<java.sql.Timestamp> creationdate = createDateTime("CREATIONDATE", java.sql.Timestamp.class);

    public final NumberPath<Double> deletedby = createNumber("DELETEDBY", Double.class);

    public final DateTimePath<java.sql.Timestamp> deleteddate = createDateTime("DELETEDDATE", java.sql.Timestamp.class);

    public final StringPath documentbody = createString("DOCUMENTBODY");

    public final NumberPath<Double> documentstatus = createNumber("DOCUMENTSTATUS", Double.class);

    public final StringPath documentsummary = createString("DOCUMENTSUMMARY");

    public final StringPath documenttitle = createString("DOCUMENTTITLE");

    public final NumberPath<Double> documentversion = createNumber("DOCUMENTVERSION", Double.class);

    public final NumberPath<Double> entryid = createNumber("ENTRYID", Double.class);

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final DateTimePath<java.sql.Timestamp> modificationdate = createDateTime("MODIFICATIONDATE", java.sql.Timestamp.class);

    public final NumberPath<Double> modifiedby = createNumber("MODIFIEDBY", Double.class);

    public final com.mysema.query.sql.PrimaryKey<SDocument2> primary = createPrimaryKey(id);

    public SDocument2(String variable) {
        super(SDocument2.class, forVariable(variable), "null", "document2_");
    }

    public SDocument2(Path<? extends SDocument2> path) {
        super(path.getType(), path.getMetadata(), "null", "document2_");
    }

    public SDocument2(PathMetadata<?> metadata) {
        super(SDocument2.class, metadata, "null", "document2_");
    }

}

