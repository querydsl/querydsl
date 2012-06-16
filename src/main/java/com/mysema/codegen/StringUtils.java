package com.mysema.codegen;

import com.google.common.base.CaseFormat;

public final class StringUtils {
    
    public static String capitalize(String str) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, str);
    }
    
    public static String uncapitalize(String str) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, str);
    }

    public static String escapeJava(String str) {
        str = str.replace("\\", "\\\\");
        str = str.replace("\"", "\\\"");
        str = str.replace("\r", "\\\r");
        str = str.replace("\t", "\\\t");
        str = str.replace("\n", "\\\n");
        return str;
    }
    
    private StringUtils() {}

}
