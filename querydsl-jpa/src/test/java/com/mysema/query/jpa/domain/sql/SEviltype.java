package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SEviltype is a Querydsl query type for SEviltype
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SEviltype extends com.mysema.query.sql.RelationalPathBase<SEviltype> {

    private static final long serialVersionUID = 1013345983;

    public static final SEviltype eviltype = new SEviltype("eviltype_");

    public final NumberPath<Integer> _asc = createNumber("_asc", Integer.class);

    public final NumberPath<Integer> _desc = createNumber("_desc", Integer.class);

    public final NumberPath<Integer> getId = createNumber("GET_ID", Integer.class);

    public final NumberPath<Integer> getclassId = createNumber("GETCLASS_ID", Integer.class);

    public final NumberPath<Integer> getmetadataId = createNumber("GETMETADATA_ID", Integer.class);

    public final NumberPath<Integer> gettypeId = createNumber("GETTYPE_ID", Integer.class);

    public final NumberPath<Integer> hashcodeId = createNumber("HASHCODE_ID", Integer.class);

    public final NumberPath<Integer> id = createNumber("ID", Integer.class);

    public final NumberPath<Integer> isnotnullId = createNumber("ISNOTNULL_ID", Integer.class);

    public final NumberPath<Integer> isnullId = createNumber("ISNULL_ID", Integer.class);

    public final NumberPath<Integer> notifyId = createNumber("NOTIFY_ID", Integer.class);

    public final NumberPath<Integer> notifyallId = createNumber("NOTIFYALL_ID", Integer.class);

    public final NumberPath<Integer> tostringId = createNumber("TOSTRING_ID", Integer.class);

    public final NumberPath<Integer> waitId = createNumber("WAIT_ID", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<SEviltype> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SEviltype> eviltype_DescFK = createForeignKey(_desc, "ID");

    public final com.mysema.query.sql.ForeignKey<SEviltype> eviltype_GETTYPEIDFK = createForeignKey(gettypeId, "ID");

    public final com.mysema.query.sql.ForeignKey<SEviltype> eviltype_GETCLASSIDFK = createForeignKey(getclassId, "ID");

    public final com.mysema.query.sql.ForeignKey<SEviltype> eviltype_ISNULLIDFK = createForeignKey(isnullId, "ID");

    public final com.mysema.query.sql.ForeignKey<SEviltype> eviltype_TOSTRINGIDFK = createForeignKey(tostringId, "ID");

    public final com.mysema.query.sql.ForeignKey<SEviltype> eviltype_AscFK = createForeignKey(_asc, "ID");

    public final com.mysema.query.sql.ForeignKey<SEviltype> eviltype_WAITIDFK = createForeignKey(waitId, "ID");

    public final com.mysema.query.sql.ForeignKey<SEviltype> eviltype_GETIDFK = createForeignKey(getId, "ID");

    public final com.mysema.query.sql.ForeignKey<SEviltype> eviltype_NOTIFYIDFK = createForeignKey(notifyId, "ID");

    public final com.mysema.query.sql.ForeignKey<SEviltype> eviltype_GETMETADATAIDFK = createForeignKey(getmetadataId, "ID");

    public final com.mysema.query.sql.ForeignKey<SEviltype> eviltype_HASHCODEIDFK = createForeignKey(hashcodeId, "ID");

    public final com.mysema.query.sql.ForeignKey<SEviltype> eviltype_NOTIFYALLIDFK = createForeignKey(notifyallId, "ID");

    public final com.mysema.query.sql.ForeignKey<SEviltype> eviltype_ISNOTNULLIDFK = createForeignKey(isnotnullId, "ID");

    public final com.mysema.query.sql.ForeignKey<SEviltype> _eviltype_DescFK = createInvForeignKey(id, "_desc");

    public final com.mysema.query.sql.ForeignKey<SEviltype> _eviltype_GETTYPEIDFK = createInvForeignKey(id, "GETTYPE_ID");

    public final com.mysema.query.sql.ForeignKey<SEviltype> _eviltype_GETCLASSIDFK = createInvForeignKey(id, "GETCLASS_ID");

    public final com.mysema.query.sql.ForeignKey<SEviltype> _eviltype_ISNULLIDFK = createInvForeignKey(id, "ISNULL_ID");

    public final com.mysema.query.sql.ForeignKey<SEviltype> _eviltype_TOSTRINGIDFK = createInvForeignKey(id, "TOSTRING_ID");

    public final com.mysema.query.sql.ForeignKey<SEviltype> _eviltype_AscFK = createInvForeignKey(id, "_asc");

    public final com.mysema.query.sql.ForeignKey<SEviltype> _eviltype_WAITIDFK = createInvForeignKey(id, "WAIT_ID");

    public final com.mysema.query.sql.ForeignKey<SEviltype> _eviltype_GETIDFK = createInvForeignKey(id, "GET_ID");

    public final com.mysema.query.sql.ForeignKey<SEviltype> _eviltype_NOTIFYIDFK = createInvForeignKey(id, "NOTIFY_ID");

    public final com.mysema.query.sql.ForeignKey<SEviltype> _eviltype_GETMETADATAIDFK = createInvForeignKey(id, "GETMETADATA_ID");

    public final com.mysema.query.sql.ForeignKey<SEviltype> _eviltype_HASHCODEIDFK = createInvForeignKey(id, "HASHCODE_ID");

    public final com.mysema.query.sql.ForeignKey<SEviltype> _eviltype_NOTIFYALLIDFK = createInvForeignKey(id, "NOTIFYALL_ID");

    public final com.mysema.query.sql.ForeignKey<SEviltype> _eviltype_ISNOTNULLIDFK = createInvForeignKey(id, "ISNOTNULL_ID");

    public SEviltype(String variable) {
        super(SEviltype.class, forVariable(variable), "null", "eviltype_");
    }

    public SEviltype(Path<? extends SEviltype> path) {
        super(path.getType(), path.getMetadata(), "null", "eviltype_");
    }

    public SEviltype(PathMetadata<?> metadata) {
        super(SEviltype.class, metadata, "null", "eviltype_");
    }

}

