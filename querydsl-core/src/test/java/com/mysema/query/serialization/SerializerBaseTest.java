/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.serialization;

import java.util.Map;

import org.junit.Test;

import com.mysema.query.types.JavaTemplates;
import com.mysema.query.types.custom.StringTemplate;
import com.mysema.query.types.expr.StringConstant;
import com.mysema.query.types.path.StringPath;
import com.mysema.query.types.path.PathBuilder;

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
    serializer.handle(StringConstant.create(""));
    // custom
    serializer.handle(StringTemplate.create("xxx",StringConstant.create("")));
    }

}
