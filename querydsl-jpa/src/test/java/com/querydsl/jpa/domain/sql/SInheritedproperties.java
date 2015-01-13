package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.StringPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SInheritedproperties is a Querydsl querydsl type for SInheritedproperties
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SInheritedproperties extends com.querydsl.sql.RelationalPathBase<SInheritedproperties> {

    private static final long serialVersionUID = -992601885;

    public static final SInheritedproperties inheritedproperties_ = new SInheritedproperties("inheritedproperties_");

    public final StringPath classProperty = createString("classProperty");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath stringAsSimple = createString("stringAsSimple");

    public final StringPath superclassProperty = createString("superclassProperty");

    public final com.querydsl.sql.PrimaryKey<SInheritedproperties> primary = createPrimaryKey(id);

    public SInheritedproperties(String variable) {
        super(SInheritedproperties.class, forVariable(variable), "", "inheritedproperties_");
        addMetadata();
    }

    public SInheritedproperties(String variable, String schema, String table) {
        super(SInheritedproperties.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SInheritedproperties(Path<? extends SInheritedproperties> path) {
        super(path.getType(), path.getMetadata(), "", "inheritedproperties_");
        addMetadata();
    }

    public SInheritedproperties(PathMetadata<?> metadata) {
        super(SInheritedproperties.class, metadata, "", "inheritedproperties_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(classProperty, ColumnMetadata.named("classProperty").withIndex(4).ofType(12).withSize(255));
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(-5).withSize(19).notNull());
        addMetadata(stringAsSimple, ColumnMetadata.named("stringAsSimple").withIndex(2).ofType(12).withSize(255));
        addMetadata(superclassProperty, ColumnMetadata.named("superclassProperty").withIndex(3).ofType(12).withSize(255));
    }

}

