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
package com.mysema.query.mongodb;

import com.mysema.query.types.Operator;
import com.mysema.query.types.OperatorImpl;

/**
 * Mongodb specific operators
 * 
 * @author tiwe
 *
 */
public final class MongodbOps {
    
    public static final Operator<Boolean> NEAR = new OperatorImpl<Boolean>("NEAR", Number.class, Number.class);
    
    public static final Operator<Boolean> ELEM_MATCH = new OperatorImpl<Boolean>("ELEM_MATCH", Object.class, Object.class);

    private MongodbOps(){}
    
}
