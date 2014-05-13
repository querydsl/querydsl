package com.mysema.query.sql.domain;

import com.mysema.query.sql.ColumnMetadata;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.PathMetadataFactory;
import com.mysema.query.types.path.DatePath;

import java.sql.Date;

public class QDateTest extends RelationalPathBase<QDateTest> {

    private static final long serialVersionUID = 1394463749655231079L;

    public static final QDateTest qDateTest = new QDateTest("DATE_TEST");

    public final DatePath<Date> dateTest = createDate("dateTest", java.sql.Date.class);

    public QDateTest(String path) {
        super(QDateTest.class, PathMetadataFactory.forVariable(path), "PUBLIC", "DATE_TEST");
        addMetadata();
    }

    public QDateTest(PathMetadata<?> metadata) {
        super(QDateTest.class, metadata, "PUBLIC", "DATE_TEST");
        addMetadata();
    }

    protected void addMetadata() {
        addMetadata(dateTest, ColumnMetadata.named("DATE_TEST"));
    }

}
