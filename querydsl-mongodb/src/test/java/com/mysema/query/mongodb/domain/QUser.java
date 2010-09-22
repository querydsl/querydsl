/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.mongodb.domain;

import java.util.Date;

import com.mysema.query.types.PathMetadataFactory;
import com.mysema.query.types.path.DatePath;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;

public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -4872833626508344081L;

    public QUser(String var) {
        super(User.class, PathMetadataFactory.forVariable(var));
    }

    public final StringPath id = createString("id");
    public final StringPath firstName = createString("firstName");
    public final StringPath lastName = createString("lastName");

    public final DatePath<Date> created = createDate("date", Date.class);
    public final NumberPath<Integer> age = createNumber("age", Integer.class);
}
