package com.mysema.query.sql.domain;

import javax.annotation.Generated;
import java.sql.Types;
import java.util.UUID;

import com.mysema.query.sql.ColumnMetadata;
import com.mysema.query.sql.spatial.RelationalPathSpatial;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.SimplePath;
import static com.mysema.query.types.PathMetadataFactory.forVariable;



/**
 * QUuids is a Querydsl query type for QUuids
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QUuids extends RelationalPathSpatial<QUuids> {

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

    public QUuids(PathMetadata<?> metadata) {
        super(QUuids.class, metadata, "public", "UUIDS");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(field, ColumnMetadata.named("FIELD").withIndex(1).ofType(Types.OTHER).withSize(2147483647));
    }

}
