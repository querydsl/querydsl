/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.serialization;

import java.util.Map;

import org.junit.Test;

import com.mysema.query.types.custom.CString;
import com.mysema.query.types.expr.EStringConst;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PathBuilder;

public class SerializerBaseTest {

    @Test
    public void test(){
    DummySerializer serializer = new DummySerializer(new JavaTemplates());
    PString strPath = new PString("str");
    // path
    serializer.handle(strPath);
    // operation
    serializer.handle(strPath.isNotNull());
    // long path
    serializer.handle(new PathBuilder<Object>(Object.class,"p").getList("l",Map.class).get(0));
    // constant
    serializer.handle(EStringConst.create(""));
    // custom
    serializer.handle(CString.create("xxx",EStringConst.create("")));
    }

}
