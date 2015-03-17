package com.querydsl.sql.domain;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

import java.sql.Types;
import java.util.UUID;

import javax.annotation.Generated;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.SimplePath;
import com.querydsl.sql.ColumnMetadata;
import com.querydsl.sql.RelationalPathBase;



/**
 * QUuids is a Querydsl query type for QUuids
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QUuids extends RelationalPathBase<QUuids> {

    private static final long serialVersionUID = -1780705501;

    public static final QUuids uuids = new QUuids("UUIDS");

    public final SimplePath<UUID> field = createSimple("field", UUID.class);

    public QUuids(String variable) {
        super(QUuids.class, forVariable(variable), "public", "UUIDS");
        addMetadata();
    }

    public QUuids(String variable, String schema, String table) {
        super(QUuids.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QUuids(Path<? extends QUuids> path) {
        super(path.getType(), path.getMetadata(), "public", "UUIDS");
        addMetadata();
    }

    public QUuids(PathMetadata metadata) {
        super(QUuids.class, metadata, "public", "UUIDS");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(field, ColumnMetadata.named("FIELD").withIndex(1).ofType(Types.OTHER).withSize(2147483647));
    }

}
