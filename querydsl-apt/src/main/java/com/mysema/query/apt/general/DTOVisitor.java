/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt.general;

import java.util.Set;
import java.util.TreeSet;

import com.mysema.query.apt.Type;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.ConstructorDeclaration;
import com.sun.mirror.util.SimpleDeclarationVisitor;

/**
 * DTOVisitor provides.
 * 
 * @author tiwe
 * @version $Id$
 */
public class DTOVisitor extends SimpleDeclarationVisitor {
    final Set<Type> types = new TreeSet<Type>();

    private Type last;

    @Override
    public void visitClassDeclaration(ClassDeclaration d) {
        last = new Type(d);
        types.add(last);
    }

    @Override
    public void visitConstructorDeclaration(ConstructorDeclaration d) {
        last.addConstructor(d);
    }

}
