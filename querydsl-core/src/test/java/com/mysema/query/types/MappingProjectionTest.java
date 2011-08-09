package com.mysema.query.types;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.commons.lang.Pair;
import com.mysema.query.Tuple;
import com.mysema.query.types.path.StringPath;

public class MappingProjectionTest {
    
    StringPath str1 = new StringPath("str1");
    StringPath str2 = new StringPath("str2");
    
    @SuppressWarnings("serial")
    @Test
    public void test(){                
        MappingProjection<Pair<String,String>> mapping = new MappingProjection<Pair<String,String>>(Pair.class, str1, str2){
            @Override
            protected Pair<String, String> map(Tuple row) {
                return Pair.of(row.get(str1), row.get(str2));
            }            
        };
        
        Pair<String, String> pair = mapping.newInstance("1", "2");
        assertEquals("1", pair.getFirst());
        assertEquals("2", pair.getSecond());
    }

}
