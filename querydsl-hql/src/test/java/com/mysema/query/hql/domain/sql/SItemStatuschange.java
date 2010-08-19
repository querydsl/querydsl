package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import com.mysema.query.sql.*;
import java.util.*;


/**
 * SItemStatuschange is a Querydsl query type for SItemStatuschange
 */
@Table("ITEM_STATUSCHANGE")
public class SItemStatuschange extends BeanPath<SItemStatuschange> implements RelationalPath<SItemStatuschange> {

    private static final long serialVersionUID = 675000350;

    public static final SItemStatuschange itemStatuschange = new SItemStatuschange("ITEM_STATUSCHANGE");

    public final PNumber<Long> itemId = createNumber("ITEM_ID", Long.class);

    public final PNumber<Long> statuschangesId = createNumber("STATUSCHANGES_ID", Long.class);

    private Expr[] _all;

    public final ForeignKey<SItem> fkc2c9ebee9e7e0323 = new ForeignKey<SItem>(this, itemId, "ID");

    public final ForeignKey<SStatuschange> fkc2c9ebee2f721e35 = new ForeignKey<SStatuschange>(this, statuschangesId, "ID");

    public SItemStatuschange(String variable) {
        super(SItemStatuschange.class, forVariable(variable));
    }

    public SItemStatuschange(BeanPath<? extends SItemStatuschange> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SItemStatuschange(PathMetadata<?> metadata) {
        super(SItemStatuschange.class, metadata);
    }

    public Expr[] all() {
        if (_all == null) {
            _all = new Expr[]{itemId, statuschangesId};
        }
        return _all;
    }

    public PrimaryKey<SItemStatuschange> getPrimaryKey() {
        return null;
    }

    public List<ForeignKey<?>> getForeignKeys() {
        return Arrays.<ForeignKey<?>>asList(fkc2c9ebee9e7e0323, fkc2c9ebee2f721e35);
    }

    public List<ForeignKey<?>> getInverseForeignKeys() {
        return Collections.<ForeignKey<?>>emptyList();
    }

}

