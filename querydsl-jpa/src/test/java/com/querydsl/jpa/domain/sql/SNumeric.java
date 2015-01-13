package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SNumeric is a Querydsl querydsl type for SNumeric
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SNumeric extends com.querydsl.sql.RelationalPathBase<SNumeric> {

    private static final long serialVersionUID = -1260757277;

    public static final SNumeric numeric_ = new SNumeric("numeric_");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<java.math.BigDecimal> value = createNumber("value", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> value_ = createNumber("value_", java.math.BigDecimal.class);

    public final com.querydsl.sql.PrimaryKey<SNumeric> primary = createPrimaryKey(id);

    public SNumeric(String variable) {
        super(SNumeric.class, forVariable(variable), "", "numeric_");
        addMetadata();
    }

    public SNumeric(String variable, String schema, String table) {
        super(SNumeric.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SNumeric(Path<? extends SNumeric> path) {
        super(path.getType(), path.getMetadata(), "", "numeric_");
        addMetadata();
    }

    public SNumeric(PathMetadata<?> metadata) {
        super(SNumeric.class, metadata, "", "numeric_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(-5).withSize(19).notNull());
        addMetadata(value, ColumnMetadata.named("value").withIndex(2).ofType(3).withSize(19).withDigits(2));
        addMetadata(value_, ColumnMetadata.named("value_").withIndex(3).ofType(3).withSize(19).withDigits(2));
    }

}

