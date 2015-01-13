package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.DateTimePath;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.StringPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SUser2 is a Querydsl querydsl type for SUser2
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SUser2 extends com.querydsl.sql.RelationalPathBase<SUser2> {

    private static final long serialVersionUID = 912100265;

    public static final SUser2 user2_ = new SUser2("user2_");

    public final NumberPath<Double> createdBy = createNumber("createdBy", Double.class);

    public final DateTimePath<java.sql.Timestamp> creationDate = createDateTime("creationDate", java.sql.Timestamp.class);

    public final DateTimePath<java.sql.Timestamp> deleteDate = createDateTime("deleteDate", java.sql.Timestamp.class);

    public final NumberPath<Double> deletedBy = createNumber("deletedBy", Double.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.sql.Timestamp> modificationDate = createDateTime("modificationDate", java.sql.Timestamp.class);

    public final NumberPath<Double> modifiedBy = createNumber("modifiedBy", Double.class);

    public final StringPath userEmail = createString("userEmail");

    public final StringPath userFirstName = createString("userFirstName");

    public final StringPath userLastName = createString("userLastName");

    public final StringPath userName = createString("userName");

    public final StringPath userPassword = createString("userPassword");

    public final com.querydsl.sql.PrimaryKey<SUser2> primary = createPrimaryKey(id);

    public final com.querydsl.sql.ForeignKey<SUser2_userprop> _fk4611b46af21971a1 = createInvForeignKey(id, "user2__id");

    public SUser2(String variable) {
        super(SUser2.class, forVariable(variable), "", "user2_");
        addMetadata();
    }

    public SUser2(String variable, String schema, String table) {
        super(SUser2.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SUser2(Path<? extends SUser2> path) {
        super(path.getType(), path.getMetadata(), "", "user2_");
        addMetadata();
    }

    public SUser2(PathMetadata<?> metadata) {
        super(SUser2.class, metadata, "", "user2_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdBy, ColumnMetadata.named("createdBy").withIndex(2).ofType(8).withSize(22).notNull());
        addMetadata(creationDate, ColumnMetadata.named("creationDate").withIndex(3).ofType(93).withSize(19));
        addMetadata(deleteDate, ColumnMetadata.named("deleteDate").withIndex(4).ofType(93).withSize(19));
        addMetadata(deletedBy, ColumnMetadata.named("deletedBy").withIndex(5).ofType(8).withSize(22).notNull());
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(-5).withSize(19).notNull());
        addMetadata(modificationDate, ColumnMetadata.named("modificationDate").withIndex(6).ofType(93).withSize(19));
        addMetadata(modifiedBy, ColumnMetadata.named("modifiedBy").withIndex(7).ofType(8).withSize(22).notNull());
        addMetadata(userEmail, ColumnMetadata.named("userEmail").withIndex(8).ofType(12).withSize(255));
        addMetadata(userFirstName, ColumnMetadata.named("userFirstName").withIndex(9).ofType(12).withSize(255));
        addMetadata(userLastName, ColumnMetadata.named("userLastName").withIndex(10).ofType(12).withSize(255));
        addMetadata(userName, ColumnMetadata.named("userName").withIndex(11).ofType(12).withSize(255));
        addMetadata(userPassword, ColumnMetadata.named("userPassword").withIndex(12).ofType(12).withSize(255));
    }

}

