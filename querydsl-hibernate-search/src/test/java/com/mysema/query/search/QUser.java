/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.search;

import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.StringPath;
import com.mysema.query.types.path.PathMetadataFactory;

public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 8362025864799201294L;

    public QUser(String variable) {
        super(User.class, PathMetadataFactory.forVariable(variable));
    }

    public final StringPath emailAddress = createString("emailAddress");

    public final StringPath firstName = createString("firstName");

    public final StringPath lastName = createString("lastName");

    public final StringPath middleName = createString("middleName");

}
