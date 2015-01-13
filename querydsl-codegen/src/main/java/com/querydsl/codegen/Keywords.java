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
package com.querydsl.codegen;

import java.util.Collection;

import com.google.common.collect.ImmutableList;

/**
 * Keywords sets in capitalized form to be used in GenericExporter and the APT processors
 * 
 * @author tiwe
 *
 */
public final class Keywords {
    
    private Keywords() {}
    
    public static final Collection<String> JPA = ImmutableList.of(
            "ABS","ALL","AND","ANY","AS","ASC","AVG","BETWEEN",
            "BIT_LENGTH[51]","BOTH","BY","CASE","CHAR_LENGTH",
            "CHARACTER_LENGTH","CLASS",
            "COALESCE","CONCAT","COUNT","CURRENT_DATE","CURRENT_TIME",
            "CURRENT_TIMESTAMP",
            "DELETE","DESC","DISTINCT","ELSE","EMPTY","END","ENTRY",
            "ESCAPE","EXISTS","FALSE","FETCH",
            "FROM","GROUP","HAVING","IN","INDEX","INNER","IS","JOIN",
            "KEY","LEADING","LEFT","LENGTH","LIKE",
            "LOCATE","LOWER","MAX","MEMBER","MIN","MOD","NEW","NOT",
            "NULL","NULLIF","OBJECT","OF","OR",
            "ORDER","OUTER","POSITION","SELECT","SET","SIZE","SOME",
            "SQRT","SUBSTRING","SUM","THEN",
            "TRAILING","TRIM","TRUE","TYPE","UNKNOWN","UPDATE","UPPER",
            "VALUE","WHEN","WHERE");
    
    public static final Collection<String> JDO = ImmutableList.of(
            "AS","ASC", "ASCENDING","AVG",
            "BY","COUNT", "DESC","DESCENDING",
            "DISTINCT","EXCLUDE", "FROM","GROUP",
            "HAVING","INTO","MAX","MIN",
            "ORDER","PARAMETERS","RANGE","SELECT",
            "SUBCLASSES","SUM","UNIQUE","VARIABLES","WHERE");

}
