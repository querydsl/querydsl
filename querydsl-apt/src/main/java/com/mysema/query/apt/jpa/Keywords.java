package com.mysema.query.apt.jpa;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public final class Keywords {
    
    public static final Collection<String> keywords = Collections.unmodifiableList((Arrays.asList(
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
            "VALUE","WHEN","WHERE")));

    private Keywords() {}
    
}
