/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt;

import com.mysema.query.apt.util.TypeInfo;
import com.sun.mirror.declaration.FieldDeclaration;

/**
 * FieldDecl provides
 *
 * @author tiwe
 * @version $Id$
 */
public class FieldDecl implements Comparable<FieldDecl>{
    
    private final FieldType fieldType;
    
    private String name, keyTypeName, typeName, simpleTypeName;

    public FieldDecl(FieldDeclaration field) {
        TypeInfo typeInfo = new TypeInfo(field.getType());
        this.name = field.getSimpleName();
        this.keyTypeName = typeInfo.getKeyTypeName();
        this.typeName = typeInfo.getFullName();
        this.simpleTypeName = typeInfo.getSimpleName();
        this.fieldType = typeInfo.getFieldType();        
    }
    
    public FieldDecl(String name, String keyTypeName, String typeName, 
            String simpleTypeName, FieldType fieldType){
        this.name = name;
        this.keyTypeName = keyTypeName;
        this.typeName = typeName;
        this.simpleTypeName = simpleTypeName;
        this.fieldType = fieldType;
    }
    
    public int compareTo(FieldDecl o) {
        return name.compareTo(o.name);
    }
    
    public boolean equals(Object o){
        return o instanceof FieldDecl && name.equals(((FieldDecl)o).name);
    }
    
    public FieldType getFieldType(){
        return fieldType;
    }
    
    public String getKeyTypeName(){
        return keyTypeName;
    }
    
    public String getName(){
        return name;
    }
    
    public String getSimpleTypeName(){
        return simpleTypeName;
    }
    
    public String getTypeName(){
        return typeName;
    }
    
    public int hashCode(){
        return name.hashCode();
    }
    
}