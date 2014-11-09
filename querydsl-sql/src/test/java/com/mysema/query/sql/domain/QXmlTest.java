package com.mysema.query.sql.domain;

import javax.annotation.Generated;
import java.sql.Types;

import com.mysema.query.sql.ColumnMetadata;
import com.mysema.query.sql.spatial.RelationalPathSpatial;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.StringPath;
import static com.mysema.query.types.PathMetadataFactory.forVariable;



/**
 * QXmlTest is a Querydsl query type for QXmlTest
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QXmlTest extends RelationalPathSpatial<QXmlTest> {

    private static final long serialVersionUID = 574759316;

    public static final QXmlTest xmlTest = new QXmlTest("XML_TEST");

    public final StringPath col = createString("COL");

    public QXmlTest(String variable) {
        super(QXmlTest.class, forVariable(variable), "PUBLIC", "XML_TEST");
        addMetadata();
    }

    public QXmlTest(String variable, String schema, String table) {
        super(QXmlTest.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QXmlTest(Path<? extends QXmlTest> path) {
        super(path.getType(), path.getMetadata(), "PUBLIC", "XML_TEST");
        addMetadata();
    }

    public QXmlTest(PathMetadata<?> metadata) {
        super(QXmlTest.class, metadata, "PUBLIC", "XML_TEST");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(col, ColumnMetadata.named("COL").withIndex(1).ofType(Types.SQLXML).withSize(2147483647));
    }

}
