package com.mysema.codegen;

import static com.mysema.codegen.Symbols.ASSIGN;
import static com.mysema.codegen.Symbols.COMMA;
import static com.mysema.codegen.Symbols.DOT;
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

import com.mysema.codegen.model.Parameter;
import com.mysema.codegen.model.Type;

/**
 * @author tiwe
 *
 */
public class ScalaWriter extends AbstractCodeWriter<ScalaWriter>{
    
    private static final String DEF = "def ";
    
    private static final String EXTENDS = " extends ";
    
    private static final String IMPLEMENTS = " implements ";

    private static final String IMPORT = "import ";

    private static final String IMPORT_STATIC = "import static ";

    private static final String PACKAGE = "package ";

    private static final String PRIVATE = "private ";
    
    private static final String PRIVATE_VAL = "private val ";
    
    private static final String PROTECTED = "protected ";
    
    private static final String PROTECTED_VAL = "protected val ";

    private static final String PUBLIC = "public ";

    private static final String PUBLIC_CLASS = "class ";

    private static final String VAL = "val ";

    private static final String TRAIT = "trait ";
    
    private final Set<String> classes = new HashSet<String>();
    
    private final Set<String> packages = new HashSet<String>();
    
    private Type type;
    
    public ScalaWriter(Appendable appendable){
        super(appendable);
        this.packages.add("java.lang");
    }

