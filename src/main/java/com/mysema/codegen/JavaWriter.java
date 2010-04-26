/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.codegen;

import static com.mysema.codegen.Symbols.ASSIGN;
import static com.mysema.codegen.Symbols.COMMA;
import static com.mysema.codegen.Symbols.DOT;
import static com.mysema.codegen.Symbols.NEWLINE;
import static com.mysema.codegen.Symbols.QUOTE;
import static com.mysema.codegen.Symbols.SEMICOLON;
import static com.mysema.codegen.Symbols.SPACE;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

/**
 * JavaWriter is the default implementation of the CodeWriter interface
 * 
 * @author tiwe
 *
 */
public final class JavaWriter implements Appendable, CodeWriter{
        
    private static final String EXTENDS = " extends ";

    private static final String IMPLEMENTS = " implements ";

    private static final String IMPORT = "import ";

    private static final String IMPORT_STATIC = "import static ";

    private static final String PACKAGE = "package ";

    private static final String PRIVATE = "private ";
    
    private static final String PRIVATE_STATIC_FINAL = "private static final ";

    private static final String PROTECTED = "protected ";

    private static final String PUBLIC = "public ";

    private static final String PUBLIC_CLASS = "public class ";

    private static final String PUBLIC_FINAL = "public final ";

    private static final String PUBLIC_INTERFACE = "public interface ";

    private static final String PUBLIC_STATIC = "public static ";

    private static final String PUBLIC_STATIC_FINAL = "public static final ";
    
    private final Appendable appendable;
    
    private final Set<Class<?>> importedClasses = new HashSet<Class<?>>();
    
    private final Set<Package> importedPackages = new HashSet<Package>();
    
    private String indent = "";
    
    private String type;
    
    public JavaWriter(Appendable appendable){
        if (appendable == null){
            throw new IllegalArgumentException("appendable is null");
        }
        this.appendable = appendable;
        this.importedPackages.add(Object.class.getPackage());
    }
    
    @Override
    public JavaWriter annotation(Annotation annotation) throws IOException {
        append(indent).append("@").appendType(annotation.annotationType()).append("(");
        boolean first = true;        
        for (Method method : annotation.annotationType().getDeclaredMethods()){            
            try {
                Object value = method.invoke(annotation);
                if (value == null){
                    continue;
                }else if (!first){
                    append(COMMA);
                }
                append(method.getName()+"=");                
                annotationConstant(value);
            } catch (IllegalArgumentException e) {
                throw new CodegenException(e);
            } catch (IllegalAccessException e) {
                throw new CodegenException(e);
            } catch (InvocationTargetException e) {
                throw new CodegenException(e);
            }
            first = false;
        }        
        return append(")").nl();
     }    
    

    @Override
    public JavaWriter annotation(Class<? extends Annotation> annotation) throws IOException{
        return append(indent).append("@").appendType(annotation).nl();
    }
    
    @SuppressWarnings("unchecked")
    private void annotationConstant(Object value) throws IOException{
         if (value instanceof Class){
             appendType((Class)value).append(".class");             
         }else if (value instanceof Number){
             append(value.toString());
         }else if (value instanceof Enum){
             Enum enumValue = (Enum)value;
             append(enumValue.getDeclaringClass().getName()+DOT+enumValue.name());
         }else if (value instanceof String){
             append(QUOTE + StringEscapeUtils.escapeJava(value.toString()) + QUOTE);
         }else{
             throw new IllegalArgumentException("Unsupported annotation value : " + value);
         }
     }
    
    
    @Override
    public JavaWriter append(char c) throws IOException {
        appendable.append(c);
        return this;
    }
 
    @Override
    public JavaWriter append(CharSequence csq) throws IOException {
        appendable.append(csq);
        return this;
    }

    @Override
    public JavaWriter append(CharSequence csq, int start, int end) throws IOException {
        appendable.append(csq, start, end);
        return this;
    }

    private JavaWriter appendType(Class<?> type) throws IOException{
        if (importedClasses.contains(type) || importedPackages.contains(type.getPackage())){
            append(type.getSimpleName());
        }else{
            append(type.getName());
        }
        return this;
    }

    public JavaWriter beginClass(String simpleName) throws IOException{
        return beginClass(simpleName, null);
    }
    
    @Override
    public JavaWriter beginClass(String simpleName, String superClass, String... interfaces) throws IOException{
        append(indent + PUBLIC_CLASS + simpleName);
        if (superClass != null){
            append(EXTENDS + superClass);
        }
        if (interfaces.length > 0){
            append(IMPLEMENTS);//.join(COMMA, interfaces);
            append(StringUtils.join(interfaces, COMMA));
        }
        append(" {").nl().nl();
        goIn();
        
        type = simpleName;
        if (type.contains("<")){
            type = type.substring(0, type.indexOf('<'));
        }
        return this;
    }
 
    @Override
    public <T> JavaWriter beginConstructor(Collection<T> parameters, Transformer<T,String> transformer) throws IOException {
        append(indent + PUBLIC + type).params(parameters, transformer).append(" {").nl();
        return goIn();        
    }
    
    @Override
    public JavaWriter beginConstructor(String... parameters) throws IOException{
        append(indent + PUBLIC + type).params(parameters).append(" {").nl();
        return goIn();
    }
    
    @Override
    public JavaWriter beginInterface(String simpleName, String... interfaces) throws IOException {
        append(indent + PUBLIC_INTERFACE + simpleName);
        if (interfaces.length > 0){
            append(EXTENDS);
            append(StringUtils.join(interfaces, COMMA));
        }
        append(" {").nl().nl();
        goIn();
        
        type = simpleName;
        if (type.contains("<")){
            type = type.substring(0, type.indexOf('<'));
        }
        return this;
        
    }
 
