package com.mysema.query.jpa.domain.sql;

import javax.annotation.Generated;

import com.mysema.query.sql.ColumnMetadata;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.StringPath;
import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * STest is a Querydsl query type for STest
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class STest extends com.mysema.query.sql.RelationalPathBase<STest> {

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

