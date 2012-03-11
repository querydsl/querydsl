package com.mysema.codegen.support;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class ScalaSyntaxUtils {

    private ScalaSyntaxUtils() {
    }

    private static final Set<String> reserved = new HashSet<String>(Arrays.asList("abstract", "do",
            "finally", "import", "object", "return", "trait", "var", "_", ":", "case", "else",
            "for", "lazy", "override", "sealed", "try", "while", "=", "=>", "<-", "catch",
            "extends", "forSome", "match", "package", "super", "true", "with", "<:", "class",
            "false", "if", "new", "private", "this", "type", "yield", "<%", ">:", "def", "final",
            "implicit", "null", "protected", "throw", "val", "#", "@"));

    public static boolean isReserved(String token) {
        return reserved.contains(token);
    }
}
