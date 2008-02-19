package com.mysema.query.test;

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
    public static final User user = new User("user");
    public static final User user1 = new User("user1");
    public static final User user2 = new User("user2");
    
    public static final Company company = new Company("company");
    public static final Company company1 = new Company("company1");
    public static final Company company2 = new Company("company2");
    
    public static final Cat cat = new Cat("cat");
    public static final Cat cat1 = new Cat("cat1");
    public static final Cat cat2 = new Cat("cat2");
    
    public static final Cat kitten = new Cat("kitten");
    
    public static class User extends DomainType<User>{
        User(String path) {super(path);}
        public final NumberProperty id = numberProp("id");
        public final StringProperty userName = stringProp("userName");
        public final StringProperty firstName = stringProp("firstName");
        public final StringProperty lastName = stringProp("lastName");
        public final Company company = new Company(_path+".company");        
    }
    
    public static class Company extends DomainType<Company>{
        Company(String path) {super(path);}
        public final NumberProperty id = numberProp("id");
        public final StringProperty name = stringProp("name");
    }

    public static class Cat extends DomainType<Cat>{
        Cat(String path) {super(path);}
        public final NumberProperty bodyWeight = numberProp("bodyWeight");
        public final Cat kittens = new Cat(_path+".kittens");
        public final Cat mate = new Cat(_path+".mate");
    }

}
