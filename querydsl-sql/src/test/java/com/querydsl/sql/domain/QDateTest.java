package com.querydsl.sql.domain;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.sql.RelationalPathBase;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.path.DatePath;

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
