package com.querydsl.apt;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class NoteTest extends AbstractProcessorTest {

    private Collection<String> aptOptions;

    private ByteArrayOutputStream err = new ByteArrayOutputStream();

    private static final String packagePath = "src/test/java/com/querydsl/apt/";

    public void process() throws IOException {
        List<String> classes = getFiles(packagePath);
        process(QuerydslAnnotationProcessor.class, classes, "includedClasses");
    }

    @Override
    protected Collection<String> getAPTOptions() {
        return aptOptions;
    }

    @Override
    protected ByteArrayOutputStream getStdErr() {
        return err;
    }

    protected boolean isStdErrEmpty() {
        return getStdErr().toByteArray().length == 0;
    }

    @Test
    public void processDefault() throws IOException {
        aptOptions = Collections.emptyList();
        process();
        assertTrue(isStdErrEmpty());
    }

    @Test
    public void processEnabled() throws IOException {
        aptOptions = Arrays.asList("-Aquerydsl.logInfo=true");
        process();
        assertFalse(isStdErrEmpty());
    }

    @Test
    public void processDisabled() throws IOException {
        aptOptions = Arrays.asList("-Aquerydsl.logInfo=false");
        process();
        assertTrue(isStdErrEmpty());
    }

}
