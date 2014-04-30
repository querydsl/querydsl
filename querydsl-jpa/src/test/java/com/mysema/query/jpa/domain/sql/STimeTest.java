package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;

import java.io.*;

import java.io.File;


/**
 * STimeTest is a Querydsl query type for STimeTest
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class STimeTest extends com.mysema.query.sql.RelationalPathBase<STimeTest> {

    private static final long serialVersionUID = -1454836496;

    public static final STimeTest timeTest1 = new STimeTest("TIME_TEST");

    public final TimePath<java.sql.Time> timeTest = createTime("timeTest", java.sql.Time.class);

    public STimeTest(String variable) {
        super(STimeTest.class, forVariable(variable), "null", "TIME_TEST");
        addMetadata();
    }

    public STimeTest(String variable, String schema, String table) {
        super(STimeTest.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public STimeTest(Path<? extends STimeTest> path) {
        super(path.getType(), path.getMetadata(), "null", "TIME_TEST");
        addMetadata();
    }

    public STimeTest(PathMetadata<?> metadata) {
        super(STimeTest.class, metadata, "null", "TIME_TEST");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(timeTest, ColumnMetadata.named("TIME_TEST").withIndex(1).ofType(92).withSize(8));
    }

}

