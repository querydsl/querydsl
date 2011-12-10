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
package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

import com.mysema.query.sql.ForeignKey;
import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.RelationalPath;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.NumberPath;


/**
 * SNationality is a Querydsl query type for SNationality
 */
public class SNationality extends RelationalPathBase<SNationality> implements RelationalPath<SNationality> {

    private static final long serialVersionUID = 1320834259;

    public static final SNationality nationality = new SNationality("NATIONALITY");

    public final NumberPath<Integer> calendarId = createNumber("CALENDAR_ID", Integer.class);

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final PrimaryKey<SNationality> sql100819184436080 = createPrimaryKey(id);

    public final ForeignKey<SCalendar> fk68f2659ca61b9464 = new ForeignKey<SCalendar>(this, calendarId, "ID");

    public final ForeignKey<SPerson> _fk8e488775e9d94490 = new ForeignKey<SPerson>(this, id, "NATIONALITY_ID");

    public SNationality(String variable) {
        super(SNationality.class, forVariable(variable), null, "NATIONALITY");
    }

    public SNationality(BeanPath<? extends SNationality> entity) {
        super(entity.getType(), entity.getMetadata(), null, "NATIONALITY");
    }

    public SNationality(PathMetadata<?> metadata) {
        super(SNationality.class, metadata, null, "NATIONALITY");
    }
    
}

