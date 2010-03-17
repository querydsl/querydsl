package com.mysema.query.search;

import org.hibernate.Session;
import org.junit.Test;

import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PathBuilder;


public class SimpleTest {

    @Test
    public void specs(){
        Session session = null;
        PathBuilder<Object> entityPath = new PathBuilder<Object>(Object.class, "obj");
        PString stringPath = entityPath.getString("prop");
        
//        LuceneQuery<Object> query = new LuceneQuery<Object>(session, entityPath);
        
//        Terms & Phrases
//        "test" or "hello dolly"
//        c.Match("test") or c.Match("hello dolly")
        stringPath.like("test").or(stringPath.like("hello dolly"));
        
//        Fields
//        title:"The Right way" and text:go
//        c.Title == "The Right way" and c.Text == "go"
        stringPath.eq("The Right Way").and(stringPath.eq("go"));
        
//        WildCard
//        amb?r
//        c.ContactName.Match("amb?r")
        stringPath.like("amb?r");
        
//        Prefix
//        amber*
//        c.ContactName.StartsWith("amber")
        stringPath.startsWith("amber");
        
//        Fuzzy
//        roam~ or roam~0.8
//        c.ContactName.Like("roam") or c.ContactName.Like("roam", 0.8)
        // TODO
        
//        Proximity
//        "jakarta apache"~10
//        c.ContactName.Like("jakarta apache", 10)
        // TODO
        
//        Inclusive Range
//        mod_date:[20020101 TO 20030101]
//        c.ModifiedDate.Includes("20020101", "20030101")
//        stringPath.between("20020101", "20030101");
        // TODO
        
//        Exclusive Range
//        title:{Aida TO Carmen}
//        c.Title.Between("Aida", "Carmen")
        stringPath.between("Aida", "Carmen");
        
//        Boosting
//        jakarta^4 apache
//        c.Title.Match("jakarta".Boost(4), apache)
        // TODO
        
//        Boolean Or
//        "jakarta apache" OR jakarta
//        where c.Match("jakarta apache") || c.Match("jakarta")
        stringPath.like("jakarta apache").or(stringPath.like("jakarta"));
        
//        Boolean And
//        "jakarta apache" AND "Apache Lucene"
//        where c.Match("jakarta apache") && c.Match("Apache Lucene")
        stringPath.like("jakarta apache").and(stringPath.like("Apache Lucene"));
        
//        Boolean Not
//        "jakarta apache" NOT "Apache Lucene"
//        where c.Match("jakarta apache") && !c.Match("Apache Lucene")
        stringPath.like("jakarta apache").and(stringPath.like("Apache Lucene").not());
        
//        Required
//        +jakarta lucene
//        c.Title.Match("jakarta".Require(), "lucene")
//         TODO
        
//        Grouping
//        (jakarta OR apache) AND website
//        where (c.Title == "jakarta" || c.Title == "apache") && (c.Title == "website")
        stringPath.eq("jakarta").or(stringPath.eq("apache")).and(stringPath.eq("website"));
        
//        Native syntax
//        ie. title:{+return +"pink panther")
//        c.Search("title:(return +\"pink panther\"")
//        TODO
    }
    
}
