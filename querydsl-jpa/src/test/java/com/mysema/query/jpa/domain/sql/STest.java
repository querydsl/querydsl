package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * STest is a Querydsl query type for STest
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class STest extends com.mysema.query.sql.RelationalPathBase<STest> {

    private static final long serialVersionUID = -1389036285;

    public static final STest test = new STest("TEST");

    public final StringPath name = createString("NAME");

    public STest(String variable) {
        super(STest.class, forVariable(variable), "null", "TEST");
    }

    public STest(Path<? extends STest> path) {
        super(path.getType(), path.getMetadata(), "null", "TEST");
    }

    public STest(PathMetadata<?> metadata) {
        super(STest.class, metadata, "null", "TEST");
    }

}

