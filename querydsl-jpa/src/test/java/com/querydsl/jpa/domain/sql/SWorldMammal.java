package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SWorldMammal is a Querydsl querydsl type for SWorldMammal
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SWorldMammal extends com.querydsl.sql.RelationalPathBase<SWorldMammal> {

    private static final long serialVersionUID = 979697152;

    public static final SWorldMammal WorldMammal = new SWorldMammal("World_Mammal");

    public final NumberPath<Long> mammalsId = createNumber("mammalsId", Long.class);

    public final NumberPath<Long> worldId = createNumber("worldId", Long.class);

    public final com.querydsl.sql.PrimaryKey<SWorldMammal> primary = createPrimaryKey(worldId, mammalsId);

    public final com.querydsl.sql.ForeignKey<SMammal> fk4070aeece01c8ee7 = createForeignKey(mammalsId, "id");

    public final com.querydsl.sql.ForeignKey<SWorld> fk4070aeecd3538cf8 = createForeignKey(worldId, "id");

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

