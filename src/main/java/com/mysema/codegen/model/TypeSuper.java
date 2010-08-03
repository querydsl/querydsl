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


/**
 * TypeSuper is a Type for type variables and wildcard types
 *
 * @author tiwe
 *
 */
@Immutable
public class TypeSuper extends TypeAdapter{

    private final Type superType;

    @Nullable
    private final String varName;

    public TypeSuper(String varName, Type type) {
        super(Types.OBJECT);
        this.superType = type;
        this.varName = varName;
    }

    public TypeSuper(Type type) {
        super(Types.OBJECT);
        this.superType = type;
        this.varName = null;
    }
    
    @Override
    public String getGenericName(boolean asArgType){
        return getGenericName(asArgType, Collections.<String>emptySet(),Collections.<String>emptySet());
    }

    @Override
    public String getGenericName(boolean asArgType, Set<String> packages, Set<String> classes){
        if (!asArgType){
            return "? super " + superType.getGenericName(true, packages, classes);
        }else{
            return super.getGenericName(asArgType, packages, classes);
        }
    }
    
    @Nullable
    public String getVarName(){
        return varName;
    }
}
