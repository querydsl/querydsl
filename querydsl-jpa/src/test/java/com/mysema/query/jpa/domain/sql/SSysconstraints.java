package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SSysconstraints is a Querydsl query type for SSysconstraints
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SSysconstraints extends com.mysema.query.sql.RelationalPathBase<SSysconstraints> {

    private static final long serialVersionUID = 1753494650;

    public static final SSysconstraints sysconstraints = new SSysconstraints("sysconstraints");

    public SSysconstraints(String variable) {
        super(SSysconstraints.class, forVariable(variable), "sys", "sysconstraints");
    }

    public SSysconstraints(Path<? extends SSysconstraints> path) {
        super(path.getType(), path.getMetadata(), "sys", "sysconstraints");
    }

    public SSysconstraints(PathMetadata<?> metadata) {
        super(SSysconstraints.class, metadata, "sys", "sysconstraints");
    }

}

