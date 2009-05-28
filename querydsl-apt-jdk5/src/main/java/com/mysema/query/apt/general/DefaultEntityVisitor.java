/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt.general;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.mysema.query.codegen.FieldModel;
import com.mysema.query.codegen.FieldType;
import com.mysema.query.codegen.ClassModel;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.FieldDeclaration;
import com.sun.mirror.declaration.InterfaceDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.Modifier;
import com.sun.mirror.util.SimpleDeclarationVisitor;

/**
 * DefaultEntityVisitor is a visitor for entity and embeddable types.
 * 
 * @author tiwe
 * @version $Id$
 */
public class DefaultEntityVisitor extends SimpleDeclarationVisitor {
    private ClassModel last;

    public final Map<String, ClassModel> types = new HashMap<String, ClassModel>();

    private void addField(String name, TypeHelper typeInfo) {
        String keyTypeName = typeInfo.getKeyTypeName();
        String typeName = typeInfo.getFullName();
        String typePackage = typeInfo.getPackageName();
        String simpleTypeName = typeInfo.getSimpleName();
        FieldType fieldType = typeInfo.getFieldType();
        last.addField(new FieldModel(name, keyTypeName, typePackage,
                typeName, simpleTypeName, fieldType));
    }

    @Override
    public void visitClassDeclaration(ClassDeclaration d) {
        String simpleName = d.getSimpleName();
        String name = d.getQualifiedName();
        String packageName = d.getPackage().getQualifiedName();
        String superType = d.getSuperclass().getDeclaration()
                .getQualifiedName();
        last = new ClassModel(superType, packageName, name, simpleName);
        types.put(d.getQualifiedName(), last);
    }

    @Override
    public void visitFieldDeclaration(FieldDeclaration d) {
        if (!d.getModifiers().contains(Modifier.STATIC)
                && !d.getModifiers().contains(Modifier.TRANSIENT)) {
            addField(d.getSimpleName(), new TypeHelper(d.getType()));
        }
    }

    @Override
    public void visitInterfaceDeclaration(InterfaceDeclaration d) {
        String simpleName = d.getSimpleName();
        String name = d.getQualifiedName();
        String packageName = d.getPackage().getQualifiedName();
        String superType = null;
        if (!d.getSuperinterfaces().isEmpty()) {
            superType = d.getSuperinterfaces().iterator().next()
                    .getDeclaration().getQualifiedName();
        }
        last = new ClassModel(superType, packageName, name, simpleName);
        types.put(d.getQualifiedName(), last);
    }

    @Override
    public void visitMethodDeclaration(MethodDeclaration d) {
        if (!d.getModifiers().contains(Modifier.STATIC)) {
            if (d.getParameters().isEmpty()) {
                if (d.getSimpleName().startsWith("get")) {
                    String name = StringUtils.uncapitalize(d.getSimpleName()
                            .substring(3));
                    addField(name, new TypeHelper(d.getReturnType()));
                } else if (d.getSimpleName().startsWith("is")) {
                    String name = StringUtils.uncapitalize(d.getSimpleName()
                            .substring(2));
                    addField(name, new TypeHelper(d.getReturnType()));
                }
            }
        }
    }

}