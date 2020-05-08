package com.querydsl.codegen;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import java.io.IOException;
import java.util.Collection;

/**
 * provides files in annotation processing to write classes to
 *
 * @author f43nd1r
 */
public interface Filer {
    Appendable createFile(ProcessingEnvironment processingEnvironment, String classname, Collection<? extends Element> elements) throws IOException;
}
