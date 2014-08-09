package com.mysema.query.jpa.domain.sql;

import javax.annotation.Generated;

import com.mysema.query.sql.ColumnMetadata;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.NumberPath;
import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * SCatalog_price is a Querydsl query type for SCatalog_price
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SCatalog_price extends com.mysema.query.sql.RelationalPathBase<SCatalog_price> {

    private static final long serialVersionUID = 1271141965;

    public static final SCatalog_price catalog_price_ = new SCatalog_price("catalog__price_");

    public final NumberPath<Integer> catalog_id = createNumber("catalog_id", Integer.class);

    public final NumberPath<Long> pricesId = createNumber("pricesId", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SCatalog_price> primary = createPrimaryKey(catalog_id, pricesId);

    public final com.mysema.query.sql.ForeignKey<SCatalog> fkaa04532fbb9021ab = createForeignKey(catalog_id, "id");

    public final com.mysema.query.sql.ForeignKey<SPrice> fkaa04532f5222eaf7 = createForeignKey(pricesId, "id");

    public SCatalog_price(String variable) {
        super(SCatalog_price.class, forVariable(variable), "", "catalog__price_");
        addMetadata();
    }

    public SCatalog_price(String variable, String schema, String table) {
        super(SCatalog_price.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SCatalog_price(Path<? extends SCatalog_price> path) {
        super(path.getType(), path.getMetadata(), "", "catalog__price_");
        addMetadata();
    }

    public SCatalog_price(PathMetadata<?> metadata) {
        super(SCatalog_price.class, metadata, "", "catalog__price_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(catalog_id, ColumnMetadata.named("catalog__id").withIndex(1).ofType(4).withSize(10).notNull());
        addMetadata(pricesId, ColumnMetadata.named("prices_id").withIndex(2).ofType(-5).withSize(19).notNull());
    }

}

