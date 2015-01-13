/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.hibernate.search;

import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.path.EntityPathBase;
import com.querydsl.core.types.path.StringPath;

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
