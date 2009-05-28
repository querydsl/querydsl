/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt.general;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.mysema.query.codegen.Constructor;
import com.mysema.query.codegen.Parameter;
import com.mysema.query.codegen.Type;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.ConstructorDeclaration;
import com.sun.mirror.declaration.ParameterDeclaration;
import com.sun.mirror.util.SimpleDeclarationVisitor;

/**
 * DefaultDTOVisitor is a visitor for DTO types.
 * 
 * @author tiwe
 * @version $Id$
 */
public class DefaultDTOVisitor extends SimpleDeclarationVisitor {
    final Set<Type> types = new TreeSet<Type>();

    private Type last;

    @Override
    public void visitClassDeclaration(ClassDeclaration d) {
        String simpleName = d.getSimpleName();
        String name = d.getQualifiedName();
        String packageName = d.getPackage().getQualifiedName();
        String superType = d.getSuperclass().getDeclaration()
                .getQualifiedName();
        last = new Type(superType, packageName, name, simpleName);
        types.add(last);
    }

    @Override
    public void visitConstructorDeclaration(ConstructorDeclaration d) {
        List<Parameter> parameters = new ArrayList<Parameter>(d.getParameters()
                .size());
        for (ParameterDeclaration pa : d.getParameters()) {
            String name = pa.getSimpleName();
            String typeName = new TypeHelper(pa.getType()).getFullName();
            parameters.add(new Parameter(name, typeName));
        }
        last.addConstructor(new Constructor(parameters));
    }

}
