package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SCatalog is a Querydsl query type for SCatalog
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SCatalog extends com.mysema.query.sql.RelationalPathBase<SCatalog> {

    private static final long serialVersionUID = -1086781944;

    public static final SCatalog catalog = new SCatalog("CATALOG_");

    public final DatePath<java.sql.Date> effectivedate = createDate("EFFECTIVEDATE", java.sql.Date.class);

    public final NumberPath<Integer> id = createNumber("ID", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<SCatalog> sql120219232320770 = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SCatalog_price> _fkaa04532fbb9021ab = createInvForeignKey(id, "CATALOG__ID");

    public SCatalog(String variable) {
        super(SCatalog.class, forVariable(variable), "APP", "CATALOG_");
    }

    public SCatalog(Path<? extends SCatalog> entity) {
        super(entity.getType(), entity.getMetadata(), "APP", "CATALOG_");
    }

    public SCatalog(PathMetadata<?> metadata) {
        super(SCatalog.class, metadata, "APP", "CATALOG_");
    }

}

