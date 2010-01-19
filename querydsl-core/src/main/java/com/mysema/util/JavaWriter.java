/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.collections15.Transformer;

/**
 * @author tiwe
 *
 */
public class JavaWriter implements Appendable, CodeWriter{
    
    private final Appendable appendable;
    
    private String type;
    
    private String indent = "";
    
    public JavaWriter(Appendable appendable){
        this.appendable = appendable;
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

    public CodeWriter beginClass(String simpleName, String superClass, String... interfaces) throws IOException{
        append(indent + "public class " + simpleName);
        if (superClass != null){
            append(" extends " + superClass);
        }
        if (interfaces.length > 0){
            append(" implements ").params(interfaces);
        }
        append("{\n\n");
        goIn();
        
        type = simpleName;
        if (type.contains("<")) type = type.substring(0, type.indexOf('<'));
        return this;
    }

    public <T> CodeWriter  beginConstructor(Collection<T> parameters, Transformer<T,String> transformer) throws IOException {
        append(indent + "public " + type + "(").params(parameters, transformer).append("){\n");
        return goIn();        
    }

    public CodeWriter beginConstructor(String... parameters) throws IOException{
        append(indent + "public " + type + "(").params(parameters).append("){\n");
        return goIn();
    }
 
    public CodeWriter beginInterface(String simpleName, String... interfaces) throws IOException {
        append(indent + "public interface " + simpleName);
        if (interfaces.length > 0){
            append(" extends ").params(interfaces);
        }
        append("{\n\n");
        goIn();
        
        type = simpleName;
        if (type.contains("<")) type = type.substring(0, type.indexOf('<'));
        return this;
        
    }
    
    @Override
    public CodeWriter beginLine(String... segments) throws IOException {
        append(indent);
        for (String segment : segments){
            append(segment);
        }
        return this;
    }
    
    public <T> CodeWriter beginMethod(String returnType, String methodName, Collection<T> parameters, Transformer<T, String> transformer) throws IOException {
        append(indent + "public " + returnType + " " + methodName + "(").params(parameters, transformer).append("){\n");
        return goIn();
    }
 
    public CodeWriter beginMethod(String returnType, String methodName, String... args) throws IOException{
        append(indent + "public " + returnType + " " + methodName + "(").params(args).append("){\n");
        return goIn();
    }
    
    public <T> CodeWriter beginStaticMethod(String returnType, String methodName, Collection<T> parameters, Transformer<T, String> transformer) throws IOException {
        append(indent + "public static " + returnType + " " + methodName + "(").params(parameters, transformer).append("){\n");
        return goIn();        
    }
    
    public CodeWriter end() throws IOException{
        goOut();
        return line("}\n");
    }

    private CodeWriter goIn(){
        indent += "    ";
        return this;
    }

    private CodeWriter goOut(){
        if (indent.length() >= 4){
            indent = indent.substring(0, indent.length() - 4);
        }
        return this;
    }
 
    public CodeWriter imports(Class<?>... imports) throws IOException{
        for (Class<?> cl : imports) line("import " + cl.getName() + ";");
        return this;
    }

    public CodeWriter imports(Package... imports) throws IOException {
        for (Package p : imports) line("import " + p.getName() + ".*;");
        return this;
    }
    

    public CodeWriter javadoc(String... lines) throws IOException {
        line("/**");
        for (String line : lines){
            line(" * " + line);
        }
        return line(" */");
    }

    public String join(String prefix, String suffix, Iterable<String> args) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (String arg : args){
            if (!first) builder.append(", ");
            builder.append(prefix).append(arg).append(suffix);
            first = false;
        }
        return builder.toString();
    }
    
    public String join(String prefix, String suffix, String... args) {
        return join(prefix, suffix, Arrays.asList(args));
    }

    public CodeWriter line(String... segments) throws IOException{
        append(indent);
        for (String segment : segments){
            append(segment);
        }        
        return append("\n");
    }

    public CodeWriter lines(String... lines) throws IOException{
        for (String line : lines){
            line(line);
        }
        return this;
    }
    
    public CodeWriter nl() throws IOException {
        return append("\n");        
    }

    public CodeWriter packageDecl(String packageName) throws IOException{
        return line("package " + packageName + ";");
    }

    private <T> CodeWriter params(Collection<T> parameters, Transformer<T,String> transformer) throws IOException{
        boolean first = true;
        for (T param : parameters){
            if (!first) append(", ");
            append(transformer.transform(param));
            first = false;
        }
        return this;
    }

    private JavaWriter params(String... params) throws IOException{
        for (int i = 0; i < params.length; i++){
            if (i > 0) append(", ");
            append(params[i]);
        }
        return this;
    }

    public CodeWriter privateStaticFinal(String type, String name, String value) throws IOException {
        line("private static final " + type + " " + name + " = " + value + ";");
        return nl();        
    }

    public CodeWriter protectedField(String type, String name) throws IOException {
        line("protected " + type + " " + name + ";");
        return nl();        
    }

    public CodeWriter publicFinal(String type, String name) throws IOException {
        line("public final " + type + " " + name + ";");
        return nl();        
    }

    public CodeWriter publicFinal(String type, String name, String value) throws IOException {
        line("public final " + type + " " + name + " = " + value + ";");
        return nl();        
    }

    public CodeWriter publicStaticFinal(String type, String name, String value) throws IOException {
        line("public static final " + type + " " + name + " = " + value + ";");
        return nl();        
    }

    public CodeWriter staticimports(Class<?>... imports) throws IOException{
        for (Class<?> cl : imports) line("import static " + cl.getName() + ".*;");
        return this;
    }

    public CodeWriter suppressWarnings(String type) throws IOException{
        return line("@SuppressWarnings(\"" + type +"\")");
    }


}
