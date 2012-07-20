package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SInheritedproperties is a Querydsl query type for SInheritedproperties
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SInheritedproperties extends com.mysema.query.sql.RelationalPathBase<SInheritedproperties> {

    private static final long serialVersionUID = -1417492740;

    public static final SInheritedproperties inheritedproperties = new SInheritedproperties("inheritedproperties_");

    public final StringPath classproperty = createString("CLASSPROPERTY");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final StringPath stringassimple = createString("STRINGASSIMPLE");

    public final StringPath superclassproperty = createString("SUPERCLASSPROPERTY");

    public final com.mysema.query.sql.PrimaryKey<SInheritedproperties> primary = createPrimaryKey(id);

    public SInheritedproperties(String variable) {
        super(SInheritedproperties.class, forVariable(variable), "null", "inheritedproperties_");
    }

    public SInheritedproperties(Path<? extends SInheritedproperties> path) {
        super(path.getType(), path.getMetadata(), "null", "inheritedproperties_");
    }

    public SInheritedproperties(PathMetadata<?> metadata) {
        super(SInheritedproperties.class, metadata, "null", "inheritedproperties_");
    }

}

