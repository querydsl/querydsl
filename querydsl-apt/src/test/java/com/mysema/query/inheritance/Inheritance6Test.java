package com.mysema.query.inheritance;

import static org.junit.Assert.assertEquals;

import java.io.Serializable;
import java.util.Date;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QuerySupertype;
import com.mysema.query.types.path.DateTimePath;
import com.mysema.query.types.path.NumberPath;

/**
 * Test multiple level superclasses with generics.
 */
public class Inheritance6Test {

    /*
     * Top superclass.
     */
    @QuerySupertype
    public static class CommonIdentifiable<ID extends Serializable> {
        @SuppressWarnings("unused")
        private ID id;

        @SuppressWarnings("unused")
        private Date createdOn;
    }

    /*
     * Intermediate superclass, equivalent to @MappedSuperclass.
     */
    @QuerySupertype
    public abstract static class Translation<T extends Translation<T, K>, K extends TranslationKey<T, K>>
            extends CommonIdentifiable<Long> {
        @SuppressWarnings("unused")
        private String value;
    }

    /*
     * Intermediate superclass, equivalent to @MappedSuperclass.
     */
    @QuerySupertype
    public abstract static class TranslationKey<T extends Translation<T, K>, K extends TranslationKey<T, K>>
            extends CommonIdentifiable<Long> {
    }

    @QueryEntity
    public static class Gloss extends Translation<Gloss, GlossKey> {
    }

    @QueryEntity
    public static class GlossKey extends TranslationKey<Gloss, GlossKey> {
    }

    @Test
    public void gloss_subtype_should_contain_fields_from_superclass() {
        assertEquals(String.class, QInheritance6Test_Gloss.gloss.value.getType());
    }

    @Test
    public void intermediate_superclass_should_contain_fields_from_top_superclass() {
         QInheritance6Test_Translation translation = QInheritance6Test_Gloss.gloss._super;
         assertEquals(DateTimePath.class, translation.createdOn.getClass());
    }

    @Test
    public void gloss_subtype_should_contain_fields_from_top_superclass() {
        assertEquals(DateTimePath.class, QInheritance6Test_Gloss.gloss.createdOn.getClass());
    }

    @Test
    public void gloss_subtype_should_contain_id_from_top_superclass() {
        assertEquals(NumberPath.class, QInheritance6Test_Gloss.gloss.id.getClass());
    }

}
