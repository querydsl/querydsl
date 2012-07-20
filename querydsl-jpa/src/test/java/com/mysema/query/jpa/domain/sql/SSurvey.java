package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SSurvey is a Querydsl query type for SSurvey
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SSurvey extends com.mysema.query.sql.RelationalPathBase<SSurvey> {

    private static final long serialVersionUID = 857081739;

    public static final SSurvey survey = new SSurvey("SURVEY");

    public final NumberPath<Integer> id = createNumber("ID", Integer.class);

    public final StringPath name = createString("NAME");

    public final StringPath name2 = createString("NAME2");

    public final com.mysema.query.sql.PrimaryKey<SSurvey> primary = createPrimaryKey(id);

    public SSurvey(String variable) {
        super(SSurvey.class, forVariable(variable), "null", "SURVEY");
    }

    public SSurvey(Path<? extends SSurvey> path) {
        super(path.getType(), path.getMetadata(), "null", "SURVEY");
    }

    public SSurvey(PathMetadata<?> metadata) {
        super(SSurvey.class, metadata, "null", "SURVEY");
    }

}

