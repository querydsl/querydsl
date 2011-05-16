/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.JoinType;
import com.mysema.query.QueryMetadata;
import com.mysema.query.jpa.domain.QEmployee;
import com.mysema.query.types.path.NumberPath;

public class JPQLSerializerTest {

    @Test
    public void NormalizeNumericArgs() {
        JPQLSerializer serializer = new JPQLSerializer(HQLTemplates.DEFAULT);
        NumberPath<Double> doublePath = new NumberPath<Double>(Double.class, "doublePath");
        serializer.handle(doublePath.add(1));
        serializer.handle(doublePath.between((float)1.0, 1l));
        serializer.handle(doublePath.lt((byte)1));
        for (Object constant : serializer.getConstantToLabel().keySet()){
            assertEquals(Double.class, constant.getClass());
        }
    }

    @Test
    public void Delete_Clause_Uses_DELETE_FROM() {
        JPQLSerializer serializer = new JPQLSerializer(HQLTemplates.DEFAULT);
        QueryMetadata md = new DefaultQueryMetadata();
        md.addJoin(JoinType.DEFAULT, QEmployee.employee);
        md.addWhere(QEmployee.employee.lastName.isNull());
        serializer.serializeForDelete(md);
        assertEquals("delete from Employee employee\nwhere employee.lastName is null", serializer.toString());
    }
}
