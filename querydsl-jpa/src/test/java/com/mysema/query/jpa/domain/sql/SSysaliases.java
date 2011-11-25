package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BooleanPath;
import com.mysema.query.types.path.SimplePath;
import com.mysema.query.types.path.StringPath;


/**
 * SSysaliases is a Querydsl query type for SSysaliases
 */
public class SSysaliases extends com.mysema.query.sql.RelationalPathBase<SSysaliases> {

    private static final long serialVersionUID = -1884315838;

    public static final SSysaliases sysaliases = new SSysaliases("SYSALIASES");

    public final StringPath alias = createString("ALIAS");

    public final StringPath aliasid = createString("ALIASID");

    public final SimplePath<Object> aliasinfo = createSimple("ALIASINFO", Object.class);

    public final StringPath aliastype = createString("ALIASTYPE");

    public final StringPath javaclassname = createString("JAVACLASSNAME");

    public final StringPath namespace = createString("NAMESPACE");

    public final StringPath schemaid = createString("SCHEMAID");

    public final StringPath specificname = createString("SPECIFICNAME");

    public final BooleanPath systemalias = createBoolean("SYSTEMALIAS");

    public SSysaliases(String variable) {
        super(SSysaliases.class, forVariable(variable), "SYS", "SYSALIASES");
    }

    public SSysaliases(Path<? extends SSysaliases> entity) {
        super(entity.getType(), entity.getMetadata(), "SYS", "SYSALIASES");
    }

    public SSysaliases(PathMetadata<?> metadata) {
        super(SSysaliases.class, metadata, "SYS", "SYSALIASES");
    }

}

