/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.domain;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;

public class InheritanceTest {

    @QueryEntity
    public abstract class BobbinGenOperation<M extends FlexPlasticFilm> extends Operation<M>{

    }

    @QueryEntity
    public abstract class Entity{

    }

    @QueryEntity
    public abstract class FlexPlastic extends Storable{

    }

    @QueryEntity
    public abstract class FlexPlasticFilm extends FlexPlastic implements Rimmable{

    }

    @QueryEntity
    public abstract class Merchandise extends Entity implements UnitConversionSupporter{

    }

    @QueryEntity
    public abstract class Operation<M extends Merchandise> extends Entity{

    }

    @QueryEntity
    public abstract class Party<A extends PartyRole> extends Entity{

    }

    public interface PartyRole{

    }

    @QueryEntity
    public class Person extends Party<PersonRole>{

    }

    public interface PersonRole extends PartyRole{

    }

    public interface Rimmable{

    }

    @QueryEntity
    public abstract class Storable extends Merchandise{

    }

    public interface UnitConversionSupporter{

    }

    @Test
    public void test(){
        assertNotNull(QInheritanceTest_BobbinGenOperation.bobbinGenOperation);
        assertNotNull(QInheritanceTest_Entity.entity);
        assertNotNull(QInheritanceTest_FlexPlastic.flexPlastic);
        assertNotNull(QInheritanceTest_FlexPlasticFilm.flexPlasticFilm);
        assertNotNull(QInheritanceTest_Merchandise.merchandise);
        assertNotNull(QInheritanceTest_Operation.operation);
        assertNotNull(QInheritanceTest_Party.party);
        assertNotNull(QInheritanceTest_Person.person);

    }

}
