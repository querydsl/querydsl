package com.mysema.query.jpa.domain.sql;

import javax.annotation.Generated;

import com.mysema.query.sql.ColumnMetadata;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.DatePath;
import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * SDateTest is a Querydsl query type for SDateTest
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SDateTest extends com.mysema.query.sql.RelationalPathBase<SDateTest> {

    private static final long serialVersionUID = -1879688879;

    public static final SDateTest dateTest1 = new SDateTest("DATE_TEST");

    public final DatePath<java.sql.Date> dateTest = createDate("dateTest", java.sql.Date.class);

    public SDateTest(String variable) {
        super(SDateTest.class, forVariable(variable), "", "DATE_TEST");
        addMetadata();
    }

    public SDateTest(String variable, String schema, String table) {
        super(SDateTest.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SDateTest(Path<? extends SDateTest> path) {
        super(path.getType(), path.getMetadata(), "", "DATE_TEST");
        addMetadata();
    }

    public SDateTest(PathMetadata<?> metadata) {
        super(SDateTest.class, metadata, "", "DATE_TEST");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(dateTest, ColumnMetadata.named("DATE_TEST").withIndex(1).ofType(91).withSize(10));
    }

}

