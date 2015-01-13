package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SShow is a Querydsl querydsl type for SShow
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SShow extends com.querydsl.sql.RelationalPathBase<SShow> {

    private static final long serialVersionUID = -111289679;

    public static final SShow show_ = new SShow("show_");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.querydsl.sql.PrimaryKey<SShow> primary = createPrimaryKey(id);

    public final com.querydsl.sql.ForeignKey<SShowActs> _fk5f6ee03ab40105c = createInvForeignKey(id, "Show_id");

    public SShow(String variable) {
        super(SShow.class, forVariable(variable), "", "show_");
        addMetadata();
    }

    public SShow(String variable, String schema, String table) {
        super(SShow.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SShow(Path<? extends SShow> path) {
        super(path.getType(), path.getMetadata(), "", "show_");
        addMetadata();
    }

    public SShow(PathMetadata<?> metadata) {
        super(SShow.class, metadata, "", "show_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(-5).withSize(19).notNull());
    }

}

