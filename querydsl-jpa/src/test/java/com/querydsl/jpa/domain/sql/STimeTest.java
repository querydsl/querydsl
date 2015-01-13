package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.TimePath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * STimeTest is a Querydsl querydsl type for STimeTest
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class STimeTest extends com.querydsl.sql.RelationalPathBase<STimeTest> {

    private static final long serialVersionUID = -1454836496;

    public static final STimeTest timeTest1 = new STimeTest("TIME_TEST");

    public final TimePath<java.sql.Time> timeTest = createTime("timeTest", java.sql.Time.class);

    public STimeTest(String variable) {
        super(STimeTest.class, forVariable(variable), "", "TIME_TEST");
        addMetadata();
    }

    public STimeTest(String variable, String schema, String table) {
        super(STimeTest.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public STimeTest(Path<? extends STimeTest> path) {
        super(path.getType(), path.getMetadata(), "", "TIME_TEST");
        addMetadata();
    }

    public STimeTest(PathMetadata<?> metadata) {
        super(STimeTest.class, metadata, "", "TIME_TEST");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(timeTest, ColumnMetadata.named("TIME_TEST").withIndex(1).ofType(92).withSize(8));
    }

}

