package com.querydsl.codegen;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import java.io.IOException;
import java.util.Collection;

/**
 * creates files with {@link javax.annotation.processing.Filer}.
 *
 * @author f43nd1r
 */
public class DefaultFiler implements Filer {
    @Override
    public Appendable createFile(ProcessingEnvironment processingEnvironment, String classname, Collection<? extends Element> elements) throws IOException {
        return processingEnvironment.getFiler().createSourceFile(classname, elements.toArray(new Element[0])).openWriter();
    }
}
