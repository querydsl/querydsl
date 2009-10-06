package com.mysema.query.collections.domain;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.mysema.query.types.path.PSimple;

public class AnimalTest {
    
    @Test
    public void dateAsSimple(){
        assertTrue(QAnimal.animal.dateAsSimple.getClass().equals(PSimple.class));
    }

}
