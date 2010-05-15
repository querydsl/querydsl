package com.mysema.query.sql.oracle;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mysema.query.types.path.PNumber;


public class SumOverTest {
    
    @Test
    public void test(){
	PNumber<Integer> intPath = new PNumber<Integer>(Integer.class, "intPath");
	SumOver<Integer> sumOver = new SumOver<Integer>(intPath);
	sumOver.order(intPath);
	sumOver.partition(intPath);
	
	assertEquals("sum(intPath) over (partition by intPath order by intPath)", sumOver.toString());
    }

}
