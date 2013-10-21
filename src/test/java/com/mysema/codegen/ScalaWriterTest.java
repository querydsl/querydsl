package com.mysema.codegen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.Max;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Function;
import com.mysema.codegen.model.ClassType;
import com.mysema.codegen.model.Parameter;
import com.mysema.codegen.model.SimpleType;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeCategory;
import com.mysema.codegen.model.Types;

public class ScalaWriterTest {

    private static final Function<Parameter, Parameter> transformer = new Function<Parameter, Parameter>() {
        @Override
        public Parameter apply(Parameter input) {
            return input;
        }
    };

    private final Writer w = new StringWriter();

    private final ScalaWriter writer = new ScalaWriter(w, true);

    private Type testType, testType2, testSuperType, testInterface1, testInterface2;

    @Before
    public void setUp() {
        testType = new ClassType(JavaWriterTest.class);
        testType2 = new SimpleType("com.mysema.codegen.Test", "com.mysema.codegen", "Test");
        testSuperType = new SimpleType("com.mysema.codegen.Superclass", "com.mysema.codegen",
                "Superclass");
        testInterface1 = new SimpleType("com.mysema.codegen.TestInterface1", "com.mysema.codegen",
                "TestInterface1");
        testInterface2 = new SimpleType("com.mysema.codegen.TestInterface2", "com.mysema.codegen",
                "TestInterface2");
    }

    @Test
    public void Object() throws IOException {
        writer.beginObject("Test");
        writer.end();
        assertTrue(w.toString().contains("object Test {"));
    }

    @Test
    public void Class_With_Interfaces() throws IOException {
        writer.beginClass(testType, testType2, testInterface1);
        assertTrue(w.toString().contains("class JavaWriterTest extends Test with TestInterface1 {"));
    }

    @Test
    public void Interface_With_Superinterfaces() throws IOException {
        writer.beginInterface(testType, testType2, testInterface1);
        assertTrue(w.toString().contains("trait JavaWriterTest extends Test with TestInterface1 {"));
    }

    @Test
    public void CustomHeader() throws IOException {
        // class QDepartment(path: String) extends
        // RelationalPathBase[QDepartment](classOf[QDepartment], path){
        // val id = createNumber("ID", classOf[Integer]);
        // val company = createNumber("COMPANY", classOf[Integer]);
        // val idKey = createPrimaryKey(id);
        // val companyKey: ForeignKey[QCompany] = createForeignKey(company,
        // "ID");
        // }
        writer.beginClass("QDepartment(path: String) extends RelationalPathBase[QDepartment](classOf[QDepartment], path)");
        writer.publicFinal(Types.OBJECT, "id", "createNumber(\"ID\", classOf[Integer])");
        writer.publicFinal(Types.OBJECT, "company", "createNumber(\"COMPANY\", classOf[Integer])");
        writer.publicFinal(Types.OBJECT, "idKey", "createPrimaryKey(id)");
        writer.publicFinal(Types.OBJECT, "companyKey", "createForeignKey(company,\"ID\")");
        writer.end();

        System.out.println(w);
    }

    @Test
    public void BeanAccessors() throws IOException {
        writer.beginClass(new SimpleType("Person"));
        writer.beginPublicMethod(Types.STRING, "getName");
        writer.line("\"Daniel Spiewak\"");
        writer.end();
        writer.beginPublicMethod(Types.VOID, "setName", new Parameter("name", Types.STRING));
        writer.line("//");
        writer.end();
        writer.end();

        System.out.println(w);
    }

    @Test
    public void Arrays() throws IOException {
        // def main(args: Array[String]) {
        writer.beginClass(new SimpleType("Main"));
        writer.field(Types.STRING.asArrayType(), "stringArray");
        writer.beginPublicMethod(Types.VOID, "main",
                new Parameter("args", Types.STRING.asArrayType()));
        writer.line("//");
        writer.end();
        writer.beginPublicMethod(Types.VOID, "main2", new Parameter("args", new ClassType(
                TypeCategory.ARRAY, String[].class)));
        writer.line("//");
        writer.end();
        writer.end();

        System.out.println(w);
        assertTrue(w.toString().contains("var stringArray: Array[String]"));
        assertTrue(w.toString().contains("def main(args: Array[String])"));
        assertTrue(w.toString().contains("def main2(args: Array[String])"));
    }

    @Test
    public void Arrays2() throws IOException {
        writer.field(Types.BYTE_P.asArrayType(), "byteArray");

        System.out.println(w);
        assertTrue(w.toString().contains("var byteArray: Array[Byte]"));
    }

    @Test
    public void Trait() throws IOException {
        // trait MyTrait
        writer.beginInterface(new SimpleType("MyTrait"));
        writer.line("//");
        writer.end();

        System.out.println(w);
        assertTrue(w.toString().contains("trait MyTrait"));
    }

