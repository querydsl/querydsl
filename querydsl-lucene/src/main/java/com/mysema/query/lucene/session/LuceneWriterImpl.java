package com.mysema.query.lucene.session;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;

import com.mysema.query.QueryException;
import com.mysema.query.lucene.LuceneWriter;

public class LuceneWriterImpl implements LuceneWriter {

    private IndexWriter writer;

    public LuceneWriterImpl(IndexWriter writer) {
        this.writer = writer;
    }

    @Override
    public LuceneWriter addDocument(Document doc) {
        try {
            writer.addDocument(doc);
            return this;
        } catch (IOException e) {
            throw new QueryException(e);
        }
    }

    @Override
    public LuceneWriter deleteDocuments(Term term) {
        try {
            writer.deleteDocuments(term);
            return this;
        } catch (IOException e) {
            throw new QueryException(e);
        }
    }

    public void commit() {
        try {
            writer.commit();
        } catch (IOException e) {
            throw new QueryException(e);
        }
    }

    public IndexWriter getIndexWriter() {
        return writer;
    }

}
