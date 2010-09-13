package com.mysema.query.util;

import com.mysema.query.annotations.QueryDelegate;
import com.mysema.query.domain.DelegateTest;
import com.mysema.query.domain.QDelegateTest_User;
import com.mysema.query.types.path.StringPath;

public final class UserUtils {

    private UserUtils(){}

    @QueryDelegate(DelegateTest.User.class)
    public static StringPath getName(QDelegateTest_User user){
        return user.name;
    }

}
