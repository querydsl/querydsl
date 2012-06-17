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
package com.mysema.query.sql;

import com.mysema.query.types.Ops;

/**
 * @author tiwe
 *
 */
public class SQLiteTemplates extends SQLTemplates {
    
    public SQLiteTemplates() {
        this('\\', false);
    }
    
    public SQLiteTemplates(boolean quote) {
        this('\\', quote);
    }

    public SQLiteTemplates(char escape, boolean quote) {
        super("\"", escape, quote);
        setBigDecimalSupported(false);
        add(Ops.MOD, "{0} % {1}");
        
        add(Ops.INDEX_OF, "charindex({1},{0},1)-1");
        add(Ops.INDEX_OF_2ARGS, "charindex({1},{0},{2s}+1)-1");
                
        // TODO : optimize
        add(Ops.DateTimeOps.YEAR, "cast(strftime('%Y',{0} / 1000, 'unixepoch', 'localtime') as integer)");
        add(Ops.DateTimeOps.YEAR_MONTH, "strftime('%Y',{0} / 1000, 'unixepoch', 'localtime') * 100 + strftime('%m',{0} / 1000, 'unixepoch', 'localtime')");
        add(Ops.DateTimeOps.MONTH, "cast(strftime('%m',{0} / 1000, 'unixepoch', 'localtime') as integer)");
        add(Ops.DateTimeOps.WEEK, "cast(strftime('%W',{0} / 1000, 'unixepoch', 'localtime') as integer) + 1");
        add(Ops.DateTimeOps.DAY_OF_MONTH, "cast(strftime('%d',{0} / 1000, 'unixepoch', 'localtime') as integer)");
        add(Ops.DateTimeOps.DAY_OF_WEEK, "cast(strftime('%w',{0} / 1000, 'unixepoch', 'localtime') as integer) + 1");
        add(Ops.DateTimeOps.DAY_OF_YEAR, "cast(strftime('%j',{0} / 1000, 'unixepoch', 'localtime') as integer)");
        add(Ops.DateTimeOps.HOUR, "cast(strftime('%H',{0} / 1000, 'unixepoch', 'localtime') as integer)");
        add(Ops.DateTimeOps.MINUTE, "cast(strftime('%M',{0} / 1000, 'unixepoch', 'localtime') as integer)");
        add(Ops.DateTimeOps.SECOND, "cast(strftime('%S',{0} / 1000, 'unixepoch', 'localtime') as integer)");
    }
    
}
