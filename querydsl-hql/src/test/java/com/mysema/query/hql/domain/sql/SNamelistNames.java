package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import com.mysema.query.sql.*;
import java.util.*;


/**
 * SNamelistNames is a Querydsl query type for SNamelistNames
 */
@Table("NAMELIST_NAMES")
public class SNamelistNames extends BeanPath<SNamelistNames> implements RelationalPath<SNamelistNames> {

    private static final long serialVersionUID = -1506785162;

    public static final SNamelistNames namelistNames = new SNamelistNames("NAMELIST_NAMES");

    public final PString element = createString("ELEMENT");

    public final PNumber<Long> namelistId = createNumber("NAMELIST_ID", Long.class);

    private Expr[] _all;

    public final ForeignKey<SNamelist> fkd6c82d72b8406ca4 = new ForeignKey<SNamelist>(this, namelistId, "ID");

    public SNamelistNames(String variable) {
        super(SNamelistNames.class, forVariable(variable));
    }

    public SNamelistNames(BeanPath<? extends SNamelistNames> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SNamelistNames(PathMetadata<?> metadata) {
        super(SNamelistNames.class, metadata);
    }

    public Expr[] all() {
        if (_all == null) {
            _all = new Expr[]{element, namelistId};
        }
        return _all;
    }

    public PrimaryKey<SNamelistNames> getPrimaryKey() {
        return null;
    }

    public List<ForeignKey<?>> getForeignKeys() {
        return Arrays.<ForeignKey<?>>asList(fkd6c82d72b8406ca4);
    }

    public List<ForeignKey<?>> getInverseForeignKeys() {
        return Collections.<ForeignKey<?>>emptyList();
    }
    
    @Override
    public List<Expr<?>> getColumns() {
        return Arrays.<Expr<?>>asList(all());
    }

}

