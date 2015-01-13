package com.querydsl.apt;

import javax.persistence.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.collect.ForwardingSet;
import com.google.common.io.Files;
import com.querydsl.apt.hibernate.HibernateAnnotationProcessor;
import com.querydsl.codegen.GenericExporter;
import com.querydsl.codegen.Keywords;
import com.querydsl.apt.domain.AbstractEntityTest;
import com.querydsl.apt.domain.CustomCollection;
import com.querydsl.apt.domain.Generic2Test;
import org.junit.Test;
import static org.junit.Assert.fail;

public class GenericExporterTest extends AbstractProcessorTest {

    private static final String PACKAGE_PATH = "src/test/java/com/mysema/querydsl/domain/";

    private static final List<String> CLASSES = getFiles(PACKAGE_PATH);

    @Test
    public void Execute() throws IOException {
        // via APT
        process(QuerydslAnnotationProcessor.class, CLASSES, "QuerydslAnnotationProcessor");

        // via GenericExporter
        GenericExporter exporter = new GenericExporter();
        exporter.setTargetFolder(new File("target/GenericExporterTest"));
        exporter.export(AbstractEntityTest.class.getPackage());

        List<String> expected = new ArrayList<String>();
        // delegates are not supported
        expected.add("QDelegateTest_SimpleUser.java");
        expected.add("QDelegateTest_SimpleUser2.java");
        expected.add("QDelegateTest_User.java");
        expected.add("QDelegate2Test_Entity.java");
        expected.add("QExampleEntity.java");

        expected.add("QQueryProjectionTest_DTOWithProjection.java");
        expected.add("QQueryProjectionTest_EntityWithProjection.java");
        expected.add("QEmbeddable3Test_EmbeddableClass.java");

        // FIXME
        expected.add("QExternalEntityTest_MyEntity.java");
        expected.add("QQueryEmbedded4Test_User.java");

        execute(expected, "GenericExporterTest", "QuerydslAnnotationProcessor");
    }

    @Test
    public void Execute2() throws IOException {
        // via APT
        process(HibernateAnnotationProcessor.class, CLASSES, "HibernateAnnotationProcessor");

        // via GenericExporter
        GenericExporter exporter = new GenericExporter();
        exporter.setKeywords(Keywords.JPA);
        exporter.setEntityAnnotation(Entity.class);
        exporter.setEmbeddableAnnotation(Embeddable.class);
        exporter.setEmbeddedAnnotation(Embedded.class);
        exporter.setSupertypeAnnotation(MappedSuperclass.class);
        exporter.setSkipAnnotation(Transient.class);
        exporter.setTargetFolder(new File("target/GenericExporterTest2"));
        exporter.addStopClass(ForwardingSet.class);
        exporter.setStrictMode(true);
        exporter.export(AbstractEntityTest.class.getPackage());

        List<String> expected = new ArrayList<String>();
        // GenericExporter doesn't include field/method selection
        expected.add("QFileAttachment.java");
        expected.add("QJodaTest_BaseEntity.java");
        expected.add("QEnum3Test_Entity1.java");
        expected.add("QCustomCollection_MyCustomCollection.java");
        expected.add("QCustomCollection_MyCustomCollection2.java");

        expected.add("QTemporalTest_MyEntity.java");

        expected.add("QTemporal2Test_Cheque.java");
        expected.add("QQueryProjectionTest_DTOWithProjection.java");
        expected.add("QQueryProjectionTest_EntityWithProjection.java");
        expected.add("QEmbeddable3Test_EmbeddableClass.java");

        // FIXME
        expected.add("QGeneric4Test_HidaBez.java");
        expected.add("QGeneric4Test_HidaBezGruppe.java");
        expected.add("QInterfaceType2Test_UserImpl.java");
        expected.add("QOrderTest_Order.java");
        expected.add("QManagedEmailTest_ManagedEmails.java");
        expected.add("QGeneric12Test_ChannelRole.java");
        expected.add("QManyToManyTest_Person.java");
        expected.add("QOneToOneTest_Person.java");
        expected.add("QGeneric16Test_HidaBez.java");
        expected.add("QGeneric16Test_HidaBezGruppe.java");
        expected.add("QProperties2Test_ConcreteX.java");

        execute(expected, "GenericExporterTest2", "HibernateAnnotationProcessor");
    }

    @Test
    public void Execute3() {
        GenericExporter exporter = new GenericExporter();
        exporter.setKeywords(Keywords.JPA);
        exporter.setEntityAnnotation(Entity.class);
        exporter.setEmbeddableAnnotation(Embeddable.class);
        exporter.setEmbeddedAnnotation(Embedded.class);
        exporter.setSupertypeAnnotation(MappedSuperclass.class);
        exporter.setSkipAnnotation(Transient.class);
        exporter.setTargetFolder(new File("target/GenericExporterTest3"));
        //exporter.addStopClass(ForwardingSet.class);
        exporter.export(CustomCollection.MyCustomCollection.class,
                        CustomCollection.MyCustomCollection2.class,
                        CustomCollection.MyEntity.class);
    }

    @Test
    public void Execute4() throws IOException {
        GenericExporter exporter = new GenericExporter();
        exporter.setKeywords(Keywords.JPA);
        exporter.setEntityAnnotation(Entity.class);
        exporter.setEmbeddableAnnotation(Embeddable.class);
        exporter.setEmbeddedAnnotation(Embedded.class);
        exporter.setSupertypeAnnotation(MappedSuperclass.class);
        exporter.setSkipAnnotation(Transient.class);
        exporter.setTargetFolder(new File("target/GenericExporterTest4"));
        exporter.addStopClass(ForwardingSet.class);
        exporter.export(Generic2Test.class.getClasses());
    }

    private void execute(List<String> expected, String genericExporterFolder, String aptFolder) throws IOException {
        List<String> failures = new ArrayList<String>();
        int successes = 0;
        for (File file : new File("target/"+genericExporterFolder+"/com/mysema/query/domain").listFiles()) {
            File other = new File("target/"+aptFolder+"/com/mysema/query/domain", file.getName());
            if (!other.exists() || !other.isFile()) continue;
            String result1 = Files.toString(file, Charsets.UTF_8);
            String result2 = Files.toString(other, Charsets.UTF_8);
            if (!result1.equals(result2)) {
                if (!expected.contains(file.getName())) {
                    System.err.println(file.getName());
                    failures.add(file.getName());
                }
            } else {
                successes++;
            }
        }
        if (!failures.isEmpty()) {
            for (String failure : failures) {
                System.err.println(failure);
            }
            fail("Failed with " + failures.size() + " failures, " + successes + " succeeded, " + failures);
        }
    }

}
