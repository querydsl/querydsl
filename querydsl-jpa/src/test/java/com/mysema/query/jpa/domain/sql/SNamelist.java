package com.mysema.query.jpa.domain.sql;

import javax.annotation.Generated;

import com.mysema.query.sql.ColumnMetadata;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.NumberPath;
import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * SNamelist is a Querydsl query type for SNamelist
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SNamelist extends com.mysema.query.sql.RelationalPathBase<SNamelist> {

    private static final long serialVersionUID = -930763259;

    public static final SNamelist namelist_ = new SNamelist("namelist_");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SNamelist> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SNameListNames> _fkd6c82d7217b6c3fc = createInvForeignKey(id, "NameList_id");

    public SNamelist(String variable) {
        super(SNamelist.class, forVariable(variable), "", "namelist_");
        addMetadata();
    }

    public SNamelist(String variable, String schema, String table) {
        super(SNamelist.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SNamelist(Path<? extends SNamelist> path) {
        super(path.getType(), path.getMetadata(), "", "namelist_");
        addMetadata();
    }

    public SNamelist(PathMetadata<?> metadata) {
        super(SNamelist.class, metadata, "", "namelist_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(-5).withSize(19).notNull());
    }

}

