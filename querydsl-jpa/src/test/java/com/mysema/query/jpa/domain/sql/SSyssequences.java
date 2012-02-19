package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SSyssequences is a Querydsl query type for SSyssequences
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SSyssequences extends com.mysema.query.sql.RelationalPathBase<SSyssequences> {

    private static final long serialVersionUID = -1065496554;

    public static final SSyssequences syssequences = new SSyssequences("SYSSEQUENCES");

    public final NumberPath<Long> currentvalue = createNumber("CURRENTVALUE", Long.class);

    public final StringPath cycleoption = createString("CYCLEOPTION");

    public final NumberPath<Long> increment = createNumber("INCREMENT", Long.class);

    public final NumberPath<Long> maximumvalue = createNumber("MAXIMUMVALUE", Long.class);

    public final NumberPath<Long> minimumvalue = createNumber("MINIMUMVALUE", Long.class);

    public final StringPath schemaid = createString("SCHEMAID");

    public final SimplePath<Object> sequencedatatype = createSimple("SEQUENCEDATATYPE", Object.class);

    public final StringPath sequenceid = createString("SEQUENCEID");

    public final StringPath sequencename = createString("SEQUENCENAME");

    public final NumberPath<Long> startvalue = createNumber("STARTVALUE", Long.class);

    public SSyssequences(String variable) {
        super(SSyssequences.class, forVariable(variable), "SYS", "SYSSEQUENCES");
    }

    public SSyssequences(Path<? extends SSyssequences> entity) {
        super(entity.getType(), entity.getMetadata(), "SYS", "SYSSEQUENCES");
    }

    public SSyssequences(PathMetadata<?> metadata) {
        super(SSyssequences.class, metadata, "SYS", "SYSSEQUENCES");
    }

}

