package com.mysema.query.domain;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ReservedNamesInTypesTest {
    
    @Test
    public void Correctly_Escaped(){
        assertNotNull(QPublic.public$);
        assertNotNull(QPrivate.private$);
    }

}
