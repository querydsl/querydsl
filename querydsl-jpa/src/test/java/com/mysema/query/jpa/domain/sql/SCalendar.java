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
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.NumberPath;


/**
 * SCalendar is a Querydsl query type for SCalendar
 */
public class SCalendar extends RelationalPathBase<SCalendar> {

    private static final long serialVersionUID = 879203207;

    public static final SCalendar calendar = new SCalendar("CALENDAR");

    public final NumberPath<Integer> id = createNumber("ID", Integer.class);

    public final PrimaryKey<SCalendar> sql100819184430980 = createPrimaryKey(id);

    public final ForeignKey<SNationality> _fk68f2659ca61b9464 = new ForeignKey<SNationality>(this, id, "CALENDAR_ID");

    public final ForeignKey<SCalendarHolidays> _fk31ce1edca61b9464 = new ForeignKey<SCalendarHolidays>(this, id, "CALENDAR_ID");

    public SCalendar(String variable) {
        super(SCalendar.class, forVariable(variable), null, "CALENDAR");
    }

    public SCalendar(BeanPath<? extends SCalendar> entity) {
        super(entity.getType(), entity.getMetadata(), null, "CALENDAR");
    }

    public SCalendar(PathMetadata<?> metadata) {
        super(SCalendar.class, metadata, null, "CALENDAR");
    }

}

