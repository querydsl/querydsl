/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.serialization;

import java.util.Map;

import org.junit.Test;

import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.JavaTemplates;
import com.mysema.query.types.path.PathBuilder;
import com.mysema.query.types.path.StringPath;
import com.mysema.query.types.template.StringTemplate;

public class SerializerBaseTest {

    @Test
    public void test(){
        DummySerializer serializer = new DummySerializer(new JavaTemplates());
        StringPath strPath = new StringPath("str");
        // path
        serializer.handle(strPath);
        // operation
        serializer.handle(strPath.isNotNull());
        // long path
        serializer.handle(new PathBuilder<Object>(Object.class,"p").getList("l",Map.class).get(0));
        // constant
        serializer.handle(ConstantImpl.create(""));
        //  custom
        serializer.handle(StringTemplate.create("xxx", ConstantImpl.create("")));
    }

}
