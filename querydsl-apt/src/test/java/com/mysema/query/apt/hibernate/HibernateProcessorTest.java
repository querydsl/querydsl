/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt.hibernate;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.mysema.query.apt.*;

import freemarker.template.TemplateException;

/**
 * HibernateProcessorTest provides.
 * 
 * @author tiwe
 * @version $Id$
 */
public class HibernateProcessorTest  {
        
    @Test
    public void testDomainTypes() throws IOException, TemplateException{        
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("domainTypes", Collections.singleton(createTypeDecl()));
        model.put("pre", "");
        model.put("include", "");
        model.put("package",  "com.mysema.query");
        model.put("classSimpleName", "Test");
        StringWriter writer = new StringWriter();
        new FreeMarkerSerializer("/querydsl-hibernate-domain.ftl").serialize(model, writer);
    }

    @Test
    public void testDTOTypes() throws IOException, TemplateException{
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("dtoTypes", Collections.singleton(createTypeDecl()));
        model.put("pre", "");
        model.put("package", "com.mysema.query");
        model.put("classSimpleName", "Test");
        StringWriter writer = new StringWriter();
        new FreeMarkerSerializer("/querydsl-hibernate-dto.ftl").serialize(model, writer);
    }
    
    private Type createTypeDecl() {
        Type typeDecl = new Type("com.mysema.query.DomainSuperClass","com.mysema.query.DomainClass","DomainClass");
        Field field = new Field("field",null,"java.lang.String","java.lang.String",Field.Type.STRING);
        typeDecl.addField(field);
        Parameter param = new Parameter("name","java.lang.String");
        typeDecl.addConstructor( new Constructor(Collections.singleton(param)));
        return typeDecl;
    }
}
