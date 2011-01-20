package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import com.mysema.query.sql.*;
import java.util.*;


/**
 * SInheritedproperties is a Querydsl query type for SInheritedproperties
 */
@Table("INHERITEDPROPERTIES")
public class SInheritedproperties extends BeanPath<SInheritedproperties> implements RelationalPath<SInheritedproperties> {

    private static final long serialVersionUID = -2093604956;

    public static final SInheritedproperties inheritedproperties = new SInheritedproperties("INHERITEDPROPERTIES");

    public final PString classproperty = createString("CLASSPROPERTY");

    public final PNumber<Long> id = createNumber("ID", Long.class);

    public final PString stringassimple = createString("STRINGASSIMPLE");

    public final PString superclassproperty = createString("SUPERCLASSPROPERTY");

    private Expr[] _all;

    public final PrimaryKey<SInheritedproperties> sql100819184434080 = new PrimaryKey<SInheritedproperties>(this, id);

    public SInheritedproperties(String variable) {
        super(SInheritedproperties.class, forVariable(variable));
    }

    public SInheritedproperties(BeanPath<? extends SInheritedproperties> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SInheritedproperties(PathMetadata<?> metadata) {
        super(SInheritedproperties.class, metadata);
    }

    public Expr[] all() {
        if (_all == null) {
            _all = new Expr[]{classproperty, id, stringassimple, superclassproperty};
        }
        return _all;
    }

    public PrimaryKey<SInheritedproperties> getPrimaryKey() {
        return sql100819184434080;
    }

    public List<ForeignKey<?>> getForeignKeys() {
        return Collections.<ForeignKey<?>>emptyList();
    }

    public List<ForeignKey<?>> getInverseForeignKeys() {
        return Collections.<ForeignKey<?>>emptyList();
    }

    @Override
    public List<Expr<?>> getColumns() {
        return Arrays.<Expr<?>>asList(all());
    }
}

