package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.forVariable;

import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.sql.Table;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;


/**
 * SInheritedproperties is a Querydsl query type for SInheritedproperties
 */
@Table("INHERITEDPROPERTIES")
public class SInheritedproperties extends RelationalPathBase<SInheritedproperties> {

    private static final long serialVersionUID = -2093604956;

    public static final SInheritedproperties inheritedproperties = new SInheritedproperties("INHERITEDPROPERTIES");

    public final StringPath classproperty = createString("CLASSPROPERTY");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final StringPath stringassimple = createString("STRINGASSIMPLE");

    public final StringPath superclassproperty = createString("SUPERCLASSPROPERTY");

    public final PrimaryKey<SInheritedproperties> sql100819184434080 = createPrimaryKey(id);

    public SInheritedproperties(String variable) {
        super(SInheritedproperties.class, forVariable(variable));
    }

    public SInheritedproperties(BeanPath<? extends SInheritedproperties> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SInheritedproperties(PathMetadata<?> metadata) {
        super(SInheritedproperties.class, metadata);
    }

}

