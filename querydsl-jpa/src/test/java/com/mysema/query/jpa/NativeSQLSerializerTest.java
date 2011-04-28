package com.mysema.query.jpa;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.JoinType;
import com.mysema.query.jpa.domain.sql.SAnimal;
import com.mysema.query.sql.MySQLTemplates;


public class NativeSQLSerializerTest {
    
    @Test
    @Ignore
    public void In() {
        NativeSQLSerializer serializer = new NativeSQLSerializer(new MySQLTemplates());
        DefaultQueryMetadata md = new DefaultQueryMetadata();
        SAnimal cat = SAnimal.animal;
        md.addJoin(JoinType.DEFAULT, cat);
        md.addWhere(cat.name.in("X", "Y"));
        md.addProjection(cat.id);
        serializer.serialize(md, false);
        assertEquals("cat.*.id in :a1", serializer.toString());        
    }

}
