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
package com.mysema.query.sql.domain;

import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.PathMetadataFactory;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.StringPath;

//@Table("TEST")
public class QTest_ extends EntityPathBase<Object> {

    private static final long serialVersionUID = -8421112749591552595L;

    public final StringPath name = createString("NAME");

    public QTest_(String path) {
        super(Object.class, PathMetadataFactory.forVariable(path));
    }

    public QTest_(PathMetadata<?> metadata) {
        super(Object.class, metadata);
    }

}
