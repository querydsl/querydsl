/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.lucene3;

import static com.querydsl.lucene3.QPerson.person;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.path.StringPath;

public class LuceneSerializerNotTokenizedTest {
    private RAMDirectory idx;
    private IndexWriter writer;
    private IndexSearcher searcher;
    private LuceneSerializer serializer;

    private final QueryMetadata metadata = new DefaultQueryMetadata();

    private final Person clooney = new Person("actor_1", "George Clooney", new LocalDate(1961, 4, 6));
    private final Person pitt = new Person("actor_2", "Brad Pitt", new LocalDate(1963, 12, 18));

    private void testQuery(Expression<?> expr, String expectedQuery, int expectedHits) throws Exception {
        Query query = serializer.toQuery(expr, metadata);
        TopDocs docs = searcher.search(query, 100);
        assertEquals(expectedHits, docs.totalHits);
        assertEquals(expectedQuery, query.toString());
    }

    private Document createDocument(Person person) {
        Document doc = new Document();
        doc.add(new Field("id", person.getId(), Store.YES, Index.NOT_ANALYZED));
        doc.add(new Field("name", person.getName(), Store.YES, Index.NOT_ANALYZED));
        doc.add(new Field("birthDate", person.getBirthDate().toString(), Store.YES, Index.NOT_ANALYZED));
        return doc;
    }

    @Before
    public void Before() throws Exception {
        serializer = new LuceneSerializer(false, false);
        idx = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_31, 
                new StandardAnalyzer(Version.LUCENE_30))
            .setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        writer = new IndexWriter(idx, config);

        writer.addDocument(createDocument(clooney));
        writer.addDocument(createDocument(pitt));

        Document document = new Document();
        for (String movie : Arrays.asList("Interview with the Vampire",
                                          "Up in the Air")) {
            document.add(new Field("movie", movie, Store.YES, Index.NOT_ANALYZED));
        }
        writer.addDocument(document);

        writer.close();

        IndexReader reader = IndexReader.open(idx);
        searcher = new IndexSearcher(reader);
    }

    @Test
    public void Equals_By_Id_Matches() throws Exception {
        testQuery(person.id.eq("actor_1"), "id:actor_1", 1);
    }

    @Test
    public void Equals_By_Id_Does_Not_Match() throws Exception {
        testQuery(person.id.eq("actor_8"), "id:actor_8", 0);
    }

    @Test
    public void Equals_By_Name_Matches() throws Exception {
        testQuery(person.name.eq("George Clooney"), "name:George Clooney", 1);
    }

    @Test(expected=UnsupportedOperationException.class)
    public void Equals_By_Name_Ignoring_Case_Does_Not_Match() throws Exception {
        testQuery(person.name.equalsIgnoreCase("george clooney"), "name:george clooney", 0);
    }

    @Test
    public void Equals_By_Name_Does_Not_Match() throws Exception {
        testQuery(person.name.eq("George Looney"), "name:George Looney", 0);
    }

    @Test
    public void Starts_With_Name_Should_Match() throws Exception {
        testQuery(person.name.startsWith("George C"), "name:George C*", 1);
    }

    @Test
    public void Starts_With_Name_Should_Not_Match() throws Exception {
        testQuery(person.name.startsWith("George L"), "name:George L*", 0);
    }

    @Test
    public void Ends_With_Name_Should_Match() throws Exception {
        testQuery(person.name.endsWith("e Clooney"), "name:*e Clooney", 1);
    }

    @Test
    public void Ends_With_Name_Should_Not_Match() throws Exception {
        testQuery(person.name.endsWith("e Looney"), "name:*e Looney", 0);
    }

    @Test
    public void Contains_Name_Should_Match() throws Exception {
        testQuery(person.name.contains("oney"), "name:*oney*", 1);
    }

    @Test
    public void Contains_Name_Should_Not_Match() throws Exception {
        testQuery(person.name.contains("bloney"), "name:*bloney*", 0);
    }

    @Test
    public void In_Names_Should_Match_2() throws Exception {
        testQuery(person.name.in("Brad Pitt", "George Clooney"), "name:Brad Pitt name:George Clooney", 2);
    }

    @Test
    public void Or_By_Name_Should_Match_2() throws Exception {
        testQuery(    person.name.eq("Brad Pitt")
                  .or(person.name.eq("George Clooney")), "name:Brad Pitt name:George Clooney", 2);
    }

    @Test
    public void Equals_By_Birth_Date() throws Exception {
        testQuery(person.birthDate.eq(clooney.getBirthDate()), "birthDate:1961-04-06", 1);
    }

    @Test
    public void Between_Phrase() throws Exception {
        testQuery(person.name.between("Brad Pitt","George Clooney"), "name:[Brad Pitt TO George Clooney]", 2);
    }

    @Test
    public void Not_Equals_Finds_The_Actors_And_Movies() throws Exception {
        testQuery(person.name.ne("Michael Douglas"), "-name:Michael Douglas +*:*", 3);
    }

    @Test
    public void Not_Equals_Finds_Only_Clooney_And_Movies() throws Exception {
        testQuery(person.name.ne("Brad Pitt"), "-name:Brad Pitt +*:*", 2);
    }

    @Test
    public void And_With_Two_Not_Equals_Doesnt_Find_The_Actors() throws Exception {
        testQuery(     person.name.ne("Brad Pitt")
                  .and(person.name.ne("George Clooney")), "+(-name:Brad Pitt +*:*) +(-name:George Clooney +*:*)", 1);
    }

    @Test
    public void Or_With_Two_Not_Equals_Finds_Movies_And_Actors() throws Exception {
        testQuery(    person.name.ne("Brad Pitt")
                  .or(person.name.ne("George Clooney")), "(-name:Brad Pitt +*:*) (-name:George Clooney +*:*)", 3);
    }

    @Test
    public void Negation_Of_Equals_Finds_Movies_And_Actors() throws Exception {
        testQuery(person.name.eq("Michael Douglas").not(), "-name:Michael Douglas +*:*", 3);
    }

    @Test
    public void Negation_Of_Equals_Finds_Pitt_And_Movies() throws Exception {
        testQuery(person.name.eq("Brad Pitt").not(), "-name:Brad Pitt +*:*", 2);
    }

    @Test
    public void Multiple_Field_Search_From_Movies() throws Exception {
        StringPath movie = new StringPath("movie");
        testQuery(movie.in("Interview with the Vampire"), "movie:Interview with the Vampire", 1);
        testQuery(movie.eq("Up in the Air"), "movie:Up in the Air", 1);
    }
}
