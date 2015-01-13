package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.StringPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * STest is a Querydsl querydsl type for STest
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class STest extends com.querydsl.sql.RelationalPathBase<STest> {

    private static final long serialVersionUID = -1389036285;

    public static final STest test = new STest("TEST");

    public final StringPath name = createString("name");

    public STest(String variable) {
        super(STest.class, forVariable(variable), "", "TEST");
        addMetadata();
    }

    public STest(String variable, String schema, String table) {
        super(STest.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public STest(Path<? extends STest> path) {
        super(path.getType(), path.getMetadata(), "", "TEST");
        addMetadata();
    }

    public STest(PathMetadata<?> metadata) {
        super(STest.class, metadata, "", "TEST");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(name, ColumnMetadata.named("NAME").withIndex(1).ofType(12).withSize(255));
    }

}

