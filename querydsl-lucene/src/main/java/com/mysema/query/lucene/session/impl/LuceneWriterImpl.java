package com.mysema.query.lucene.session.impl;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.query.QueryException;
import com.mysema.query.lucene.session.LuceneWriter;

public class LuceneWriterImpl implements LuceneWriter {

    private static final Logger logger = LoggerFactory.getLogger(LuceneWriterImpl.class);

    private IndexWriter writer;

    private final ReleaseListener releaseListener;

    public LuceneWriterImpl(Directory directory, boolean createNew, ReleaseListener releaseListener) {
        try {
            if (createNew == false) {
                try {
                    writer =
                        new IndexWriter(directory, new StandardAnalyzer(Version.LUCENE_30), false,
                                        MaxFieldLength.LIMITED);

                } catch (FileNotFoundException e) {
                    // Convience to create a new index if it's not already
                    // existing
                    createNew = true;
                }
            }
            if (createNew == true) {
                writer =
                    new IndexWriter(directory, new StandardAnalyzer(Version.LUCENE_30), true,
                                    MaxFieldLength.LIMITED);
            }

        } catch (IOException e) {
            throw new QueryException(e);
        }
        this.releaseListener = releaseListener;
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

    public void close() {
        if (releaseListener != null) {
            releaseListener.close(this);
        }
        Directory directory = writer.getDirectory();
        try {
            // TODO What would be best way to control this?
            writer.optimize();
            writer.close();
        } catch (IOException e) {
            logger.error("Writer close failed", e);
            try {
                if (IndexWriter.isLocked(directory)) {
                    IndexWriter.unlock(directory);
                }
            } catch (IOException e1) {
                logger.error("Lock release failed", e1);
                throw new QueryException(e1);
            }
            throw new QueryException(e);
        }
    }

    public IndexWriter getIndexWriter() {
        return writer;
    }

}
