package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;


/**
 * SLocation is a Querydsl query type for SLocation
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SLocation extends com.mysema.query.sql.RelationalPathBase<SLocation> {

    private static final long serialVersionUID = 921451897;

    public static final SLocation location_ = new SLocation("location_");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final com.mysema.query.sql.PrimaryKey<SLocation> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SStore> _fkcad4239e8a55845c = createInvForeignKey(id, "location_id");

    public SLocation(String variable) {
        super(SLocation.class, forVariable(variable), "null", "location_");
        addMetadata();
    }

    public SLocation(String variable, String schema, String table) {
        super(SLocation.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SLocation(Path<? extends SLocation> path) {
        super(path.getType(), path.getMetadata(), "null", "location_");
        addMetadata();
    }

    public SLocation(PathMetadata<?> metadata) {
        super(SLocation.class, metadata, "null", "location_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(-5).withSize(19).notNull());
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(12).withSize(255));
    }

}

