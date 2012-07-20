package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

import javax.annotation.Generated;

import org.junit.Ignore;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.StringPath;


/**
 * STest is a Querydsl query type for STest
 */
@Ignore
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

