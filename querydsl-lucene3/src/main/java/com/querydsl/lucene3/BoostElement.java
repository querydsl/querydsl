/*
 * Copyright 2016, The Querydsl Team (http://www.querydsl.com/team)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.lucene3;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanOperation;

/**
 * {@code BoostElement} is an element that allows boosting a Query.
 *
 * @author Shredder121
 */
public class BoostElement extends BooleanOperation {

    private static final long serialVersionUID = -5663346955776953048L;

    public BoostElement(Expression<Boolean> predicate, float boostFactor) {
        super(LuceneOps.BOOST, predicate, ConstantImpl.create(boostFactor));
    }

}
