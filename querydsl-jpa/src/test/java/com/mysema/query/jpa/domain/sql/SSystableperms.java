package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.StringPath;


/**
 * SSystableperms is a Querydsl query type for SSystableperms
 */
public class SSystableperms extends com.mysema.query.sql.RelationalPathBase<SSystableperms> {

    private static final long serialVersionUID = 4684209;

    public static final SSystableperms systableperms = new SSystableperms("SYSTABLEPERMS");

    public final StringPath deletepriv = createString("DELETEPRIV");

    public final StringPath grantee = createString("GRANTEE");

    public final StringPath grantor = createString("GRANTOR");

    public final StringPath insertpriv = createString("INSERTPRIV");

    public final StringPath referencespriv = createString("REFERENCESPRIV");

    public final StringPath selectpriv = createString("SELECTPRIV");

    public final StringPath tableid = createString("TABLEID");

    public final StringPath tablepermsid = createString("TABLEPERMSID");

    public final StringPath triggerpriv = createString("TRIGGERPRIV");

    public final StringPath updatepriv = createString("UPDATEPRIV");

    public SSystableperms(String variable) {
        super(SSystableperms.class, forVariable(variable), "SYS", "SYSTABLEPERMS");
    }

    public SSystableperms(Path<? extends SSystableperms> entity) {
        super(entity.getType(), entity.getMetadata(), "SYS", "SYSTABLEPERMS");
    }

    public SSystableperms(PathMetadata<?> metadata) {
        super(SSystableperms.class, metadata, "SYS", "SYSTABLEPERMS");
    }

}

