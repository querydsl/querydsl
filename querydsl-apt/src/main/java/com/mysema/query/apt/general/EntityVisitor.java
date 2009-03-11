/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt.general;

import java.util.HashMap;
import java.util.Map;

import com.mysema.query.apt.model.Field;
import com.mysema.query.apt.model.Type;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.FieldDeclaration;
import com.sun.mirror.declaration.InterfaceDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.Modifier;
import com.sun.mirror.util.SimpleDeclarationVisitor;

/**
 * EntityVisitor is a visitor for entity and embeddable types.
 * 
 * @author tiwe
 * @version $Id$
 */
public class EntityVisitor extends SimpleDeclarationVisitor {
    final Map<String, Type> types = new HashMap<String, Type>();

    private Type last;

    @Override
    public void visitClassDeclaration(ClassDeclaration d) {
        String simpleName = d.getSimpleName();
        String name = d.getQualifiedName();
        String packageName = d.getPackage().getQualifiedName();
        String superType = d.getSuperclass().getDeclaration().getQualifiedName();
        last = new Type(superType, packageName, name, simpleName);
        types.put(d.getQualifiedName(), last);
    }
    
    @Override
    public void visitInterfaceDeclaration(InterfaceDeclaration d){
        String simpleName = d.getSimpleName();
        String name = d.getQualifiedName();
        String packageName = d.getPackage().getQualifiedName();
        String superType = null;
        if (!d.getSuperinterfaces().isEmpty()){
            superType = d.getSuperinterfaces().iterator().next().getDeclaration().getQualifiedName();    
        }        
        last = new Type(superType, packageName, name, simpleName);
        types.put(d.getQualifiedName(), last);
    }

    @Override
    public void visitFieldDeclaration(FieldDeclaration d) {
        if (!d.getModifiers().contains(Modifier.STATIC) 
         && !d.getModifiers().contains(Modifier.TRANSIENT)) {
            TypeHelper typeInfo = new TypeHelper(d.getType());
            String name = FieldHelper.javaSafe(d.getSimpleName());
            String realName = FieldHelper.realName(name);
            String keyTypeName = typeInfo.getKeyTypeName();        
            String typeName = typeInfo.getFullName();
            String typePackage = typeInfo.getPackageName();
            String simpleTypeName = typeInfo.getSimpleName();
            Field.Type fieldType = typeInfo.getFieldType();   
            last.addField(new Field(name, realName, keyTypeName, typePackage, 
                    typeName, simpleTypeName, fieldType));
        }
    }
    
    @Override
    public void visitMethodDeclaration(MethodDeclaration d) {
        if (!d.getModifiers().contains(Modifier.STATIC)){
            // TODO
        }
    }

}