package com.mysema.query.jpa.domain.sql;

import javax.annotation.Generated;

import com.mysema.query.sql.ColumnMetadata;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.NumberPath;
import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * SHumanHairs is a Querydsl query type for SHumanHairs
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SHumanHairs extends com.mysema.query.sql.RelationalPathBase<SHumanHairs> {

    private static final long serialVersionUID = 1372028757;

    public static final SHumanHairs HumanHairs = new SHumanHairs("Human_hairs");

    public final NumberPath<Integer> hairs = createNumber("hairs", Integer.class);

    public final NumberPath<Long> humanId = createNumber("humanId", Long.class);

    public final com.mysema.query.sql.ForeignKey<SMammal> fk6649531ff097e318 = createForeignKey(humanId, "id");

    public SHumanHairs(String variable) {
        super(SHumanHairs.class, forVariable(variable), "", "Human_hairs");
        addMetadata();
    }

    public SHumanHairs(String variable, String schema, String table) {
        super(SHumanHairs.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SHumanHairs(Path<? extends SHumanHairs> path) {
        super(path.getType(), path.getMetadata(), "", "Human_hairs");
        addMetadata();
    }

    public SHumanHairs(PathMetadata<?> metadata) {
        super(SHumanHairs.class, metadata, "", "Human_hairs");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(hairs, ColumnMetadata.named("hairs").withIndex(2).ofType(4).withSize(10));
        addMetadata(humanId, ColumnMetadata.named("Human_id").withIndex(1).ofType(-5).withSize(19).notNull());
    }

}

