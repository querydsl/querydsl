package com.mysema.query.test.domain;

import java.util.Date;

import com.mysema.query.grammar.Types.BooleanProperty;
import com.mysema.query.grammar.Types.DomainType;
import com.mysema.query.grammar.Types.Reference;


/**
 * Domain provides
 *
 * @author tiwe
 * @version $Id$
 */
public class Domain {
    // AuditLog
    public static final qAuditLog<AuditLog> log = new qAuditLog<AuditLog>("log");
    
    // Cat
    public static final qCat<Cat> cat = new qCat<Cat>("cat");
    public static final qCat<Cat> cat1 = new qCat<Cat>("cat1");
    public static final qCat<Cat> cat2 = new qCat<Cat>("cat2");
    public static final qCat<Cat> cat3 = new qCat<Cat>("cat3");
    public static final qCat<Cat> cat4 = new qCat<Cat>("cat4");
    public static final qCat<Cat> cat5 = new qCat<Cat>("cat5");
    
    public static final qCat<Cat> kitten = new qCat<Cat>("kitten");
    public static final qCat<Cat> child = new qCat<Cat>("child");
    public static final qCat<Cat> mate = new qCat<Cat>("mate"); 
    
    // Company
    public static final qCompany<Company> company = new qCompany<Company>("company");
    public static final qCompany<Company> company1 = new qCompany<Company>("company1");
    public static final qCompany<Company> company2 = new qCompany<Company>("company2");
    public static final qCompany<Company> company3 = new qCompany<Company>("company3");
    public static final qCompany<Company> company4 = new qCompany<Company>("company4");
    public static final qCompany<Company> company5 = new qCompany<Company>("company5");
    
    // Customer
    public static final qCustomer<Customer> cust = new qCustomer<Customer>("cust");
    
    // Document
    public static final qDocument<Document> doc = new qDocument<Document>("doc");
    
    // DomesticCat
    public static final qDomesticCat<DomesticCat> domesticCat = new qDomesticCat<DomesticCat>("domesticCat");
    
    // Payment
    public static final qPayment<Payment> payment = new qPayment<Payment>("payment");
    
    // User
    public static final qUser<User> user = new qUser<User>("user");
    public static final qUser<User> user1 = new qUser<User>("user1");
    public static final qUser<User> user2 = new qUser<User>("user2");
    public static final qUser<User> user3 = new qUser<User>("user3");
    public static final qUser<User> user4 = new qUser<User>("user4");
    public static final qUser<User> user5 = new qUser<User>("user5");
        
    // type declarations
    
    public static class qAuditLog<T extends AuditLog> extends DomainType<T>{
        qAuditLog(String path) {super(path);}
        qAuditLog(DomainType<?> type, String path) {super(type,path);}
        
        private qItem<Item> item;
        public qItem<Item> item() {
            if (item == null) item = new qItem<Item>(this,"item");
            return item;
        }   
    }
    
    public static class qCat<T extends Cat> extends DomainType<T>{
        qCat(String path) {super(path);}
        qCat(DomainType<?> type, String path) {super(type,path);}        
        public final BooleanProperty alive = _boolean("alive");
        public final Reference<Integer> bodyWeight = _prop("bodyWeight",Integer.class);
        public final Reference<String> name = _prop("name",String.class);
        
        private qCat<Cat> kittens;
        public final qCat<Cat> kittens(){
            if (kittens == null) kittens = new qCat<Cat>(this,"kittens"); 
            return kittens;
        }        
        private qCat<Cat> mate;
        public final qCat<Cat> mate(){
            if (mate == null) mate = new qCat<Cat>(this,"mate");
            return mate;
        }        
    }
    
    public static class qDomesticCat<T extends DomesticCat> extends qCat<T>{
        qDomesticCat(String path) {super(path);}
        qDomesticCat(DomainType<?> type, String path) {super(type,path);}
    }
    
    public static class qCustomer<T extends Customer> extends DomainType<T>{
        qCustomer(String path) {super(path);}
        qCustomer(DomainType<?> type, String path) {super(type,path);}
        
        private qName<Name> name;
        public final qName<Name> name(){
            if (name == null) name = new qName<Name>(this, "name");
            return name;
        }
    }
    
    public static class qCompany<T extends Company> extends DomainType<T>{
        qCompany(String path) {super(path);}
        qCompany(DomainType<?> type, String path) {super(type,path);}
        public final Reference<Long> id = _prop("id",Long.class);
        public final Reference<String> name = _prop("name",String.class);
    }
    
    public static class qDocument<T extends Document> extends DomainType<T>{
        qDocument(String path){super(path);}
        qDocument(DomainType<?> type, String path) {super(type,path);}
        public final Reference<String> name = _prop("name",String.class);
        public final Reference<Date> validTo = _prop("validTo",Date.class);
    }
    
    public static class qItem<T extends Item> extends DomainType<T>{
        qItem(String path){super(path);}
        qItem(DomainType<?> type, String path) {super(type,path);}
        public Reference<String> id = _prop("id",String.class);
    }
    
    public static class qName<T extends Name> extends DomainType<T>{
        qName(String path){super(path);}
        qName(DomainType<?> type, String path) {super(type,path);}
        public final Reference<String> firstName = _prop("firstName",String.class);
    }
    
    public static class qPayment<T extends Payment> extends qItem<T>{
        qPayment(String path){super(path);}
        qPayment(DomainType<?> type, String path) {super(type,path);}
    }
    
    public static class qUser<T extends User> extends DomainType<T>{
        qUser(String path) {super(path);}
        qUser(DomainType<?> type, String path) {super(type,path);}        
        public final Reference<Long> id = _prop("id",Long.class);
        public final Reference<String> userName = _prop("userName",String.class);
        public final Reference<String> firstName = _prop("firstName",String.class);
        public final Reference<String> lastName = _prop("lastName",String.class);
        
        private qCompany<Company> company;
        public final qCompany<Company> company(){
            if (company == null) company = new qCompany<Company>(this,"company");
            return company;
        }        
    }

}
