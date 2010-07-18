package com.mysema.query.jdoql.test.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.forVariable;

import com.mysema.query.sql.ForeignKey;
import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.Table;
import com.mysema.query.types.Expr;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.custom.CSimple;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PString;


/**
 * SStore is a Querydsl query type for SStore
 */
@Table(value="STORE")
public class SStore extends PEntity<SStore> {

    private static final long serialVersionUID = -1302810257;

    public static final SStore store = new SStore("STORE");

    public final PString name = createString("NAME");

    public final PNumber<Long> storeId = createNumber("STORE_ID", Long.class);

    public final PrimaryKey<SStore> sysIdx51 = new PrimaryKey<SStore>(this, storeId);

    public final ForeignKey<SStoreProductsbyname> _storeProductsbynameFk1 = new ForeignKey<SStoreProductsbyname>(this, storeId, "STORE_ID_OID");

    public final ForeignKey<SStoreProducts> _storeProductsFk1 = new ForeignKey<SStoreProducts>(this, storeId, "STORE_ID_OID");

    public SStore(String variable) {
        super(SStore.class, forVariable(variable));
    }

    public SStore(PEntity<? extends SStore> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SStore(PathMetadata<?> metadata) {
        super(SStore.class, metadata);
    }

    public Expr<Object[]> all() {
        return CSimple.create(Object[].class, "{0}.*", this);
    }

}

