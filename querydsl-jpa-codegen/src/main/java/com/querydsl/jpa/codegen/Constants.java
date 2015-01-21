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
package com.querydsl.jpa.codegen;

import java.util.List;

import com.google.common.collect.ImmutableList;

/**
 * Constants defines keywords used in Hibernate
 * 
 * @author tiwe
 *
 */
public final class Constants {
    
    public static final List<String> keywords = ImmutableList.of(
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

    private Constants() {}

}
