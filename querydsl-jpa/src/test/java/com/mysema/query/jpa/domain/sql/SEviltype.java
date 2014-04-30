package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;


/**
 * SEviltype is a Querydsl query type for SEviltype
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SEviltype extends com.mysema.query.sql.RelationalPathBase<SEviltype> {

    private static final long serialVersionUID = 1348954496;

    public static final SEviltype eviltype_ = new SEviltype("eviltype_");

    public final NumberPath<Integer> _asc = createNumber("_asc", Integer.class);

    public final NumberPath<Integer> _desc = createNumber("_desc", Integer.class);

    public final NumberPath<Integer> getClassId = createNumber("getClassId", Integer.class);

    public final NumberPath<Integer> getId = createNumber("getId", Integer.class);

    public final NumberPath<Integer> getMetadataId = createNumber("getMetadataId", Integer.class);

    public final NumberPath<Integer> getTypeId = createNumber("getTypeId", Integer.class);

    public final NumberPath<Integer> hashCodeId = createNumber("hashCodeId", Integer.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> isnotnullId = createNumber("isnotnullId", Integer.class);

    public final NumberPath<Integer> isnullId = createNumber("isnullId", Integer.class);

    public final NumberPath<Integer> notifyAllId = createNumber("notifyAllId", Integer.class);

    public final NumberPath<Integer> notifyId = createNumber("notifyId", Integer.class);

    public final NumberPath<Integer> toStringId = createNumber("toStringId", Integer.class);

    public final NumberPath<Integer> waitId = createNumber("waitId", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<SEviltype> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SEviltype> fkd21f83516787cd9e = createForeignKey(toStringId, "id");

    public final com.mysema.query.sql.ForeignKey<SEviltype> fkd21f835114c0ad20 = createForeignKey(_desc, "id");

    public final com.mysema.query.sql.ForeignKey<SEviltype> fkd21f835151e065d5 = createForeignKey(waitId, "id");

    public final com.mysema.query.sql.ForeignKey<SEviltype> fkd21f83517e62bab2 = createForeignKey(notifyAllId, "id");

    public final com.mysema.query.sql.ForeignKey<SEviltype> fkd21f8351c4df9054 = createForeignKey(getId, "id");

    public final com.mysema.query.sql.ForeignKey<SEviltype> fkd21f835112489019 = createForeignKey(isnullId, "id");

    public final com.mysema.query.sql.ForeignKey<SEviltype> fkd21f8351b09c8448 = createForeignKey(getClassId, "id");

    public final com.mysema.query.sql.ForeignKey<SEviltype> fkd21f835180b69f81 = createForeignKey(notifyId, "id");

    public final com.mysema.query.sql.ForeignKey<SEviltype> fkd21f8351b71279da = createForeignKey(getTypeId, "id");

    public final com.mysema.query.sql.ForeignKey<SEviltype> fkd21f8351226ee98f = createForeignKey(hashCodeId, "id");

    public final com.mysema.query.sql.ForeignKey<SEviltype> fkd21f8351f5ec12fa = createForeignKey(isnotnullId, "id");

    public final com.mysema.query.sql.ForeignKey<SEviltype> fkd21f8351f839f62 = createForeignKey(_asc, "id");

    public final com.mysema.query.sql.ForeignKey<SEviltype> fkd21f83512d7708c5 = createForeignKey(getMetadataId, "id");

    public final com.mysema.query.sql.ForeignKey<SEviltype> _fkd21f83516787cd9e = createInvForeignKey(id, "toString_id");

    public final com.mysema.query.sql.ForeignKey<SEviltype> _fkd21f835114c0ad20 = createInvForeignKey(id, "_desc");

    public final com.mysema.query.sql.ForeignKey<SEviltype> _fkd21f835151e065d5 = createInvForeignKey(id, "wait_id");

    public final com.mysema.query.sql.ForeignKey<SEviltype> _fkd21f83517e62bab2 = createInvForeignKey(id, "notifyAll_id");

    public final com.mysema.query.sql.ForeignKey<SEviltype> _fkd21f8351c4df9054 = createInvForeignKey(id, "get_id");

    public final com.mysema.query.sql.ForeignKey<SEviltype> _fkd21f835112489019 = createInvForeignKey(id, "isnull_id");

    public final com.mysema.query.sql.ForeignKey<SEviltype> _fkd21f8351b09c8448 = createInvForeignKey(id, "getClass_id");

    public final com.mysema.query.sql.ForeignKey<SEviltype> _fkd21f835180b69f81 = createInvForeignKey(id, "notify_id");

    public final com.mysema.query.sql.ForeignKey<SEviltype> _fkd21f8351b71279da = createInvForeignKey(id, "getType_id");

    public final com.mysema.query.sql.ForeignKey<SEviltype> _fkd21f8351226ee98f = createInvForeignKey(id, "hashCode_id");

    public final com.mysema.query.sql.ForeignKey<SEviltype> _fkd21f8351f5ec12fa = createInvForeignKey(id, "isnotnull_id");

    public final com.mysema.query.sql.ForeignKey<SEviltype> _fkd21f8351f839f62 = createInvForeignKey(id, "_asc");

    public final com.mysema.query.sql.ForeignKey<SEviltype> _fkd21f83512d7708c5 = createInvForeignKey(id, "getMetadata_id");

    public SEviltype(String variable) {
        super(SEviltype.class, forVariable(variable), "null", "eviltype_");
        addMetadata();
    }

    public SEviltype(String variable, String schema, String table) {
        super(SEviltype.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SEviltype(Path<? extends SEviltype> path) {
        super(path.getType(), path.getMetadata(), "null", "eviltype_");
        addMetadata();
    }

    public SEviltype(PathMetadata<?> metadata) {
        super(SEviltype.class, metadata, "null", "eviltype_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(_asc, ColumnMetadata.named("_asc").withIndex(2).ofType(4).withSize(10));
        addMetadata(_desc, ColumnMetadata.named("_desc").withIndex(3).ofType(4).withSize(10));
        addMetadata(getClassId, ColumnMetadata.named("getClass_id").withIndex(5).ofType(4).withSize(10));
        addMetadata(getId, ColumnMetadata.named("get_id").withIndex(4).ofType(4).withSize(10));
        addMetadata(getMetadataId, ColumnMetadata.named("getMetadata_id").withIndex(6).ofType(4).withSize(10));
        addMetadata(getTypeId, ColumnMetadata.named("getType_id").withIndex(7).ofType(4).withSize(10));
        addMetadata(hashCodeId, ColumnMetadata.named("hashCode_id").withIndex(8).ofType(4).withSize(10));
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(4).withSize(10).notNull());
        addMetadata(isnotnullId, ColumnMetadata.named("isnotnull_id").withIndex(9).ofType(4).withSize(10));
        addMetadata(isnullId, ColumnMetadata.named("isnull_id").withIndex(10).ofType(4).withSize(10));
        addMetadata(notifyAllId, ColumnMetadata.named("notifyAll_id").withIndex(12).ofType(4).withSize(10));
        addMetadata(notifyId, ColumnMetadata.named("notify_id").withIndex(11).ofType(4).withSize(10));
        addMetadata(toStringId, ColumnMetadata.named("toString_id").withIndex(13).ofType(4).withSize(10));
        addMetadata(waitId, ColumnMetadata.named("wait_id").withIndex(14).ofType(4).withSize(10));
    }

}

