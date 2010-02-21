package com.mysema.query.hql.domain.sql;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * SInheritedproperties is a Querydsl query type for SInheritedproperties
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="INHERITEDPROPERTIES")
public class SInheritedproperties extends PEntity<SInheritedproperties> {

    public final PString classproperty = createString("CLASSPROPERTY");

    public final PNumber<Long> id = createNumber("ID", Long.class);

    public final PString stringassimple = createString("STRINGASSIMPLE");

    public final PString superclassproperty = createString("SUPERCLASSPROPERTY");

    public SInheritedproperties(String variable) {
        super(SInheritedproperties.class, forVariable(variable));
    }

    public SInheritedproperties(PEntity<? extends SInheritedproperties> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SInheritedproperties(PathMetadata<?> metadata) {
        super(SInheritedproperties.class, metadata);
    }

}

