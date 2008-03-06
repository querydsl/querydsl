/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt.hibernate;

import java.util.Set;
import java.util.TreeSet;

import com.mysema.query.apt.TypeDecl;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.ConstructorDeclaration;
import com.sun.mirror.util.SimpleDeclarationVisitor;

/**
 * DTOVisitor provides
 *
 * @author tiwe
 * @version $Id$
 */
public class DTOVisitor extends SimpleDeclarationVisitor {
    public final Set<TypeDecl> types = new TreeSet<TypeDecl>();

    private TypeDecl last;

    @Override
    public void visitClassDeclaration(ClassDeclaration d) {
        last = new TypeDecl(d);
        types.add(last);
    }
    @Override
    public void visitConstructorDeclaration(ConstructorDeclaration d){
        last.addConstructor(d);
    }
    
}
