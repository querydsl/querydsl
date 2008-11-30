/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt.general;

import java.util.HashMap;
import java.util.Map;

import com.mysema.query.apt.Type;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.FieldDeclaration;
import com.sun.mirror.declaration.Modifier;
import com.sun.mirror.util.SimpleDeclarationVisitor;

/**
 * EntityVisitor provides.
 * 
 * @author tiwe
 * @version $Id$
 */
public class EntityVisitor extends SimpleDeclarationVisitor {
    final Map<String, Type> types = new HashMap<String, Type>();

    private Type last;

    @Override
    public void visitClassDeclaration(ClassDeclaration d) {
        last = new Type(d);
        types.put(d.getQualifiedName(), last);
    }

    @Override
    public void visitFieldDeclaration(FieldDeclaration d) {
        if (!d.getModifiers().contains(Modifier.STATIC)) {
            last.addField(d);
        }
    }

}