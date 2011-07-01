package com.mysema.query.domain;

import org.junit.Ignore;

import com.mysema.query.annotations.QueryEntity;

@Ignore
public class Enum2Test {
    
    @QueryEntity
    public abstract class EnumPermissions<P extends Enum<P> & Permission> extends EntityImpl implements Permissions<P> {
    
    }

    @QueryEntity
    public abstract class EntityImpl {
                
    }

    public interface Permission {
        
    }
    
    public interface Permissions<P> {
        
    }
    
}
