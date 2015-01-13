package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.DatePath;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.StringPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SPerson is a Querydsl querydsl type for SPerson
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SPerson extends com.querydsl.sql.RelationalPathBase<SPerson> {

    private static final long serialVersionUID = 1974039961;

    public static final SPerson person_ = new SPerson("person_");

    public final DatePath<java.sql.Date> birthDay = createDate("birthDay", java.sql.Date.class);

    public final NumberPath<Long> i = createNumber("i", Long.class);

    public final StringPath name = createString("name");

    public final NumberPath<Long> nationalityId = createNumber("nationalityId", Long.class);

    public final NumberPath<Long> pidId = createNumber("pidId", Long.class);

    public final com.querydsl.sql.PrimaryKey<SPerson> primary = createPrimaryKey(i);

    public final com.querydsl.sql.ForeignKey<SPersonid> fkd78fcfaad7999e61 = createForeignKey(pidId, "id");

    public final com.querydsl.sql.ForeignKey<SNationality> fkd78fcfaaf6578e38 = createForeignKey(nationalityId, "id");

    public final com.querydsl.sql.ForeignKey<SAccount> _fk809dbbd28cfac74 = createInvForeignKey(i, "owner_i");

    public SPerson(String variable) {
        super(SPerson.class, forVariable(variable), "", "person_");
        addMetadata();
    }

    public SPerson(String variable, String schema, String table) {
        super(SPerson.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SPerson(Path<? extends SPerson> path) {
        super(path.getType(), path.getMetadata(), "", "person_");
        addMetadata();
    }

    public SPerson(PathMetadata<?> metadata) {
        super(SPerson.class, metadata, "", "person_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(birthDay, ColumnMetadata.named("birthDay").withIndex(2).ofType(91).withSize(10));
        addMetadata(i, ColumnMetadata.named("i").withIndex(1).ofType(-5).withSize(19).notNull());
        addMetadata(name, ColumnMetadata.named("name").withIndex(3).ofType(12).withSize(255));
        addMetadata(nationalityId, ColumnMetadata.named("nationality_id").withIndex(4).ofType(-5).withSize(19));
        addMetadata(pidId, ColumnMetadata.named("pid_id").withIndex(5).ofType(-5).withSize(19));
    }

}

