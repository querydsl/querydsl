package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.DatePath;
import com.querydsl.core.types.path.NumberPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SCatalog is a Querydsl querydsl type for SCatalog
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SCatalog extends com.querydsl.sql.RelationalPathBase<SCatalog> {

    private static final long serialVersionUID = 669498199;

    public static final SCatalog catalog_ = new SCatalog("catalog_");

    public final DatePath<java.sql.Date> effectiveDate = createDate("effectiveDate", java.sql.Date.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final com.querydsl.sql.PrimaryKey<SCatalog> primary = createPrimaryKey(id);

    public final com.querydsl.sql.ForeignKey<SCatalog_price> _fkaa04532fbb9021ab = createInvForeignKey(id, "catalog__id");

    public SCatalog(String variable) {
        super(SCatalog.class, forVariable(variable), "", "catalog_");
        addMetadata();
    }

    public SCatalog(String variable, String schema, String table) {
        super(SCatalog.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SCatalog(Path<? extends SCatalog> path) {
        super(path.getType(), path.getMetadata(), "", "catalog_");
        addMetadata();
    }

    public SCatalog(PathMetadata<?> metadata) {
        super(SCatalog.class, metadata, "", "catalog_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(effectiveDate, ColumnMetadata.named("effectiveDate").withIndex(2).ofType(91).withSize(10));
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(4).withSize(10).notNull());
    }

}

