package com.mysema.query.sql;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.mysema.query.types.Expr;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.PEnum;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PathMetadataFactory;

@Table("PERSON")
public class QPerson extends BeanPath<QPerson> implements RelationalPath<QPerson> {

    private static final long serialVersionUID = 475064746;

    public static final QPerson person = new QPerson("PERSON");

    public final PString firstname = createString("FIRSTNAME");

    public final PEnum<com.mysema.query.alias.AliasTest.Gender> gender = createEnum("GENDER", com.mysema.query.alias.AliasTest.Gender.class);

    public final PNumber<Integer> id = createNumber("ID", Integer.class);

    public final PString securedid = createString("SECUREDID");

    private Expr[] _all;

    public final PrimaryKey<QPerson> sysIdx118 = new PrimaryKey<QPerson>(this, id);

    public QPerson(String variable) {
        super(QPerson.class, PathMetadataFactory.forVariable(variable));
    }

    public QPerson(BeanPath<? extends QPerson> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QPerson(PathMetadata<?> metadata) {
        super(QPerson.class, metadata);
    }

    public Expr<?>[] all() {
        if (_all == null) {
            _all = new Expr[]{firstname, gender, id, securedid};
        }
        return _all;
    }

    @Override
    public List<Expr<?>> getColumns() {
        return Arrays.asList(all());
    }

    @Override
    public PrimaryKey<QPerson> getPrimaryKey() {
        return sysIdx118;
    }

    @Override
    public List<ForeignKey<?>> getForeignKeys() {
        return Collections.emptyList();
    }

    @Override
    public List<ForeignKey<?>> getInverseForeignKeys() {
        return Collections.emptyList();
    }

}