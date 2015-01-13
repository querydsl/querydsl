package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SParameter is a Querydsl querydsl type for SParameter
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SParameter extends com.querydsl.sql.RelationalPathBase<SParameter> {

    private static final long serialVersionUID = 1712103815;

    public static final SParameter parameter_ = new SParameter("parameter_");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.querydsl.sql.PrimaryKey<SParameter> primary = createPrimaryKey(id);

    public final com.querydsl.sql.ForeignKey<SFormula> _fk1c4adbb924189298 = createInvForeignKey(id, "parameter_id");

    public SParameter(String variable) {
        super(SParameter.class, forVariable(variable), "", "parameter_");
        addMetadata();
    }

    public SParameter(String variable, String schema, String table) {
        super(SParameter.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SParameter(Path<? extends SParameter> path) {
        super(path.getType(), path.getMetadata(), "", "parameter_");
        addMetadata();
    }

    public SParameter(PathMetadata<?> metadata) {
        super(SParameter.class, metadata, "", "parameter_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(-5).withSize(19).notNull());
    }

}

