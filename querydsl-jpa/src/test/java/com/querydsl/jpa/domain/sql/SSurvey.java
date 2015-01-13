package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.StringPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SSurvey is a Querydsl querydsl type for SSurvey
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SSurvey extends com.querydsl.sql.RelationalPathBase<SSurvey> {

    private static final long serialVersionUID = 857081739;

    public static final SSurvey survey = new SSurvey("SURVEY");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public final StringPath name2 = createString("name2");

    public final com.querydsl.sql.PrimaryKey<SSurvey> primary = createPrimaryKey(id);

    public SSurvey(String variable) {
        super(SSurvey.class, forVariable(variable), "", "SURVEY");
        addMetadata();
    }

    public SSurvey(String variable, String schema, String table) {
        super(SSurvey.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SSurvey(Path<? extends SSurvey> path) {
        super(path.getType(), path.getMetadata(), "", "SURVEY");
        addMetadata();
    }

    public SSurvey(PathMetadata<?> metadata) {
        super(SSurvey.class, metadata, "", "SURVEY");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(id, ColumnMetadata.named("ID").withIndex(1).ofType(4).withSize(10).notNull());
        addMetadata(name, ColumnMetadata.named("NAME").withIndex(2).ofType(12).withSize(30));
        addMetadata(name2, ColumnMetadata.named("NAME2").withIndex(3).ofType(12).withSize(30));
    }

}

