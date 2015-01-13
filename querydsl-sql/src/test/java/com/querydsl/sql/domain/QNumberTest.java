package com.querydsl.sql.domain;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

import java.sql.Types;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.sql.spatial.RelationalPathSpatial;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.BooleanPath;
import com.querydsl.core.types.path.NumberPath;



/**
 * QNumberTest is a Querydsl querydsl type for QNumberTest
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QNumberTest extends RelationalPathSpatial<QNumberTest> {

    private static final long serialVersionUID = 291758928;

    public static final QNumberTest numberTest = new QNumberTest("NUMBER_TEST");

    public final BooleanPath col1Boolean = createBoolean("col1");

    public final NumberPath<Byte> col1Number = createNumber("col2", Byte.class);

    public QNumberTest(String variable) {
        super(QNumberTest.class, forVariable(variable), "PUBLIC", "NUMBER_TEST");
        addMetadata();
    }

    public QNumberTest(String variable, String schema, String table) {
        super(QNumberTest.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QNumberTest(Path<? extends QNumberTest> path) {
        super(path.getType(), path.getMetadata(), "PUBLIC", "NUMBER_TEST");
        addMetadata();
    }

    public QNumberTest(PathMetadata<?> metadata) {
        super(QNumberTest.class, metadata, "PUBLIC", "NUMBER_TEST");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(col1Boolean, ColumnMetadata.named("COL1").withIndex(1).ofType(Types.BIT));
        addMetadata(col1Number, ColumnMetadata.named("COL1").withIndex(1).ofType(Types.BIT));
    }

}
