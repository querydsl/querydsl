/*
 * Copyright 2012, Mysema Ltd
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


public class DataNucleusTemplates extends JPQLTemplates {

    public static final DataNucleusTemplates DEFAULT = new DataNucleusTemplates();

    public DataNucleusTemplates() {
        this(DEFAULT_ESCAPE);
    }

    public DataNucleusTemplates(char escape) {
        super(escape);
        add(Ops.LIKE, "{0} like {1}",1);
        add(Ops.MATCHES, "{0} like {1}", 27); // TODO : support real regexes
        add(Ops.MATCHES_IC, "{0} like {1}", 27); // TODO : support real regexes

        add(Ops.STRING_CONTAINS, "{0} like {%1%}");
        add(Ops.STRING_CONTAINS_IC, "{0l} like {%%1%%}");
        add(Ops.ENDS_WITH, "{0} like {%1}");
        add(Ops.ENDS_WITH_IC, "{0l} like {%%1}");
        add(Ops.STARTS_WITH, "{0} like {1%}");
        add(Ops.STARTS_WITH_IC, "{0l} like {1%%}");


    }

}
