package com.mysema.query.jpa.domain.sql;

import javax.annotation.Generated;

import com.mysema.query.sql.ColumnMetadata;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;
import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * SMammal is a Querydsl query type for SMammal
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SMammal extends com.mysema.query.sql.RelationalPathBase<SMammal> {

    private static final long serialVersionUID = 666678672;

    public static final SMammal Mammal = new SMammal("Mammal");

    public final StringPath dtype = createString("dtype");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SMammal> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SHumanHairs> _fk6649531ff097e318 = createInvForeignKey(id, "Human_id");

    public final com.mysema.query.sql.ForeignKey<SWorldMammal> _fk4070aeece01c8ee7 = createInvForeignKey(id, "mammals_id");

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

