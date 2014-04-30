package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;

import java.io.*;

import java.io.File;


/**
 * SSurvey is a Querydsl query type for SSurvey
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SSurvey extends com.mysema.query.sql.RelationalPathBase<SSurvey> {

    private static final long serialVersionUID = 857081739;

    public static final SSurvey survey = new SSurvey("SURVEY");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public final StringPath name2 = createString("name2");

    public final com.mysema.query.sql.PrimaryKey<SSurvey> primary = createPrimaryKey(id);

    public SSurvey(String variable) {
        super(SSurvey.class, forVariable(variable), "null", "SURVEY");
        addMetadata();
    }

    public SSurvey(String variable, String schema, String table) {
        super(SSurvey.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SSurvey(Path<? extends SSurvey> path) {
        super(path.getType(), path.getMetadata(), "null", "SURVEY");
        addMetadata();
    }

    public SSurvey(PathMetadata<?> metadata) {
        super(SSurvey.class, metadata, "null", "SURVEY");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(id, ColumnMetadata.named("ID").withIndex(1).ofType(4).withSize(10).notNull());
        addMetadata(name, ColumnMetadata.named("NAME").withIndex(2).ofType(12).withSize(30));
        addMetadata(name2, ColumnMetadata.named("NAME2").withIndex(3).ofType(12).withSize(30));
    }

}

