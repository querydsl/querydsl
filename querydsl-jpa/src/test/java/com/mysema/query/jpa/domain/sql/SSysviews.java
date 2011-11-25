package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.StringPath;


/**
 * SSysviews is a Querydsl query type for SSysviews
 */
public class SSysviews extends com.mysema.query.sql.RelationalPathBase<SSysviews> {

    private static final long serialVersionUID = -1694389326;

    public static final SSysviews sysviews = new SSysviews("SYSVIEWS");

    public final StringPath checkoption = createString("CHECKOPTION");

    public final StringPath compilationschemaid = createString("COMPILATIONSCHEMAID");

    public final StringPath tableid = createString("TABLEID");

    public final StringPath viewdefinition = createString("VIEWDEFINITION");

    public SSysviews(String variable) {
        super(SSysviews.class, forVariable(variable), "SYS", "SYSVIEWS");
    }

    public SSysviews(Path<? extends SSysviews> entity) {
        super(entity.getType(), entity.getMetadata(), "SYS", "SYSVIEWS");
    }

    public SSysviews(PathMetadata<?> metadata) {
        super(SSysviews.class, metadata, "SYS", "SYSVIEWS");
    }

}

