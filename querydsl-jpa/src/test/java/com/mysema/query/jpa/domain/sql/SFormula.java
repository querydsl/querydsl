package com.mysema.query.jpa.domain.sql;

import javax.annotation.Generated;

import com.mysema.query.sql.ColumnMetadata;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.NumberPath;
import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * SFormula is a Querydsl query type for SFormula
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SFormula extends com.mysema.query.sql.RelationalPathBase<SFormula> {

    private static final long serialVersionUID = 1097200554;

    public static final SFormula formula_ = new SFormula("formula_");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Long> parameterId = createNumber("parameterId", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SFormula> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SParameter> fk1c4adbb924189298 = createForeignKey(parameterId, "id");

    public SFormula(String variable) {
        super(SFormula.class, forVariable(variable), "", "formula_");
        addMetadata();
    }

    public SFormula(String variable, String schema, String table) {
        super(SFormula.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SFormula(Path<? extends SFormula> path) {
        super(path.getType(), path.getMetadata(), "", "formula_");
        addMetadata();
    }

    public SFormula(PathMetadata<?> metadata) {
        super(SFormula.class, metadata, "", "formula_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(4).withSize(10).notNull());
        addMetadata(parameterId, ColumnMetadata.named("parameter_id").withIndex(2).ofType(-5).withSize(19));
    }

}

