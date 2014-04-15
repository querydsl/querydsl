package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;


/**
 * SNameListNames is a Querydsl query type for SNameListNames
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SNameListNames extends com.mysema.query.sql.RelationalPathBase<SNameListNames> {

    private static final long serialVersionUID = 150303150;

    public static final SNameListNames NameListNames = new SNameListNames("NameList_names");

    public final NumberPath<Long> nameListId = createNumber("nameListId", Long.class);

    public final StringPath names = createString("names");

    public final com.mysema.query.sql.ForeignKey<SNamelist> fkd6c82d7217b6c3fc = createForeignKey(nameListId, "id");

    public SNameListNames(String variable) {
        super(SNameListNames.class, forVariable(variable), "null", "NameList_names");
        addMetadata();
    }

    public SNameListNames(String variable, String schema, String table) {
        super(SNameListNames.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SNameListNames(Path<? extends SNameListNames> path) {
        super(path.getType(), path.getMetadata(), "null", "NameList_names");
        addMetadata();
    }

    public SNameListNames(PathMetadata<?> metadata) {
        super(SNameListNames.class, metadata, "null", "NameList_names");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(nameListId, ColumnMetadata.named("NameList_id").withIndex(1).ofType(-5).withSize(19).notNull());
        addMetadata(names, ColumnMetadata.named("names").withIndex(2).ofType(12).withSize(255));
    }

}

