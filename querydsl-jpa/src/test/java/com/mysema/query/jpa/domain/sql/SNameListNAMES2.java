package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SNameListNAMES is a Querydsl query type for SNameListNAMES
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SNameListNAMES2 extends com.mysema.query.sql.RelationalPathBase<SNameListNAMES2> {

    private static final long serialVersionUID = 149318062;

    public static final SNameListNAMES2 NameListNAMES = new SNameListNAMES2("NameList_NAMES");

    public final NumberPath<Long> nameListID = createNumber("NameList_ID", Long.class);

    public final StringPath names = createString("NAMES");

    public final com.mysema.query.sql.ForeignKey<SNamelist> nameListNAMESNameListIDFK = createForeignKey(nameListID, "id");

    public SNameListNAMES2(String variable) {
        super(SNameListNAMES2.class, forVariable(variable), "null", "NameList_NAMES");
    }

    public SNameListNAMES2(Path<? extends SNameListNAMES2> path) {
        super(path.getType(), path.getMetadata(), "null", "NameList_NAMES");
    }

    public SNameListNAMES2(PathMetadata<?> metadata) {
        super(SNameListNAMES2.class, metadata, "null", "NameList_NAMES");
    }

}

