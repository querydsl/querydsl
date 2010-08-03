/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.codegen.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class TypeTest {

    private Set<String> emptyStrings = Collections.<String>emptySet();
        
    private ClassType<Locale> locale = new ClassType<Locale>(TypeCategory.SIMPLE,Locale.class);
    
    private Type string = Types.STRING;
    
    private Type string2 = new SimpleType(string);
    
    private Type locale2 = new SimpleType(locale);
    
    private Type stringList = new ClassType<List>(TypeCategory.LIST, List.class, Types.STRING);
    
    private Type stringList2 = new SimpleType(Types.LIST, Types.STRING);
    
    private Type stringMap = new ClassType<Map>(TypeCategory.MAP, Map.class, Types.STRING, Types.STRING);
    
    private Type stringMap2 = new SimpleType(Types.MAP, Types.STRING, Types.STRING);
   
    @Test
    public void arrayType(){
        assertEquals("Object[]",Types.OBJECTS.getGenericName(true).toString());
    }
    
    @Test
    public void testEquals(){
        assertEquals(locale, locale2);
        assertEquals(locale2, locale);
        assertEquals(stringList, stringList2);
        assertEquals(stringList2, stringList);
    }
    
    @Test
    public void testHashcode(){
        assertEquals(locale.hashCode(), locale2.hashCode());
        assertEquals(stringList.hashCode(), stringList2.hashCode());
    }
    
    @Test
    public void testGetGenericNameBoolean() {
        assertEquals("java.util.Locale",locale.getGenericName(true));
        assertEquals("java.util.Locale",locale2.getGenericName(true));
        assertEquals("java.util.List<String>",stringList.getGenericName(true));
        assertEquals("java.util.List<String>",stringList2.getGenericName(true));
        assertEquals("java.util.Map<String,String>", stringMap.getGenericName(true));
        assertEquals("java.util.Map<String,String>", stringMap2.getGenericName(true));
        
        assertEquals("String", string.getGenericName(true));
        assertEquals("String", string2.getGenericName(true));
    }

    @Test
    public void testGetRawName() {
        assertEquals("java.util.Locale",locale.getRawName(emptyStrings,emptyStrings));
        assertEquals("java.util.Locale",locale2.getRawName(emptyStrings,emptyStrings));
        assertEquals("java.util.List",stringList.getRawName(emptyStrings,emptyStrings));
        assertEquals("java.util.List",stringList2.getRawName(emptyStrings,emptyStrings));
        
        assertEquals("String", string.getRawName(emptyStrings, emptyStrings));
        assertEquals("String", string2.getRawName(emptyStrings, emptyStrings));
    }

    @Test
    public void testGetGenericNameBooleanSetOfStringSetOfString() {
        assertEquals("java.util.Locale",locale.getGenericName(true, emptyStrings, emptyStrings));
        assertEquals("java.util.Locale",locale2.getGenericName(true, emptyStrings, emptyStrings));
        assertEquals("java.util.List<String>",stringList.getGenericName(true,emptyStrings,emptyStrings));
        assertEquals("java.util.List<String>",stringList2.getGenericName(true,emptyStrings,emptyStrings));
    }

    @Test
    public void testGetFullName() {
        assertEquals("java.util.Locale",locale.getFullName());
        assertEquals("java.util.Locale",locale2.getFullName());
        assertEquals("java.util.List",stringList.getFullName());
        assertEquals("java.util.List",stringList2.getFullName());
    }

    @Test
    public void testGetPackageName() {
        assertEquals("java.util",locale.getPackageName());
        assertEquals("java.util",locale2.getPackageName());
        assertEquals("java.util",stringList.getPackageName());
        assertEquals("java.util",stringList2.getPackageName());
    }

    @Test
    public void testGetParameters() {
        assertEquals(Collections.emptyList(), locale.getParameters());
        assertEquals(Collections.emptyList(), locale2.getParameters());
        assertEquals(Collections.singletonList(Types.STRING), stringList.getParameters());
        assertEquals(Collections.singletonList(Types.STRING), stringList2.getParameters());
    }

    @Test
    public void testGetSimpleName() {
        assertEquals("Locale",locale.getSimpleName());
        assertEquals("Locale",locale2.getSimpleName());
        assertEquals("List",stringList.getSimpleName());
        assertEquals("List",stringList2.getSimpleName());
    }

    @Test
    public void testGetJavaClass() {
        assertEquals(Locale.class,locale.getJavaClass());
    }

    @Test
    public void testIsFinal() {
        assertTrue(locale.isFinal());
        assertTrue(locale2.isFinal());
        assertFalse(stringList.isFinal());
        
        assertTrue(Types.STRING.isFinal());
        assertTrue(Types.LONG.isFinal());
    }

    @Test
    public void testIsPrimitive() {
        assertFalse(locale.isPrimitive());
        assertFalse(locale2.isPrimitive());
        assertFalse(stringList.isPrimitive());
        assertFalse(stringList2.isPrimitive());
    }

    @Test
    public void testGetCategory() {
        assertEquals(TypeCategory.SIMPLE, locale.getCategory());
        assertEquals(TypeCategory.SIMPLE, locale2.getCategory());
        assertEquals(TypeCategory.LIST, stringList.getCategory());
        assertEquals(TypeCategory.LIST, stringList2.getCategory());
    }

    @Test
    public void testAs() {
        assertEquals(TypeCategory.SIMPLE, stringList.as(TypeCategory.SIMPLE).getCategory());
        assertEquals(TypeCategory.SIMPLE, stringList2.as(TypeCategory.SIMPLE).getCategory());
    }

    @Test
    public void testGetPrimitiveName() {
        assertNull(locale.getPrimitiveName());
        assertNull(locale2.getPrimitiveName());
        assertNull(stringList.getPrimitiveName());
        assertNull(stringList2.getPrimitiveName());
    }

    @Test
    public void testToString() {
        assertEquals("java.util.Locale",locale.toString());
        assertEquals("java.util.Locale",locale2.toString());
        assertEquals("java.util.List<String>",stringList.toString());
        assertEquals("java.util.List<String>",stringList2.toString());
    }

    @Test
    public void testAsArrayType() {
        assertEquals("java.util.Locale[]", locale.asArrayType().getFullName());
        assertEquals(TypeCategory.ARRAY, locale.asArrayType().getCategory());
        assertEquals("java.util.Locale[]", locale2.asArrayType().getFullName());
        assertEquals(TypeCategory.ARRAY, locale2.asArrayType().getCategory());
        assertEquals("java.util.List[]", stringList.asArrayType().getFullName());
        assertEquals("java.util.List[]", stringList2.asArrayType().getFullName());
    }

}