    @Override
    public JavaWriter beginLine(String... segments) throws IOException {
        append(indent);
        for (String segment : segments){
            append(segment);
        }
        return this;
    }
    
    private JavaWriter beginMethod(String modifiers, String returnType, String methodName, String... args) throws IOException{
        append(indent + modifiers + returnType + SPACE + methodName).params(args).append(" {").nl();
        return goIn();
    }
    
    @Override
    public <T> JavaWriter beginPublicMethod(String returnType, String methodName, Collection<T> parameters, Transformer<T, String> transformer) throws IOException {
        return beginMethod(PUBLIC, returnType, methodName, transform(parameters, transformer));
    }

    @Override
    public JavaWriter beginPublicMethod(String returnType, String methodName, String... args) throws IOException{
        return beginMethod(PUBLIC, returnType, methodName, args);
    }
    
    @Override
    public <T> JavaWriter beginStaticMethod(String returnType, String methodName, Collection<T> parameters, Transformer<T, String> transformer) throws IOException {
        return beginMethod(PUBLIC_STATIC, returnType, methodName, transform(parameters, transformer));
    }

    @Override
    public JavaWriter beginStaticMethod(String returnType, String methodName, String... args) throws IOException{
        return beginMethod(PUBLIC_STATIC, returnType, methodName, args);
    }
    
    @Override
    public JavaWriter end() throws IOException{
        goOut();
        return line("}").nl();
    }
 
    @Override
    public JavaWriter field(String type, String name) throws IOException {
        return stmt(type + SPACE + name).nl();
    }

    private JavaWriter goIn(){
        indent += "    ";
        return this;
    }
    
    private JavaWriter goOut(){
        if (indent.length() >= 4){
            indent = indent.substring(0, indent.length() - 4);
        }
        return this;
    }

    @Override
    public JavaWriter imports(Class<?>... imports) throws IOException{
        for (Class<?> cl : imports){
            importedClasses.add(cl);
            line(IMPORT + cl.getName() + SEMICOLON);
        }
        nl();
        return this;
    }
    
    @Override
    public JavaWriter imports(Package... imports) throws IOException {
        for (Package p : imports){
            importedPackages.add(p);
            line(IMPORT + p.getName() + ".*;");
        }
        nl();
        return this;
    }

    @Override
    public JavaWriter javadoc(String... lines) throws IOException {
        line("/**");
        for (String line : lines){
            line(" * " + line);
        }
        return line(" */");
    }

    @Override
    public JavaWriter line(String... segments) throws IOException{
        append(indent);
        for (String segment : segments){
            append(segment);
        }        
        return nl();
    }

    @Override
    public JavaWriter nl() throws IOException {
        return append(NEWLINE);        
    }

    @Override
    public JavaWriter packageDecl(String packageName) throws IOException{
        importedPackages.add(Package.getPackage(packageName));
        return line(PACKAGE + packageName + SEMICOLON).nl();
    }
    
    private <T> JavaWriter params(Collection<T> parameters, Transformer<T,String> transformer) throws IOException{
        append("(");
        boolean first = true;
        for (T param : parameters){
            if (!first){
                append(COMMA);
            }
            append(transformer.transform(param));
            first = false;
        }
        append(")");
        return this;
    }
    
    private JavaWriter params(String... params) throws IOException{
        append("(");
        append(StringUtils.join(params, COMMA));
        append(")");
        return this;
    }

    @Override
    public JavaWriter privateField(String type, String name) throws IOException {
        return field(PRIVATE, type, name);
    }
    
    @Override
    public JavaWriter privateStaticFinal(String type, String name, String value) throws IOException {
        return field(PRIVATE_STATIC_FINAL, type, name, value);
    }
    
    @Override
    public JavaWriter protectedField(String type, String name) throws IOException {
        return field(PROTECTED, type, name);        
    }
        
    @Override
    public JavaWriter publicField(String type, String name) throws IOException {
        return field(PUBLIC, type, name);
    }
    
    @Override
    public JavaWriter publicFinal(String type, String name) throws IOException {
        return field(PUBLIC_FINAL, type, name);        
    }

    @Override
    public JavaWriter publicFinal(String type, String name, String value) throws IOException {
        return field(PUBLIC_FINAL, type, name, value);
    }

    @Override
    public JavaWriter publicStaticFinal(String type, String name, String value) throws IOException {
        return field(PUBLIC_STATIC_FINAL, type, name, value);
    }
    
    private JavaWriter field(String modifier, String type, String name) throws IOException{
        return stmt(modifier + type + SPACE + name).nl();
    }
    
    private JavaWriter field(String modifier, String type, String name, String value) throws IOException{
        return stmt(modifier + type + SPACE + name + ASSIGN + value).nl();
    }
    
    @Override
    public JavaWriter staticimports(Class<?>... imports) throws IOException{
        for (Class<?> cl : imports){
            line(IMPORT_STATIC + cl.getName() + ".*;");
        }
        return this;
    }

    private JavaWriter stmt(String stmt) throws IOException{
        return line(stmt + SEMICOLON);
    }

    @Override
    public JavaWriter suppressWarnings(String type) throws IOException{
        return line("@SuppressWarnings(\"" + type +"\")");
    }
    
    private <T> String[] transform(Collection<T> parameters, Transformer<T,String> transformer){
        String[] rv = new String[parameters.size()];
        int i = 0; 
        for (T value : parameters){
            rv[i++] = transformer.transform(value);
        }
        return rv;
    }

}
