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
package com.querydsl.sql.domain;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Expression;

public class QIdName extends ConstructorExpression<IdName> {

    private static final long serialVersionUID = 5770565824515003611L;

    public QIdName(Expression<java.lang.Integer> id, Expression<java.lang.String> name) {
        super(IdName.class, new Class[]{int.class, String.class}, id, name);
    }

}
