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
package com.mysema.query;

import net.jcip.annotations.Immutable;

/**
 * JoinType defines the supported join types
 *
 * @author tiwe
 */
@Immutable
public enum JoinType {
    /**
     * cross join
     */
    DEFAULT,
    /**
     * inner join
     */
    INNERJOIN,
    /**
     * join
     */
    JOIN,
    /**
     * left join
     */
    LEFTJOIN,
    /**
     * right join
     */
    RIGHTJOIN,
    /**
     * full join
     */
    FULLJOIN;

}
