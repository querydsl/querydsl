package com.mysema.query.jdo.test.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.forVariable;

import com.mysema.query.sql.ForeignKey;
import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.sql.Table;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;


/**
 * SStoreProductsbyname is a Querydsl query type for SStoreProductsbyname
 */
@Table(value="STORE_PRODUCTSBYNAME")
public class SStoreProductsbyname extends RelationalPathBase<SStoreProductsbyname> {

    private static final long serialVersionUID = 764053781;

    public static final SStoreProductsbyname storeProductsbyname = new SStoreProductsbyname("STORE_PRODUCTSBYNAME");

    public final StringPath key = createString("KEY");

    public final NumberPath<Long> productIdVid = createNumber("PRODUCT_ID_VID", Long.class);

    public final NumberPath<Long> storeIdOid = createNumber("STORE_ID_OID", Long.class);

    public final PrimaryKey<SStoreProductsbyname> sysIdx53 = createPrimaryKey(key, storeIdOid);

    public final ForeignKey<SStore> storeProductsbynameFk1 = new ForeignKey<SStore>(this, storeIdOid, "STORE_ID");

    public final ForeignKey<SProduct> storeProductsbynameFk2 = new ForeignKey<SProduct>(this, productIdVid, "PRODUCT_ID");

    public SStoreProductsbyname(String variable) {
        super(SStoreProductsbyname.class, forVariable(variable));
    }

    public SStoreProductsbyname(BeanPath<? extends SStoreProductsbyname> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SStoreProductsbyname(PathMetadata<?> metadata) {
        super(SStoreProductsbyname.class, metadata);
    }

}

