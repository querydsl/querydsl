package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.StringPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SLocation is a Querydsl querydsl type for SLocation
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SLocation extends com.querydsl.sql.RelationalPathBase<SLocation> {

    private static final long serialVersionUID = 921451897;

    public static final SLocation location_ = new SLocation("location_");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final com.querydsl.sql.PrimaryKey<SLocation> primary = createPrimaryKey(id);

    public final com.querydsl.sql.ForeignKey<SStore> _fkcad4239e8a55845c = createInvForeignKey(id, "location_id");

    public SLocation(String variable) {
        super(SLocation.class, forVariable(variable), "", "location_");
        addMetadata();
    }

    public SLocation(String variable, String schema, String table) {
        super(SLocation.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SLocation(Path<? extends SLocation> path) {
        super(path.getType(), path.getMetadata(), "", "location_");
        addMetadata();
    }

    public SLocation(PathMetadata<?> metadata) {
        super(SLocation.class, metadata, "", "location_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(-5).withSize(19).notNull());
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(12).withSize(255));
    }

}

