/**
 * 
 */
package com.mysema.query.lucene.session;

import org.apache.lucene.document.Document;

import com.mysema.query.types.PathMetadataFactory;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;

public class QDocument extends EntityPathBase<Document> {

    private static final long serialVersionUID = -4872833626508344081L;

    public QDocument(final String var) {
        super(Document.class, PathMetadataFactory.forVariable(var));
    }

    public final NumberPath<Integer> year = createNumber("year", Integer.class);

    public final StringPath title = createString("title");

    public final NumberPath<Double> gross = createNumber("gross", Double.class);
}