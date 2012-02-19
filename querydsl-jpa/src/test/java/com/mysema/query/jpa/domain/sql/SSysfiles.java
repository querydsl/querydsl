package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SSysfiles is a Querydsl query type for SSysfiles
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SSysfiles extends com.mysema.query.sql.RelationalPathBase<SSysfiles> {

    private static final long serialVersionUID = -1709159493;

    public static final SSysfiles sysfiles = new SSysfiles("SYSFILES");

    public final StringPath fileid = createString("FILEID");

    public final StringPath filename = createString("FILENAME");

    public final NumberPath<Long> generationid = createNumber("GENERATIONID", Long.class);

    public final StringPath schemaid = createString("SCHEMAID");

    public SSysfiles(String variable) {
        super(SSysfiles.class, forVariable(variable), "SYS", "SYSFILES");
    }

    public SSysfiles(Path<? extends SSysfiles> entity) {
        super(entity.getType(), entity.getMetadata(), "SYS", "SYSFILES");
    }

    public SSysfiles(PathMetadata<?> metadata) {
        super(SSysfiles.class, metadata, "SYS", "SYSFILES");
    }

}

