package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import com.mysema.query.sql.*;
import java.util.*;


/**
 * SItem is a Querydsl query type for SItem
 */
@Table("ITEM")
public class SItem extends BeanPath<SItem> implements RelationalPath<SItem> {

    private static final long serialVersionUID = 500932572;

    public static final SItem item = new SItem("ITEM");

    public final PNumber<Long> currentstatusId = createNumber("CURRENTSTATUS_ID", Long.class);

    public final PString dtype = createString("DTYPE");

    public final PNumber<Long> id = createNumber("ID", Long.class);

    public final PNumber<Integer> name = createNumber("NAME", Integer.class);

    public final PNumber<Long> productId = createNumber("PRODUCT_ID", Long.class);

    public final PNumber<Long> statusId = createNumber("STATUS_ID", Long.class);

    private Expr[] _all;

    public final PrimaryKey<SItem> sql100819184434350 = new PrimaryKey<SItem>(this, id);

    public final ForeignKey<SItem> fk22ef33a549aeb0 = new ForeignKey<SItem>(this, productId, "ID");

    public final ForeignKey<SStatus> fk22ef33eedeba64 = new ForeignKey<SStatus>(this, statusId, "ID");

    public final ForeignKey<SStatus> fk22ef33bb4e150b = new ForeignKey<SStatus>(this, currentstatusId, "ID");

    public final ForeignKey<SAuditlog> _fk3e07a1891bee4d44 = new ForeignKey<SAuditlog>(this, id, "ITEM_ID");

    public final ForeignKey<SItemStatuschange> _fkc2c9ebee9e7e0323 = new ForeignKey<SItemStatuschange>(this, id, "ITEM_ID");

    public final ForeignKey<SItem> _fk22ef33a549aeb0 = new ForeignKey<SItem>(this, id, "PRODUCT_ID");

    public final ForeignKey<SPrice> _fk49cc129a549aeb0 = new ForeignKey<SPrice>(this, id, "PRODUCT_ID");

    public SItem(String variable) {
        super(SItem.class, forVariable(variable));
    }

    public SItem(BeanPath<? extends SItem> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SItem(PathMetadata<?> metadata) {
        super(SItem.class, metadata);
    }

    public Expr[] all() {
        if (_all == null) {
            _all = new Expr[]{currentstatusId, dtype, id, name, productId, statusId};
        }
        return _all;
    }

    public PrimaryKey<SItem> getPrimaryKey() {
        return sql100819184434350;
    }

    public List<ForeignKey<?>> getForeignKeys() {
        return Arrays.<ForeignKey<?>>asList(fk22ef33a549aeb0, fk22ef33eedeba64, fk22ef33bb4e150b);
    }

    public List<ForeignKey<?>> getInverseForeignKeys() {
        return Arrays.<ForeignKey<?>>asList(_fk3e07a1891bee4d44, _fkc2c9ebee9e7e0323, _fk22ef33a549aeb0, _fk49cc129a549aeb0);
    }
    
    @Override
    public List<Expr<?>> getColumns() {
        return Arrays.<Expr<?>>asList(all());
    }

}

