package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SFormula is a Querydsl querydsl type for SFormula
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SFormula extends com.querydsl.sql.RelationalPathBase<SFormula> {

    private static final long serialVersionUID = 1097200554;

    public static final SFormula formula_ = new SFormula("formula_");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Long> parameterId = createNumber("parameterId", Long.class);

    public final com.querydsl.sql.PrimaryKey<SFormula> primary = createPrimaryKey(id);

    public final com.querydsl.sql.ForeignKey<SParameter> fk1c4adbb924189298 = createForeignKey(parameterId, "id");

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

