package com.mysema.query.domain.p9;

import javax.persistence.Entity;

@Entity
public class Article {

    String name;

    Content content;

    Person author;

}
