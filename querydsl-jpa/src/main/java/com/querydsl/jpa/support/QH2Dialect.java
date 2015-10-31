/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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
package com.querydsl.jpa.support;

import org.hibernate.dialect.H2Dialect;

import com.querydsl.sql.H2Templates;
import com.querydsl.sql.SQLTemplates;

/**
 * {@code QH2Dialect} extends {@code H2Dialect} with additional functions
 */
public class QH2Dialect extends H2Dialect {

    public QH2Dialect() {
        SQLTemplates templates = H2Templates.DEFAULT;
        getFunctions().putAll(DialectSupport.createFunctions(templates));
    }

}
