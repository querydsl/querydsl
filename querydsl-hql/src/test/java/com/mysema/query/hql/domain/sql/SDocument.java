package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import com.mysema.query.sql.*;
import java.util.*;


/**
 * SDocument is a Querydsl query type for SDocument
 */
@Table("DOCUMENT")
public class SDocument extends BeanPath<SDocument> implements RelationalPath<SDocument> {

    private static final long serialVersionUID = 1919248740;

    public static final SDocument document = new SDocument("DOCUMENT");

    public final PNumber<Integer> id = createNumber("ID", Integer.class);

    public final PString name = createString("NAME");

    public final PDate<java.sql.Date> validto = createDate("VALIDTO", java.sql.Date.class);

    private Expr[] _all;

    public final PrimaryKey<SDocument> sql100819184432950 = new PrimaryKey<SDocument>(this, id);

    public SDocument(String variable) {
        super(SDocument.class, forVariable(variable));
    }

    public SDocument(BeanPath<? extends SDocument> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SDocument(PathMetadata<?> metadata) {
        super(SDocument.class, metadata);
    }

    public Expr[] all() {
        if (_all == null) {
            _all = new Expr[]{id, name, validto};
        }
        return _all;
    }

    public PrimaryKey<SDocument> getPrimaryKey() {
        return sql100819184432950;
    }

    public List<ForeignKey<?>> getForeignKeys() {
        return Collections.<ForeignKey<?>>emptyList();
    }

    public List<ForeignKey<?>> getInverseForeignKeys() {
        return Collections.<ForeignKey<?>>emptyList();
    }

}

