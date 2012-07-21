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
package com.mysema.query.jpa;

import com.mysema.query.types.Ops;

/**
 * NOTE : this is under construction
 *
 * @author tiwe
 *
 */
public class EclipseLinkTemplates extends JPQLTemplates {

    public static final JPQLTemplates DEFAULT = new EclipseLinkTemplates();

    // TODO : indexed list access

    public EclipseLinkTemplates() {
        this(DEFAULT_ESCAPE);
    }
    
    public EclipseLinkTemplates(char escape) {
        super(escape);        
        add(Ops.STRING_CAST, "cast({0} as varchar)");
        
        // EclipseLink specific (works at least with Derby, HSQLDB and H2)
//        add(Ops.DateTimeOps.MILLISECOND, "0"); // NOT SUPPORTED
//        add(Ops.DateTimeOps.SECOND, "func('second',{0})");
//        add(Ops.DateTimeOps.MINUTE, "func('minute',{0})");
//        add(Ops.DateTimeOps.HOUR, "func('hour',{0})");
//        add(Ops.DateTimeOps.DAY_OF_WEEK, "func('dayofweek',{0})");
//        add(Ops.DateTimeOps.DAY_OF_MONTH, "func('day',{0})");
//        add(Ops.DateTimeOps.DAY_OF_YEAR, "func('dayofyear',{0})");
//        add(Ops.DateTimeOps.MONTH, "func('month',{0})");
//        add(Ops.DateTimeOps.WEEK, "func('week',{0})");
//        add(Ops.DateTimeOps.YEAR, "func('year',{0})");
//        add(Ops.DateTimeOps.YEAR_MONTH, "func('year',{0}) * 100 + func('month',{0})");
        
        add(Ops.CHAR_AT, "substring({0},{1}+1,1)");

    }
    
    @Override
    public boolean isSelect1Supported() {
        return true;
    }
    
}
