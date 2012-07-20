package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

import javax.annotation.Generated;

import org.junit.Ignore;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.TimePath;


/**
 * STimeTest is a Querydsl query type for STimeTest
 */
@Ignore
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class STimeTest extends com.mysema.query.sql.RelationalPathBase<STimeTest> {

    private static final long serialVersionUID = -1454836496;

    public static final STimeTest timeTest1 = new STimeTest("TIME_TEST");

    public final TimePath<java.sql.Time> timeTest = createTime("TIME_TEST", java.sql.Time.class);

    public STimeTest(String variable) {
        super(STimeTest.class, forVariable(variable), "null", "TIME_TEST");
    }

    public STimeTest(Path<? extends STimeTest> path) {
        super(path.getType(), path.getMetadata(), "null", "TIME_TEST");
    }

    public STimeTest(PathMetadata<?> metadata) {
        super(STimeTest.class, metadata, "null", "TIME_TEST");
    }

}

