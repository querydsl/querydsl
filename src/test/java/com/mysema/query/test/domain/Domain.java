package com.mysema.query.test.domain;

import com.mysema.query.grammar.Types.DomainType;
import com.mysema.query.grammar.Types.NumberProperty;
import com.mysema.query.grammar.Types.StringProperty;


/**
 * Domain provides
 *
 * @author tiwe
 * @version $Id$
 */
public class Domain {
   
    // Cat
    public static final qCat cat = new qCat("cat");
    public static final qCat cat1 = new qCat("cat1");
    public static final qCat cat2 = new qCat("cat2");
    public static final qCat cat3 = new qCat("cat3");
    public static final qCat cat4 = new qCat("cat4");
    public static final qCat cat5 = new qCat("cat5");
    
    public static final qCat kitten = new qCat("kitten");
    public static final qCat child = new qCat("child");
    public static final qCat mate = new qCat("mate"); 
    
    // Company
    public static final qCompany company = new qCompany("company");
    public static final qCompany company1 = new qCompany("company1");
    public static final qCompany company2 = new qCompany("company2");
    public static final qCompany company3 = new qCompany("company3");
    public static final qCompany company4 = new qCompany("company4");
    public static final qCompany company5 = new qCompany("company5");
    
    // Customer
    public static final qCustomer cust = new qCustomer("cust");
    
    // Document
    public static final qDocument doc = new qDocument("doc");
    
    // User
    public static final qUser user = new qUser("user");
    public static final qUser user1 = new qUser("user1");
    public static final qUser user2 = new qUser("user2");
    public static final qUser user3 = new qUser("user3");
    public static final qUser user4 = new qUser("user4");
    public static final qUser user5 = new qUser("user5");
        
    // type declarations
    
    public static class qCat extends DomainType<Cat>{
        qCat(String path) {super(path);}
        qCat(DomainType<?> type, String path) {super(type,path);}
        private qCat kittens, mate;
        public final NumberProperty bodyWeight = num("bodyWeight");
        public final StringProperty name = str("name");
        public final qCat kittens(){
            if (kittens == null) kittens = new qCat(this,"kittens"); 
            return kittens;
        }
        public final qCat mate(){
            if (mate == null) mate = new qCat(this,"mate");
            return mate;
        }        
    }
    
    public static class qCustomer extends DomainType<Customer>{
        qCustomer(String path) {super(path);}
        qCustomer(DomainType<?> type, String path) {super(type,path);}
        private qName name;
        public final qName name(){
            if (name == null) name = new qName(this, "name");
            return name;
        }
    }
    
    public static class qCompany extends DomainType<Company>{
        qCompany(String path) {super(path);}
        qCompany(DomainType<?> type, String path) {super(type,path);}
        public final NumberProperty id = num("id");
        public final StringProperty name = str("name");
    }
    
    public static class qDocument extends DomainType<Document>{
        qDocument(String path){super(path);}
        qDocument(DomainType<?> type, String path) {super(type,path);}
        public final StringProperty name = str("name");
    }
    
    public static class qName extends DomainType<Name>{
        qName(String path){super(path);}
        qName(DomainType<?> type, String path) {super(type,path);}
        public final StringProperty firstName = str("firstName");
    }
    
    public static class qUser extends DomainType<User>{
        qUser(String path) {super(path);}
        qUser(DomainType<?> type, String path) {super(type,path);}
        private qCompany company;
        public final NumberProperty id = num("id");
        public final StringProperty userName = str("userName");
        public final StringProperty firstName = str("firstName");
        public final StringProperty lastName = str("lastName");
        public final qCompany company(){
            if (company == null) company = new qCompany(this,"company");
            return company;
        }        
    }

}
