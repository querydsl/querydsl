package com.mysema.query.jpa.domain.sql;

import javax.annotation.Generated;

import com.mysema.query.sql.ColumnMetadata;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.NumberPath;
import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * SParameter is a Querydsl query type for SParameter
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SParameter extends com.mysema.query.sql.RelationalPathBase<SParameter> {

    private static final long serialVersionUID = 1712103815;

    public static final SParameter parameter_ = new SParameter("parameter_");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SParameter> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SFormula> _fk1c4adbb924189298 = createInvForeignKey(id, "parameter_id");

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

