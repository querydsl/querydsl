package com.mysema.query.mongodb.domain;

import com.mysema.query.mongodb.domain.User;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PathMetadataFactory;

public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -4872833626508344081L;

    public QUser(String var) {
        super(User.class, PathMetadataFactory.forVariable(var));
    }

    public final PString id = createString("id");
    public final PString firstName = createString("firstName");
    public final PString lastName = createString("lastName");

}
