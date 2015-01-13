package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SPrice is a Querydsl querydsl type for SPrice
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SPrice extends com.querydsl.sql.RelationalPathBase<SPrice> {

    private static final long serialVersionUID = 768137319;

    public static final SPrice price_ = new SPrice("price_");

    public final NumberPath<Long> amount = createNumber("amount", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> productId = createNumber("productId", Long.class);

    public final com.querydsl.sql.PrimaryKey<SPrice> primary = createPrimaryKey(id);

    public final com.querydsl.sql.ForeignKey<SItem> fkc59678362c7f0c58 = createForeignKey(productId, "id");

    public final com.querydsl.sql.ForeignKey<SCatalog_price> _fkaa04532f5222eaf7 = createInvForeignKey(id, "prices_id");

    public SPrice(String variable) {
        super(SPrice.class, forVariable(variable), "", "price_");
        addMetadata();
    }

    public SPrice(String variable, String schema, String table) {
        super(SPrice.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SPrice(Path<? extends SPrice> path) {
        super(path.getType(), path.getMetadata(), "", "price_");
        addMetadata();
    }

    public SPrice(PathMetadata<?> metadata) {
        super(SPrice.class, metadata, "", "price_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(amount, ColumnMetadata.named("amount").withIndex(2).ofType(-5).withSize(19).notNull());
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(-5).withSize(19).notNull());
        addMetadata(productId, ColumnMetadata.named("product_id").withIndex(3).ofType(-5).withSize(19));
    }

}