    @Override
    public ScalaWriter annotation(Annotation annotation) throws IOException {
        beginLine().append("@").appendType(annotation.annotationType());
        Method[] methods = annotation.annotationType().getDeclaredMethods();
        if (methods.length == 1 && methods[0].getName().equals("value")){
            try {
                Object value = methods[0].invoke(annotation);
                append("(");
                annotationConstant(value);
                append(")");
            } catch (IllegalArgumentException e) {
                throw new CodegenException(e);
            } catch (IllegalAccessException e) {
                throw new CodegenException(e);
            } catch (InvocationTargetException e) {
                throw new CodegenException(e);
            }            
        }else{
            boolean first = true;        
            for (Method method : methods){            
                try {
                    Object value = method.invoke(annotation);
                    if (value == null || value.equals(method.getDefaultValue())){
                        continue;
                    }else if (!first){
                        append(COMMA);
                    }else{
                        append("(");
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
            if (!first){
                append(")");    
            }        
        }        
        return nl();
    }

    @Override
    public ScalaWriter annotation(Class<? extends Annotation> annotation) throws IOException {
        return beginLine().append("@").appendType(annotation).nl();
    }
    
    @SuppressWarnings("unchecked")
    private void annotationConstant(Object value) throws IOException{
         if (value instanceof Class){
             appendType((Class)value).append(".class");             
         }else if (value instanceof Number || value instanceof Boolean){
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
    
    private ScalaWriter appendType(Class<?> type) throws IOException{
        if (classes.contains(type.getName()) || packages.contains(type.getPackage().getName())){
            append(type.getSimpleName());
        }else{
            append(type.getName());
        }
        return this;
    }

    @Override
    public ScalaWriter beginClass(Type type) throws IOException {
        return beginClass(type, null);
    }

    @Override
    public ScalaWriter beginClass(Type type, Type superClass, Type... interfaces) throws IOException {
        packages.add(type.getPackageName());
        beginLine(PUBLIC_CLASS + type.getSimpleName());
        if (superClass != null){
            append(EXTENDS + getGenericName(false, superClass));
        }
        if (interfaces.length > 0){
            append(IMPLEMENTS);
            for (int i = 0; i < interfaces.length; i++){
                if (i > 0){
                    append(COMMA);
                }
                append(getGenericName(false, interfaces[i]));
            }
        }
        append(" {").nl().nl();
        goIn();
        this.type = type;
        return this;
    }

    @Override
    public <T> ScalaWriter beginConstructor(Collection<T> parameters, Transformer<T, Parameter> transformer) throws IOException {
        beginLine(PUBLIC + type.getSimpleName()).params(parameters, transformer).append(" {").nl();
        return goIn();     
    }

    @Override
    public ScalaWriter beginConstructor(Parameter... params) throws IOException {
        beginLine(PUBLIC + type.getSimpleName()).params(params).append(" {").nl();
        return goIn();
    }

    @Override
    public ScalaWriter beginInterface(Type type, Type... interfaces)
            throws IOException {
        packages.add(type.getPackageName());
        beginLine(TRAIT + getGenericName(false, type));
        if (interfaces.length > 0){
            append(EXTENDS);
            for (int i = 0; i < interfaces.length; i++){
                if (i > 0){
                    append(COMMA);
                }
                append(getGenericName(false, interfaces[i]));
            }
        }
        append(" {").nl().nl();
        goIn();
        this.type = type;
        return this;   
    }

    private ScalaWriter beginMethod(String modifiers, Type returnType, String methodName, Parameter... args) throws IOException{
//        beginLine(modifiers + returnType.getGenericName(true, packages, classes) + SPACE + methodName).params(args).append(" {").nl();
        beginLine(modifiers + methodName).params(args).append(" {").nl();
        return goIn();
    }
    
    @Override
    public <T> ScalaWriter beginPublicMethod(Type returnType, String methodName, Collection<T> parameters, Transformer<T, Parameter> transformer) throws IOException {
        return beginMethod(DEF, returnType, methodName, transform(parameters, transformer));
    }

    @Override
    public ScalaWriter beginPublicMethod(Type returnType, String methodName, Parameter... args) throws IOException{
        return beginMethod(DEF, returnType, methodName, args);
    }
    
    @Override
    public <T> ScalaWriter beginStaticMethod(Type returnType, String methodName, Collection<T> parameters, Transformer<T, Parameter> transformer) throws IOException {
        return beginMethod(DEF, returnType, methodName, transform(parameters, transformer));
    }

    @Override
    public ScalaWriter beginStaticMethod(Type returnType, String methodName, Parameter... args) throws IOException{
        return beginMethod(DEF, returnType, methodName, args);
    }

    @Override
    public ScalaWriter end() throws IOException {
        goOut();
        return line("}").nl();
    }
    
    public ScalaWriter field(Type type, String name) throws IOException {
        return line(name + ": " + getGenericName(true, type) + SPACE + SEMICOLON).nl();
    }

    private ScalaWriter field(String modifier, Type type, String name) throws IOException{
        return line(modifier + name + ": " + getGenericName(true, type) + SEMICOLON).nl();
    }
    
    private ScalaWriter field(String modifier, Type type, String name, String value) throws IOException{
        return line(modifier + name + ": " + getGenericName(true, type) + ASSIGN + value + SEMICOLON).nl();
    }
    
    @Override
    public String getGenericName(boolean asArgType, Type type) {
        if (type.getParameters().isEmpty()){
            return getRawName(type);
        }else{
            StringBuilder builder = new StringBuilder();
            builder.append(getRawName(type));
            builder.append("[");
            boolean first = true;
            String fullName = type.getFullName();
            for (Type parameter : type.getParameters()){                
                if (!first){
                    builder.append(", ");
                }
                if (parameter == null || parameter.getFullName().equals(fullName)){
                    builder.append("?");
                }else{
                    builder.append(getGenericName(false, parameter));    
                }                
                first = false;
            }
            builder.append("]");
            return builder.toString();
        }
    }

    @Override
    public String getRawName(Type type) {
        String fullName = type.getFullName();
        String packageName = type.getPackageName();
        if (packages.contains(packageName) || "java.lang".equals(packageName) || classes.contains(fullName)){
            if (packageName.length() > 0){
                return fullName.substring(packageName.length()+1);    
            }else{
                return fullName;
            }  
        }else{
            return fullName;
        }
    }

    @Override
    public ScalaWriter imports(Class<?>... imports) throws IOException{
        for (Class<?> cl : imports){
            classes.add(cl.getName());
            line(IMPORT + cl.getName() + SEMICOLON);
        }
        nl();
        return this;
    }

    @Override
    public ScalaWriter imports(Package... imports) throws IOException {
        for (Package p : imports){
            packages.add(p.getName());
            line(IMPORT + p.getName() + "._;");
        }
        nl();
        return this;
    }
    
    @Override
    public ScalaWriter importClasses(String... imports) throws IOException{
        for (String cl : imports){
            classes.add(cl);
            line(IMPORT + cl + SEMICOLON);
        }
        nl();
        return this;
    }

    @Override
    public ScalaWriter importPackages(String... imports) throws IOException {
        for (String p : imports){
            packages.add(p);
            line(IMPORT + p + "._;");
        }
        nl();
        return this;
    }

    @Override
    public ScalaWriter javadoc(String... lines) throws IOException {
        line("/**");
        for (String line : lines){
            line(" * " + line);
        }
        return line(" */");
    }

    @Override
    public ScalaWriter packageDecl(String packageName) throws IOException {
        packages.add(packageName);
        return line(PACKAGE + packageName + SEMICOLON).nl();
    }
    
    private <T> ScalaWriter params(Collection<T> parameters, Transformer<T,Parameter> transformer) throws IOException{
        append("(");
        boolean first = true;
        for (T param : parameters){
            if (!first){
                append(COMMA);
            }
            param(transformer.transform(param));
            first = false;
        }
        append(")");
        return this;
    }
    
    private ScalaWriter params(Parameter... params) throws IOException{
        append("(");
        for (int i = 0; i < params.length; i++){
            if (i > 0){
                append(COMMA);
            }
            param(params[i]);
        }
        append(")");
        return this;
    }
    
    
    private ScalaWriter param(Parameter parameter) throws IOException{
        append(parameter.getName());
        append(": ");
        append(getGenericName(true, parameter.getType()));
        return this;
    }

    @Override
    public ScalaWriter privateField(Type type, String name) throws IOException {
        return field(PRIVATE, type, name);
    }
    
    @Override
    public ScalaWriter privateFinal(Type type, String name) throws IOException {
        return field(PRIVATE_VAL, type, name);        
    }
    
    @Override
    public ScalaWriter privateFinal(Type type, String name, String value) throws IOException {
        return field(PRIVATE_VAL, type, name, value);
    }

    @Override
    public ScalaWriter privateStaticFinal(Type type, String name, String value) throws IOException {
        return field(PRIVATE_VAL, type, name, value);
    }
        
    @Override
    public ScalaWriter protectedField(Type type, String name) throws IOException {
        return field(PROTECTED, type, name);        
    }
    
    @Override
    public ScalaWriter protectedFinal(Type type, String name) throws IOException {
        return field(PROTECTED_VAL, type, name);        
    }

    @Override
    public ScalaWriter protectedFinal(Type type, String name, String value) throws IOException {
        return field(PROTECTED_VAL, type, name, value);
    }

    @Override
    public ScalaWriter publicField(Type type, String name) throws IOException {
        return field(PUBLIC, type, name);
    }
    
    @Override
    public ScalaWriter publicFinal(Type type, String name) throws IOException {
        return field(VAL, type, name);        
    }
    
    @Override
    public ScalaWriter publicFinal(Type type, String name, String value) throws IOException {
        return field(VAL, type, name, value);
    }
    
    @Override
    public ScalaWriter publicStaticFinal(Type type, String name, String value) throws IOException {
        return field(VAL, type, name, value);
    }

    @Override
    public ScalaWriter staticimports(Class<?>... imports) throws IOException {
        for (Class<?> cl : imports){
            line(IMPORT_STATIC + cl.getName() + "._;");
        }
        return this;
    }

    @Override
    public ScalaWriter suppressWarnings(String type) throws IOException {
        return line("@SuppressWarnings(\"" + type +"\")");
    }
    
    private <T> Parameter[] transform(Collection<T> parameters, Transformer<T,Parameter> transformer){
        Parameter[] rv = new Parameter[parameters.size()];
        int i = 0; 
        for (T value : parameters){
            rv[i++] = transformer.transform(value);
        }
        return rv;
    }

}
