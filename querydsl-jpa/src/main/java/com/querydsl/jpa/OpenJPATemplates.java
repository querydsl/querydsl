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
package com.querydsl.jpa;

import com.querydsl.core.types.Ops;
import com.querydsl.core.types.PathType;

/**
 * OpenJPATemplates extends JPQLTemplates with OpenJPA specific extensions
 *
 * @author tiwe
 *
 */
public class OpenJPATemplates extends JPQLTemplates{

    public static final OpenJPATemplates DEFAULT = new OpenJPATemplates();

    public OpenJPATemplates() {
        this(DEFAULT_ESCAPE);
        add(PathType.VARIABLE, "{0s}_");
        add(Ops.ALIAS, "{0} {1}");
        add(Ops.NEGATE, "-1 * {0}", 7);
    }

    public OpenJPATemplates(char escape) {
        super(escape);
    }

}
