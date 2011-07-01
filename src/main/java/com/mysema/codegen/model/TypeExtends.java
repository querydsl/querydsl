/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.codegen.model;

import java.util.Collections;
import java.util.Set;

import javax.annotation.Nullable;

import net.jcip.annotations.Immutable;

import org.apache.commons.lang.StringUtils;

/**
 * @author tiwe
 *
 */
@Immutable
public class TypeExtends extends TypeAdapter{

    @Nullable
    private final String varName;

    public TypeExtends(String varName, Type type) {
        super(type);
        this.varName = varName;
    }

    public TypeExtends(Type type) {
        super(type);
        varName = null;
    }

    @Override
    public String getGenericName(boolean asArgType){
        return getGenericName(asArgType, Collections.<String>emptySet(),Collections.<String>emptySet());
    }

    @Override
    public String getGenericName(boolean asArgType, Set<String> packages, Set<String> classes){
        if (!asArgType){
            if (type.equals(Types.OBJECT)){
                return "?";
            }else{
                String genericName = super.getGenericName(true, packages, classes);
                return StringUtils.isEmpty(genericName) ? "?" : "? extends " + genericName;
            }
        }else{
            return super.getGenericName(asArgType, packages, classes);
        }
    }

    @Nullable
    public String getVarName(){
        return varName;
    }

}
