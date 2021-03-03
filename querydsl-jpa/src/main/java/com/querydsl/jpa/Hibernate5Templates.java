package com.querydsl.jpa;

/**
 * Hibernate5Templates extends {@link JPQLTemplates} with Hibernate specific extensions
 *
 * @author Jan-Willem Gmelig Meyling
 *
 */
public class Hibernate5Templates extends HQLTemplates {

    public static final Hibernate5Templates DEFAULT = new Hibernate5Templates();

    public Hibernate5Templates() {
    }

    public Hibernate5Templates(char escape) {
        super(escape);
    }

    @Override
    public boolean wrapConstant(Object constant) {
        // HHH-6913 is fixed in 5.0, default to JPA behaviour
        return false;
    }

    @Override
    public boolean isWithForOn() {
        // Hibernate supports the on-clause since 5.0, and the ON clause is actually mandatory for entity joins
        return true;
    }

}
