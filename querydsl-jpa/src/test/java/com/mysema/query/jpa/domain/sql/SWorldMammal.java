package com.mysema.query.jpa.domain.sql;

import javax.annotation.Generated;

import com.mysema.query.sql.ColumnMetadata;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.NumberPath;
import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * SWorldMammal is a Querydsl query type for SWorldMammal
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SWorldMammal extends com.mysema.query.sql.RelationalPathBase<SWorldMammal> {

    private static final long serialVersionUID = 979697152;

    public static final SWorldMammal WorldMammal = new SWorldMammal("World_Mammal");

    public final NumberPath<Long> mammalsId = createNumber("mammalsId", Long.class);

    public final NumberPath<Long> worldId = createNumber("worldId", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SWorldMammal> primary = createPrimaryKey(worldId, mammalsId);

    public final com.mysema.query.sql.ForeignKey<SMammal> fk4070aeece01c8ee7 = createForeignKey(mammalsId, "id");

    public final com.mysema.query.sql.ForeignKey<SWorld> fk4070aeecd3538cf8 = createForeignKey(worldId, "id");

    public SWorldMammal(String variable) {
        super(SWorldMammal.class, forVariable(variable), "", "World_Mammal");
        addMetadata();
    }

    public SWorldMammal(String variable, String schema, String table) {
        super(SWorldMammal.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SWorldMammal(Path<? extends SWorldMammal> path) {
        super(path.getType(), path.getMetadata(), "", "World_Mammal");
        addMetadata();
    }

    public SWorldMammal(PathMetadata<?> metadata) {
        super(SWorldMammal.class, metadata, "", "World_Mammal");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(mammalsId, ColumnMetadata.named("mammals_id").withIndex(2).ofType(-5).withSize(19).notNull());
        addMetadata(worldId, ColumnMetadata.named("World_id").withIndex(1).ofType(-5).withSize(19).notNull());
    }

}

