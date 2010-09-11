package com.mysema.codegen;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections15.Transformer;
import org.junit.Before;
import org.junit.Test;

import com.mysema.codegen.model.ClassType;
import com.mysema.codegen.model.Parameter;
import com.mysema.codegen.model.SimpleType;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.Types;

public class ScalaWriterTest {
    
    private static final Transformer<Parameter,Parameter> transformer = new Transformer<Parameter,Parameter>(){
        @Override
        public Parameter transform(Parameter input) {
            return input;
        }       
    };
    
    private Writer w = new StringWriter();
    
    private ScalaWriter writer = new ScalaWriter(w);
    
    private Type testType, testType2, testSuperType, testInterface1, testInterface2;
    
    @Before
    public void setUp(){
        testType = new ClassType(JavaWriterTest.class);
        testType2 = new SimpleType("com.mysema.codegen.Test","com.mysema.codegen","Test");
        testSuperType = new SimpleType("com.mysema.codegen.Superclass","com.mysema.codegen","Superclass");
        testInterface1 = new SimpleType("com.mysema.codegen.TestInterface1","com.mysema.codegen","TestInterface1");
        testInterface2 = new SimpleType("com.mysema.codegen.TestInterface2","com.mysema.codegen","TestInterface2");
    }
    
    @Test
    public void beanAccessors() throws IOException{
        writer.beginClass(new SimpleType("Person"));        
        writer.beginPublicMethod(Types.STRING, "getName");
        writer.line("\"Daniel Spiewak\"");
        writer.end();        
        writer.beginPublicMethod(Types.VOID, "setName", new Parameter("name",Types.STRING));
        writer.line("//");
        writer.end();        
        writer.end();
        
        System.out.println(w);
    }
    
    @Test
    public void arrays() throws IOException{
//        def main(args: Array[String]) {
        writer.beginClass(new SimpleType("Main"));
        writer.field(Types.STRING.asArrayType(), "stringArray");
        writer.beginPublicMethod(Types.VOID, "main", new Parameter("args",Types.STRING.asArrayType()));
        writer.line("//");
        writer.end();        
        writer.end();
        
        System.out.println(w);
        assertTrue(w.toString().contains("var stringArray: Array[String];"));
        assertTrue(w.toString().contains("def main(args: Array[String])"));
    }
    
    @Test
    public void trait() throws IOException{
        // trait MyTrait
        writer.beginInterface(new SimpleType("MyTrait"));
        writer.line("//");
        writer.end();
        
        System.out.println(w);
        assertTrue(w.toString().contains("trait MyTrait"));
    }
    
    @Test
    public void fields() throws IOException{
//        private val people: List[Person]
        writer.imports(List.class);
        writer.beginClass(new SimpleType("Main"));
        writer.privateFinal(new SimpleType(Types.LIST, new SimpleType("Person")), "people");
        writer.end();
        
        System.out.println(w);
        assertTrue(w.toString().contains("private val people: List[Person]"));
    }
    
    @Test
    public void Basic() throws IOException {        
        writer.packageDecl("com.mysema.codegen");
        writer.imports(IOException.class, StringWriter.class, Test.class);
        writer.beginClass(testType);
        writer.annotation(Test.class);
        writer.beginPublicMethod(Types.VOID, "test");
        writer.line("// TODO");
        writer.end();
        writer.end();
        
        System.out.println(w);
    }
    
    @Test
    public void Extends() throws IOException{
        writer.beginClass(testType2, testSuperType);
        writer.end();
        
        System.out.println(w);
    }
    
    @Test
    public void Implements() throws IOException{
        writer.beginClass(testType2, null, testInterface1,testInterface2);
        writer.end();
        
        System.out.println(w);
    }
    
    @Test
    public void Interface() throws IOException{
        writer.packageDecl("com.mysema.codegen");
        writer.imports(IOException.class, StringWriter.class, Test.class);
        writer.beginInterface(testType);
        writer.end();
        
        System.out.println(w);
    }
    
    @Test
    public void Interface2() throws IOException{
        writer.beginInterface(testType2, testInterface1);
        writer.end();
        
        System.out.println(w);
    }
    
    @Test
    public void Javadoc() throws IOException{
        writer.packageDecl("com.mysema.codegen");
        writer.imports(IOException.class, StringWriter.class, Test.class);
        writer.javadoc("JavaWriterTest is a test class");
        writer.beginClass(testType);
        writer.end();
                
        System.out.println(w);
    }

    
    @Test
    public void Annotations() throws IOException{
        writer.packageDecl("com.mysema.codegen");
        writer.imports(IOException.class, StringWriter.class);
        writer.annotation(Entity.class);
        writer.beginClass(testType);
        writer.annotation(Test.class);
        writer.beginPublicMethod(Types.VOID, "test");
        writer.end();
        writer.end();
                
        System.out.println(w);
    }
    
    @Test
    public void Annotations2() throws IOException{
        writer.packageDecl("com.mysema.codegen");
        writer.imports(IOException.class.getPackage(), StringWriter.class.getPackage());
        writer.annotation(Entity.class);
        writer.beginClass(testType);
        writer.annotation(new Test(){
            @Override
            public Class<? extends Throwable> expected() {
                // TODO Auto-generated method stub
                return null;
            }
            @Override
            public long timeout() {

                return 0;
            }
            @Override
            public Class<? extends Annotation> annotationType() {
                return Test.class;
            }});
        writer.beginPublicMethod(Types.VOID, "test");
        writer.end();
        writer.end();
                
        System.out.println(w);
    }
    
    @Test
    public void Fields() throws IOException{
        writer.beginClass(testType);
        // private
        writer.privateField(Types.STRING, "privateField");
        writer.privateStaticFinal(Types.STRING, "privateStaticFinal", "\"val\"");
        // protected
        writer.protectedField(Types.STRING,"protectedField");
        // field
        writer.field(Types.STRING,"field");
        // public
        writer.publicField(Types.STRING,"publicField");
        writer.publicStaticFinal(Types.STRING, "publicStaticFinal", "\"val\"");
        writer.publicFinal(Types.STRING, "publicFinalField");
        writer.publicFinal(Types.STRING, "publicFinalField2", "\"val\"");
        
        writer.end();
        
        System.out.println(w);
    }
    
    @Test
    public void Methods() throws IOException{
        writer.beginClass(testType);
        // private
        
        // protected
        
        // method
        
        // public
        writer.beginPublicMethod(Types.STRING, "publicMethod", Arrays.asList(new Parameter("a", Types.STRING)), transformer);
        writer.line("return null;");
        writer.end();
        
        writer.beginStaticMethod(Types.STRING, "staticMethod", Arrays.asList(new Parameter("a", Types.STRING)), transformer);
        writer.line("return null;");
        writer.end();
        
        writer.end();
        
        System.out.println(w);
    }
    
    @Test
    public void Constructors() throws IOException{
        writer.beginClass(testType);
        
        writer.beginConstructor(Arrays.asList(new Parameter("a", Types.STRING), new Parameter("b", Types.STRING)), transformer);
        writer.end();
        
        writer.beginConstructor(new Parameter("a", Types.STRING));
        writer.end();
        
        writer.end();
        
        System.out.println(w);
        
    }

}
