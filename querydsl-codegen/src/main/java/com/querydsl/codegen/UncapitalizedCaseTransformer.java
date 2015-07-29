package com.querydsl.codegen;

import com.mysema.codegen.StringUtils;

/**
 * Uncapitalizes the first letter of a name.
 *
 */
public class UncapitalizedCaseTransformer implements CaseTransformer {

    /**
     * @see com.querydsl.codegen.CaseTransformer#transform(java.lang.String)
     */
    @Override
    public String transform(String name) {
        return StringUtils.uncapitalize(name);
    }

}
