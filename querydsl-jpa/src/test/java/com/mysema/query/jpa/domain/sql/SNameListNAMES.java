package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SNameListNAMES is a Querydsl query type for SNameListNAMES
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SNameListNAMES extends com.mysema.query.sql.RelationalPathBase<SNameListNAMES> {

    private static final long serialVersionUID = 149318062;

    public static final SNameListNAMES NameListNAMES = new SNameListNAMES("NameList_NAMES");

    public final NumberPath<Long> nameListID = createNumber("NameList_ID", Long.class);

    public final StringPath names = createString("NAMES");

    public final com.mysema.query.sql.ForeignKey<SNamelist> nameListNAMESNameListIDFK = createForeignKey(nameListID, "id");

    public SNameListNAMES(String variable) {
        super(SNameListNAMES.class, forVariable(variable), "null", "NameList_NAMES");
    }

    public SNameListNAMES(Path<? extends SNameListNAMES> path) {
        super(path.getType(), path.getMetadata(), "null", "NameList_NAMES");
    }

    public SNameListNAMES(PathMetadata<?> metadata) {
        super(SNameListNAMES.class, metadata, "null", "NameList_NAMES");
    }

}