    @Test
    public void Field() throws IOException {
        // private val people: List[Person]
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
    public void Extends() throws IOException {
        writer.beginClass(testType2, testSuperType);
        writer.end();

        System.out.println(w);
    }

    @Test
    public void Implements() throws IOException {
        writer.beginClass(testType2, null, testInterface1, testInterface2);
        writer.end();

        System.out.println(w);
    }

    @Test
    public void Interface() throws IOException {
        writer.packageDecl("com.mysema.codegen");
        writer.imports(IOException.class, StringWriter.class, Test.class);
        writer.beginInterface(testType);
        writer.end();

        System.out.println(w);
    }

    @Test
    public void Interface2() throws IOException {
        writer.beginInterface(testType2, testInterface1);
        writer.end();

        System.out.println(w);
    }

    @Test
    public void Javadoc() throws IOException {
        writer.packageDecl("com.mysema.codegen");
        writer.imports(IOException.class, StringWriter.class, Test.class);
        writer.javadoc("JavaWriterTest is a test class");
        writer.beginClass(testType);
        writer.end();

        System.out.println(w);
    }

    @Test
    public void AnnotationConstant() throws IOException {
        Max annotation = new MaxImpl(0l) {
            @Override
            public Class<?>[] groups() {
                return new Class[] { Object.class, String.class, int.class };
            }
        };
        writer.annotation(annotation);

        System.out.println(w);
    }

    @Test
    public void Annotation_With_ArrayMethod() throws IOException {
        Target annotation = new Target() {
            @Override
            public ElementType[] value() {
                return new ElementType[] { ElementType.FIELD, ElementType.METHOD };
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return Target.class;
            }
        };

        writer.imports(Target.class.getPackage());
        writer.annotation(annotation);
        assertTrue(w.toString().contains("@Target(Array(FIELD, METHOD))"));
    }

    @Test
    public void Annotations() throws IOException {
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
    public void Annotations2() throws IOException {
        writer.packageDecl("com.mysema.codegen");
        writer.imports(IOException.class.getPackage(), StringWriter.class.getPackage());
        writer.annotation(Entity.class);
        writer.beginClass(testType);
        writer.annotation(new Test() {
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
            }
        });
        writer.beginPublicMethod(Types.VOID, "test");
        writer.end();
        writer.end();

        System.out.println(w);
    }

    @Test
    public void Fields() throws IOException {
        writer.beginClass(testType);
        // private
        writer.privateField(Types.STRING, "privateField");
        writer.privateStaticFinal(Types.STRING, "privateStaticFinal", "\"val\"");
        // protected
        writer.protectedField(Types.STRING, "protectedField");
        // field
        writer.field(Types.STRING, "field");
        // public
        writer.publicField(Types.STRING, "publicField");
        writer.publicStaticFinal(Types.STRING, "publicStaticFinal", "\"val\"");
        writer.publicFinal(Types.STRING, "publicFinalField");
        writer.publicFinal(Types.STRING, "publicFinalField2", "\"val\"");

        writer.end();

        System.out.println(w);
    }

    @Test
    public void Methods() throws IOException {
        writer.beginClass(testType);
        // private

        // protected

        // method

        // public
        writer.beginPublicMethod(Types.STRING, "publicMethod",
                Arrays.asList(new Parameter("a", Types.STRING)), transformer);
        writer.line("return null;");
        writer.end();

        writer.beginStaticMethod(Types.STRING, "staticMethod",
                Arrays.asList(new Parameter("a", Types.STRING)), transformer);
        writer.line("return null;");
        writer.end();

        writer.end();

        System.out.println(w);
    }

    @Test
    public void Constructors() throws IOException {
        writer.beginClass(testType);

        writer.beginConstructor(
                Arrays.asList(new Parameter("a", Types.STRING), new Parameter("b", Types.STRING)),
                transformer);
        writer.end();

        writer.beginConstructor(new Parameter("a", Types.STRING));
        writer.end();

        writer.end();

        System.out.println(w);

    }

    @Test
    public void CaseClass() throws IOException {
        writer.caseClass("TestType", new Parameter("a", Types.STRING), new Parameter("b",
                Types.STRING));

        System.out.println(w);
        assertEquals("case class TestType(a: String, b: String)\n", w.toString());
    }

    @Test
    public void ClassConstants() {
        assertEquals("classOf[SomeClass]", writer.getClassConstant("SomeClass"));
    }

    @Test
    public void Primitive() throws IOException {
        writer.beginClass(testType);

        writer.beginConstructor(new Parameter("a", Types.INT));
        writer.end();

        writer.end();

        System.out.println(w);

        assertTrue(w.toString().contains("def this(a: Int) {"));
    }

    @Test
    public void Primitive_Types() throws IOException {
        writer.field(Types.BOOLEAN_P, "field");
        writer.field(Types.BYTE_P, "field");
        writer.field(Types.CHAR, "field");
        writer.field(Types.INT, "field");
        writer.field(Types.LONG_P, "field");
        writer.field(Types.SHORT_P, "field");
        writer.field(Types.DOUBLE_P, "field");
        writer.field(Types.FLOAT_P, "field");

        for (String type : Arrays.asList("boolean", "byte", "char", "int", "long", "short",
                "double", "float")) {
            assertTrue(w.toString().contains("field: " + StringUtils.capitalize(type)));
        }
    }

    @Test
    public void ReservedWords() throws IOException {
        writer.beginClass(testType);

        writer.beginConstructor(new Parameter("type", Types.INT));
        writer.end();

        writer.publicField(testType, "class");

        writer.beginPublicMethod(testType, "var");
        writer.end();

        writer.end();

        System.out.println(w);

        assertTrue(w.toString().contains("`type`: Int"));
        assertTrue(w.toString().contains("`class`: JavaWriterTest"));
        assertTrue(w.toString().contains("`var`(): JavaWriterTest"));
    }
}
