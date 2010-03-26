/**
 * 
 */
package com.mysema.query.search;

import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PathMetadataFactory;

public class QUser extends PEntity<User> {

    private static final long serialVersionUID = 8362025864799201294L;

    public QUser(String variable) {
        super(User.class, PathMetadataFactory.forVariable(variable));
    }
    
    public final PString emailAddress = createString("emailAddress");
    
    public final PString firstName = createString("firstName");
    
    public final PString lastName = createString("lastName");
    
    public final PString middleName = createString("middleName");
    
}