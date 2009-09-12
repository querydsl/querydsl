/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class JavaSyntaxUtils {
    
    private static final Set<String> reserved = new HashSet<String>(Arrays.asList(
    "abstract",    
    "assert",  
    "boolean",         
    "break",   
    "byte",    
    "case",
    "catch",   
    "char",    
    "class",   
    "const*",  
    "continue",        
    "default",
    "double",  
    "do",      
    "else",    
    "enum",    
    "extends",         
    "false",
    "final",   
    "finally",         
    "float",   
    "for",     
    "goto*",   
    "if",
    "implements",      
    "import",  
    "instanceof",      
    "int",     
    "interface",       
    "long",
    "native",  
    "new",
    "null",    
    "package",         
    "private", 
    "protected",
    "public",  
    "return",  
    "short",   
    "static",  
    "strictfp",
    "super",
    "switch",  
    "synchronized",
    "this",
    "throw",   
    "throws",  
    "transient",
    "true",
    "try", 
    "void",
    "volatile",
    "while"));   
    
    public static boolean isReserved(String str){
        return reserved.contains(str);
    }

}
