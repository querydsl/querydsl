package com.mysema.query.codegen;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Keywords sets in capitalized form to be used in GenericExporter and the APT processors
 * 
 * @author tiwe
 *
 */
public final class Keywords {
    
    private Keywords() {}
    
    public static final Collection<String> JPA = Collections.unmodifiableList((Arrays.asList(
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
    
    public static final Collection<String> JDO = Collections.unmodifiableList(Arrays.asList(
            "AS","ASC", "ASCENDING","AVG",
            "BY","COUNT", "DESC","DESCENDING",
            "DISTINCT","EXCLUDE", "FROM","GROUP",
            "HAVING","INTO","MAX","MIN",
            "ORDER","PARAMETERS","RANGE","SELECT",
            "SUBCLASSES","SUM","UNIQUE","VARIABLES","WHERE"));

}
