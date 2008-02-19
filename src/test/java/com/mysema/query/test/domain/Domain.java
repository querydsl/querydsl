package com.mysema.query.test.domain;

import com.mysema.query.grammar.GrammarTypes.DomainType;
import com.mysema.query.grammar.GrammarTypes.NumberProperty;
import com.mysema.query.grammar.GrammarTypes.StringProperty;


/**
 * Domain provides
 *
 * @author tiwe
 * @version $Id$
 */
public class Domain {
    // User
    public static final User user = new User("user");
    public static final User user1 = new User("user1");
    public static final User user2 = new User("user2");
    public static final User user3 = new User("user3");
    public static final User user4 = new User("user4");
    public static final User user5 = new User("user5");
    
    // Company
    public static final Company company = new Company("company");
    public static final Company company1 = new Company("company1");
    public static final Company company2 = new Company("company2");
    public static final Company company3 = new Company("company3");
    public static final Company company4 = new Company("company4");
    public static final Company company5 = new Company("company5");
    
    // Cat
    public static final Cat cat = new Cat("cat");
    public static final Cat cat1 = new Cat("cat1");
    public static final Cat cat2 = new Cat("cat2");
    public static final Cat cat3 = new Cat("cat3");
    public static final Cat cat4 = new Cat("cat4");
    public static final Cat cat5 = new Cat("cat5");
    
    public static final Cat kitten = new Cat("kitten");
    public static final Cat child = new Cat("child");
    public static final Cat mate = new Cat("mate"); 
    
    public static class User extends DomainType<User>{
        User(String path) {super(path);}
        public final NumberProperty id = num("id");
        public final StringProperty userName = str("userName");
        public final StringProperty firstName = str("firstName");
        public final StringProperty lastName = str("lastName");
        public final Company company(){ return new Company(_path+".company");}        
    }
    
    public static class Company extends DomainType<Company>{
        Company(String path) {super(path);}
        public final NumberProperty id = num("id");
        public final StringProperty name = str("name");
    }

    public static class Cat extends DomainType<Cat>{
        Cat(String path) {super(path);}
        public final NumberProperty bodyWeight = num("bodyWeight");
        public final Cat kittens(){ return new Cat(_path+".kittens");}
        public final Cat mate(){ return new Cat(_path+".mate");}
        public final StringProperty name = str("name");
    }

}
