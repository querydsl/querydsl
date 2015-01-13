package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.StringPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SMammal is a Querydsl querydsl type for SMammal
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SMammal extends com.querydsl.sql.RelationalPathBase<SMammal> {

    private static final long serialVersionUID = 666678672;

    public static final SMammal Mammal = new SMammal("Mammal");

    public final StringPath dtype = createString("dtype");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.querydsl.sql.PrimaryKey<SMammal> primary = createPrimaryKey(id);

    public final com.querydsl.sql.ForeignKey<SHumanHairs> _fk6649531ff097e318 = createInvForeignKey(id, "Human_id");

    public final com.querydsl.sql.ForeignKey<SWorldMammal> _fk4070aeece01c8ee7 = createInvForeignKey(id, "mammals_id");

    public SMammal(String variable) {
        super(SMammal.class, forVariable(variable), "", "Mammal");
        addMetadata();
    }

    public SMammal(String variable, String schema, String table) {
        super(SMammal.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SMammal(Path<? extends SMammal> path) {
        super(path.getType(), path.getMetadata(), "", "Mammal");
        addMetadata();
    }

    public SMammal(PathMetadata<?> metadata) {
        super(SMammal.class, metadata, "", "Mammal");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(dtype, ColumnMetadata.named("DTYPE").withIndex(1).ofType(12).withSize(31).notNull());
        addMetadata(id, ColumnMetadata.named("id").withIndex(2).ofType(-5).withSize(19).notNull());
    }

}

