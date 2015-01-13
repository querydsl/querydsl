package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.StringPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SNameListNames is a Querydsl querydsl type for SNameListNames
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SNameListNames extends com.querydsl.sql.RelationalPathBase<SNameListNames> {

    private static final long serialVersionUID = 150303150;

    public static final SNameListNames NameListNames = new SNameListNames("NameList_names");

    public final NumberPath<Long> nameListId = createNumber("nameListId", Long.class);

    public final StringPath names = createString("names");

    public final com.querydsl.sql.ForeignKey<SNamelist> fkd6c82d7217b6c3fc = createForeignKey(nameListId, "id");

    public SNameListNames(String variable) {
        super(SNameListNames.class, forVariable(variable), "", "NameList_names");
        addMetadata();
    }

    public SNameListNames(String variable, String schema, String table) {
        super(SNameListNames.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SNameListNames(Path<? extends SNameListNames> path) {
        super(path.getType(), path.getMetadata(), "", "NameList_names");
        addMetadata();
    }

    public SNameListNames(PathMetadata<?> metadata) {
        super(SNameListNames.class, metadata, "", "NameList_names");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(nameListId, ColumnMetadata.named("NameList_id").withIndex(1).ofType(-5).withSize(19).notNull());
        addMetadata(names, ColumnMetadata.named("names").withIndex(2).ofType(12).withSize(255));
    }

}

