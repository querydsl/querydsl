package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.*;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SSimpletypes is a Querydsl querydsl type for SSimpletypes
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SSimpletypes extends com.querydsl.sql.RelationalPathBase<SSimpletypes> {

    private static final long serialVersionUID = 1466962601;

    public static final SSimpletypes simpletypes_ = new SSimpletypes("simpletypes_");

    public final NumberPath<Byte> bbyte = createNumber("bbyte", Byte.class);

    public final NumberPath<Byte> bbyte2 = createNumber("bbyte2", Byte.class);

    public final NumberPath<java.math.BigDecimal> bigDecimal = createNumber("bigDecimal", java.math.BigDecimal.class);

    public final StringPath cchar = createString("cchar");

    public final StringPath cchar2 = createString("cchar2");

    public final DatePath<java.sql.Date> date = createDate("date", java.sql.Date.class);

    public final NumberPath<Double> ddouble = createNumber("ddouble", Double.class);

    public final NumberPath<Double> ddouble2 = createNumber("ddouble2", Double.class);

    public final NumberPath<Float> ffloat = createNumber("ffloat", Float.class);

    public final NumberPath<Float> ffloat2 = createNumber("ffloat2", Float.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> iint = createNumber("iint", Integer.class);

    public final NumberPath<Integer> iint2 = createNumber("iint2", Integer.class);

    public final StringPath llocale = createString("llocale");

    public final NumberPath<Long> llong = createNumber("llong", Long.class);

    public final NumberPath<Long> llong2 = createNumber("llong2", Long.class);

    public final StringPath sstring = createString("sstring");

    public final TimePath<java.sql.Time> time = createTime("time", java.sql.Time.class);

    public final DateTimePath<java.sql.Timestamp> timestamp = createDateTime("timestamp", java.sql.Timestamp.class);

    public final com.querydsl.sql.PrimaryKey<SSimpletypes> primary = createPrimaryKey(id);

    public SSimpletypes(String variable) {
        super(SSimpletypes.class, forVariable(variable), "", "simpletypes_");
        addMetadata();
    }

    public SSimpletypes(String variable, String schema, String table) {
        super(SSimpletypes.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SSimpletypes(Path<? extends SSimpletypes> path) {
        super(path.getType(), path.getMetadata(), "", "simpletypes_");
        addMetadata();
    }

    public SSimpletypes(PathMetadata<?> metadata) {
        super(SSimpletypes.class, metadata, "", "simpletypes_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(bbyte, ColumnMetadata.named("bbyte").withIndex(2).ofType(-6).withSize(3));
        addMetadata(bbyte2, ColumnMetadata.named("bbyte2").withIndex(3).ofType(-6).withSize(3).notNull());
        addMetadata(bigDecimal, ColumnMetadata.named("bigDecimal").withIndex(4).ofType(3).withSize(19).withDigits(2));
        addMetadata(cchar, ColumnMetadata.named("cchar").withIndex(5).ofType(1).withSize(1));
        addMetadata(cchar2, ColumnMetadata.named("cchar2").withIndex(6).ofType(1).withSize(1).notNull());
        addMetadata(date, ColumnMetadata.named("date").withIndex(7).ofType(91).withSize(10));
        addMetadata(ddouble, ColumnMetadata.named("ddouble").withIndex(8).ofType(8).withSize(22));
        addMetadata(ddouble2, ColumnMetadata.named("ddouble2").withIndex(9).ofType(8).withSize(22).notNull());
        addMetadata(ffloat, ColumnMetadata.named("ffloat").withIndex(10).ofType(7).withSize(12));
        addMetadata(ffloat2, ColumnMetadata.named("ffloat2").withIndex(11).ofType(7).withSize(12).notNull());
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(-5).withSize(19).notNull());
        addMetadata(iint, ColumnMetadata.named("iint").withIndex(12).ofType(4).withSize(10));
        addMetadata(iint2, ColumnMetadata.named("iint2").withIndex(13).ofType(4).withSize(10).notNull());
        addMetadata(llocale, ColumnMetadata.named("llocale").withIndex(14).ofType(12).withSize(255));
        addMetadata(llong, ColumnMetadata.named("llong").withIndex(15).ofType(-5).withSize(19));
        addMetadata(llong2, ColumnMetadata.named("llong2").withIndex(16).ofType(-5).withSize(19).notNull());
        addMetadata(sstring, ColumnMetadata.named("sstring").withIndex(17).ofType(12).withSize(255));
        addMetadata(time, ColumnMetadata.named("time").withIndex(18).ofType(92).withSize(8));
        addMetadata(timestamp, ColumnMetadata.named("timestamp").withIndex(19).ofType(93).withSize(19));
    }

}

