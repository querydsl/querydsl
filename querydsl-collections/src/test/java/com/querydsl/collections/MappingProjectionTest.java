package com.querydsl.collections;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.MappingProjection;

@SuppressWarnings("serial")
public class MappingProjectionTest extends AbstractQueryTest {
    
    public class ResultPart {
        
    }
    
    public class ResultObject {
        
    }
    
    @Test
    public void test() {
        final MappingProjection<ResultPart> key = new MappingProjection<ResultPart>(ResultPart.class, 
                cat.name) {

            @Override
            protected ResultPart map(Tuple row) {
                return new ResultPart();
            }

        };
        
        List<ResultObject> list = query().from(cat, cats).list(
            new MappingProjection<ResultObject>(ResultObject.class, key) {

                @Override
                protected ResultObject map(Tuple row) {
                    ResultPart consolidationKey = row.get(key);
                    return new ResultObject();
                }
            });
        
        assertEquals(cats.size(), list.size());
    }

}
