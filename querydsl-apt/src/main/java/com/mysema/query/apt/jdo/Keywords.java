package com.mysema.query.apt.jdo;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public final class Keywords {
    
    public static final Collection<String> keywords = Collections.unmodifiableList(Arrays.asList(
            "AS","ASC", "ASCENDING","AVG",
            "BY","COUNT", "DESC","DESCENDING",
            "DISTINCT","EXCLUDE", "FROM","GROUP",
            "HAVING","INTO","MAX","MIN",
            "ORDER","PARAMETERS","RANGE","SELECT",
            "SUBCLASSES","SUM","UNIQUE","VARIABLES","WHERE"));
    
    private Keywords(){}

}
