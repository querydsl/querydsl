package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import com.mysema.query.sql.*;
import java.util.*;


/**
 * SStore is a Querydsl query type for SStore
 */
@Table("STORE")
public class SStore extends BeanPath<SStore> implements RelationalPath<SStore> {

    private static final long serialVersionUID = -1641714376;

    public static final SStore store = new SStore("STORE");

    public final PNumber<Long> id = createNumber("ID", Long.class);

    public final PNumber<Long> locationId = createNumber("LOCATION_ID", Long.class);

    private Expr[] _all;

    public final PrimaryKey<SStore> sql100819184439380 = new PrimaryKey<SStore>(this, id);

    public final ForeignKey<SLocation> fk4c808c12adf2d04 = new ForeignKey<SLocation>(this, locationId, "ID");

    public final ForeignKey<SStoreCustomer> _fk808055bc828daef0 = new ForeignKey<SStoreCustomer>(this, id, "STORE_ID");

    public SStore(String variable) {
        super(SStore.class, forVariable(variable));
    }

    public SStore(BeanPath<? extends SStore> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SStore(PathMetadata<?> metadata) {
        super(SStore.class, metadata);
    }

    public Expr[] all() {
        if (_all == null) {
            _all = new Expr[]{id, locationId};
        }
        return _all;
    }

    public PrimaryKey<SStore> getPrimaryKey() {
        return sql100819184439380;
    }

    public List<ForeignKey<?>> getForeignKeys() {
        return Arrays.<ForeignKey<?>>asList(fk4c808c12adf2d04);
    }

    public List<ForeignKey<?>> getInverseForeignKeys() {
        return Arrays.<ForeignKey<?>>asList(_fk808055bc828daef0);
    }

}

