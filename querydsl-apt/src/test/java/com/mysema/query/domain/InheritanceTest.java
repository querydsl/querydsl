package com.mysema.query.domain;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;

public class InheritanceTest {
    
    @QueryEntity
    public abstract class Entity{
        
    }
    
    @QueryEntity
    public abstract class Party<A extends PartyRole> extends Entity{
        
    }
        
    @QueryEntity
    public class Person extends Party<PersonRole>{
        
    }
    
    public interface PartyRole{
        
    }
    
    public interface PersonRole extends PartyRole{
        
    }
    
    @QueryEntity
    public abstract class BobbinGenOperation<M extends FlexPlasticFilm> extends Operation<M>{
        
    }
    
    @QueryEntity
    public abstract class Operation<M extends Merchandise> extends Entity{
        
    }
    
    @QueryEntity
    public abstract class FlexPlasticFilm extends FlexPlastic implements Rimmable{
        
    }
    
    @QueryEntity
    public abstract class FlexPlastic extends Storable{
        
    }
    
    public abstract class Storable extends Merchandise{
        
    }
    
    @QueryEntity
    public abstract class Merchandise extends Entity implements UnitConversionSupporter{
        
    }
    
    public interface Rimmable{
        
    }
    
    public interface UnitConversionSupporter{
        
    }
    
    @Test
    public void test(){
        // TODO
    }

}
