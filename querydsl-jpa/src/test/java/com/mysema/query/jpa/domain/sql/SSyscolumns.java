package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SSyscolumns is a Querydsl query type for SSyscolumns
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SSyscolumns extends com.mysema.query.sql.RelationalPathBase<SSyscolumns> {

    private static final long serialVersionUID = -20060127;

    public static final SSyscolumns syscolumns = new SSyscolumns("syscolumns");

    public SSyscolumns(String variable) {
        super(SSyscolumns.class, forVariable(variable), "sys", "syscolumns");
    }

    public SSyscolumns(Path<? extends SSyscolumns> path) {
        super(path.getType(), path.getMetadata(), "sys", "syscolumns");
    }

    public SSyscolumns(PathMetadata<?> metadata) {
        super(SSyscolumns.class, metadata, "sys", "syscolumns");
    }

}

