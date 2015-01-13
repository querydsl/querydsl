/*
 * Copyright 2013, Mysema Ltd
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
package com.querydsl.jpa;

import com.querydsl.core.Detachable;
import com.querydsl.core.types.CollectionExpression;
import com.querydsl.core.types.Path;

/**
 * @author tiwe
 *
 */
public interface JPQLSubQuery extends Detachable, JPACommonQuery<JPQLSubQuery>{

    <P> JPQLSubQuery from(CollectionExpression<?,P> target, Path<P> alias);

}
