package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SHumanHairs is a Querydsl querydsl type for SHumanHairs
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SHumanHairs extends com.querydsl.sql.RelationalPathBase<SHumanHairs> {

    private static final long serialVersionUID = 1372028757;

    public static final SHumanHairs HumanHairs = new SHumanHairs("Human_hairs");

    public final NumberPath<Integer> hairs = createNumber("hairs", Integer.class);

    public final NumberPath<Long> humanId = createNumber("humanId", Long.class);

    public final com.querydsl.sql.ForeignKey<SMammal> fk6649531ff097e318 = createForeignKey(humanId, "id");

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

