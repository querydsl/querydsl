package com.mysema.query.sql.domain;

import javax.annotation.Generated;
import java.sql.Types;

import com.mysema.query.sql.ColumnMetadata;
import com.mysema.query.sql.spatial.RelationalPathSpatial;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BooleanPath;
import com.mysema.query.types.path.NumberPath;
import static com.mysema.query.types.PathMetadataFactory.forVariable;



/**
 * QNumberTest is a Querydsl query type for QNumberTest
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QNumberTest extends RelationalPathSpatial<QNumberTest> {

    private static final long serialVersionUID = 291758928;

    public static final QNumberTest numberTest = new QNumberTest("NUMBER_TEST");

    public final BooleanPath col1Boolean = createBoolean("col1");

    public final NumberPath col1Number = createNumber("col2", Byte.class);

    public QNumberTest(String variable) {
        super(QNumberTest.class, forVariable(variable), "null", "NUMBER_TEST");
        addMetadata();
    }

    public QNumberTest(String variable, String schema, String table) {
        super(QNumberTest.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QNumberTest(Path<? extends QNumberTest> path) {
        super(path.getType(), path.getMetadata(), "null", "NUMBER_TEST");
        addMetadata();
    }

    public QNumberTest(PathMetadata<?> metadata) {
        super(QNumberTest.class, metadata, "null", "NUMBER_TEST");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(col1Boolean, ColumnMetadata.named("col1").withIndex(1).ofType(Types.BIT));
        addMetadata(col1Number, ColumnMetadata.named("col1").withIndex(1).ofType(Types.BIT));
    }

}
