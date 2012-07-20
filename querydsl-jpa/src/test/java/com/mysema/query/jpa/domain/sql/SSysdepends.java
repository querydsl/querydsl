package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SSysdepends is a Querydsl query type for SSysdepends
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SSysdepends extends com.mysema.query.sql.RelationalPathBase<SSysdepends> {

    private static final long serialVersionUID = 584370123;

    public static final SSysdepends sysdepends = new SSysdepends("sysdepends");

    public SSysdepends(String variable) {
        super(SSysdepends.class, forVariable(variable), "sys", "sysdepends");
    }

    public SSysdepends(Path<? extends SSysdepends> path) {
        super(path.getType(), path.getMetadata(), "sys", "sysdepends");
    }

    public SSysdepends(PathMetadata<?> metadata) {
        super(SSysdepends.class, metadata, "sys", "sysdepends");
    }

}

