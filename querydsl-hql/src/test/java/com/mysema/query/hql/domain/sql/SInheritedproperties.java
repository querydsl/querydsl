/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.forVariable;

import com.mysema.query.types.Expr;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.custom.CSimple;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PString;

/**
 * SInheritedproperties is a Querydsl query type for SInheritedproperties
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="INHERITEDPROPERTIES")
public class SInheritedproperties extends EntityPathBase<SInheritedproperties> {

    public final PString classproperty = createString("CLASSPROPERTY");

    public final PNumber<Long> id = createNumber("ID", Long.class);

    public final PString stringassimple = createString("STRINGASSIMPLE");

    public final PString superclassproperty = createString("SUPERCLASSPROPERTY");

    public SInheritedproperties(String variable) {
        super(SInheritedproperties.class, forVariable(variable));
    }

    public SInheritedproperties(BeanPath<? extends SInheritedproperties> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SInheritedproperties(PathMetadata<?> metadata) {
        super(SInheritedproperties.class, metadata);
    }

    public Expr<Object[]> all() {
        return CSimple.create(Object[].class, "{0}.*", this);
    }

}

