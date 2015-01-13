package com.querydsl.apt.domain;

import java.io.Serializable;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;

import org.junit.Test;

import com.querydsl.apt.domain.QGeneric9Test_CommonOrganizationalUnit;

public class Generic9Test {
    
    @MappedSuperclass
    public static abstract class CommonOrganizationalUnit<T extends EntityLocalized, E extends TenantPreference, P extends CommonOrganizationalUnit<?, ?, ?>> extends
        LocalizableEntity<T> implements Serializable, Comparable<CommonOrganizationalUnit<T, E, P>> {
        
        P parent;
        
//        CommonOrganizationalUnit<?,?,?> parent2;
//        
//        CommonOrganizationalUnit<T,E,P> parent3;
        
    }

    @MappedSuperclass
    public static abstract class ProductionSurface<T extends EntityLocalized, E extends TenantPreference, P extends CommonOrganizationalUnit<?, ?, ?>> extends
        CommonOrganizationalUnit<T, E, P> implements Serializable {
        
    }

//    @Entity
//    public class Building extends ProductionSurface<BuildingLocalized, BuildingPreference, Site> {
//        
//    }
    
    @MappedSuperclass
    public static abstract class EntityLocalized extends CommonEntity {
        
    }
    
    @Entity
    public static class Preference {
        
    }

    @Entity
    @Table(name = "preference")
    @DiscriminatorColumn(name = "discriminator", discriminatorType = DiscriminatorType.STRING)
    public static abstract class TenantPreference extends Preference {
        
    }

    @MappedSuperclass
    public static abstract class CommonEntity {
        
    }
    
    @MappedSuperclass
    public static abstract class LocalizableEntity<T extends EntityLocalized> extends CommonEntity {
        
    }
    
    @Test
    public void test() {
        new QGeneric9Test_CommonOrganizationalUnit("test");
    }

}
