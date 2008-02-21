package com.mysema.query.test.domain;

public class Domain {
            
    public static class qAuditLog<T extends AuditLog> extends com.mysema.query.grammar.Types.DomainType<T>{
        private qItem<Item> item;
        qAuditLog(com.mysema.query.grammar.Types.DomainType<?> type, String path) {super(type,path);}        
        qAuditLog(String path) {super(path);}
        public qItem<Item> item() {
            if (item == null) item = new qItem<Item>(this,"item");
            return item;
        }   
    }
    
    public static class qCat<T extends Cat> extends com.mysema.query.grammar.Types.DomainType<T>{
        public final com.mysema.query.grammar.Types.BooleanProperty alive = _boolean("alive");
        public final com.mysema.query.grammar.Types.Reference<Integer> bodyWeight = _prop("bodyWeight",Integer.class);        
        private qCat<Cat> kittens;
        private qCat<Cat> mate;
        public final com.mysema.query.grammar.Types.Reference<String> name = _prop("name",String.class);        
        qCat(com.mysema.query.grammar.Types.DomainType<?> type, String path) {super(type,path);}
        qCat(String path) {super(path);}        
        public final qCat<Cat> kittens(){
            if (kittens == null) kittens = new qCat<Cat>(this,"kittens"); 
            return kittens;
        }
        public final qCat<Cat> mate(){
            if (mate == null) mate = new qCat<Cat>(this,"mate");
            return mate;
        }        
    }
    
    public static class qCompany<T extends Company> extends com.mysema.query.grammar.Types.DomainType<T>{
        public final com.mysema.query.grammar.Types.Reference<Long> id = _prop("id",Long.class);
        public final com.mysema.query.grammar.Types.Reference<String> name = _prop("name",String.class);
        qCompany(com.mysema.query.grammar.Types.DomainType<?> type, String path) {super(type,path);}
        qCompany(String path) {super(path);}
    }
    
    public static class qCustomer<T extends Customer> extends com.mysema.query.grammar.Types.DomainType<T>{
        private qName<Name> name;
        qCustomer(com.mysema.query.grammar.Types.DomainType<?> type, String path) {super(type,path);}        
        qCustomer(String path) {super(path);}
        public final qName<Name> name(){
            if (name == null) name = new qName<Name>(this, "name");
            return name;
        }
    }
    
    public static class qDocument<T extends Document> extends com.mysema.query.grammar.Types.DomainType<T>{
        public final com.mysema.query.grammar.Types.Reference<String> name = _prop("name",String.class);
        public final com.mysema.query.grammar.Types.Reference<java.util.Date> validTo = _prop("validTo",java.util.Date.class);
        qDocument(com.mysema.query.grammar.Types.DomainType<?> type, String path) {super(type,path);}
        qDocument(String path){super(path);}
    }
    
    public static class qDomesticCat<T extends DomesticCat> extends qCat<T>{
        qDomesticCat(com.mysema.query.grammar.Types.DomainType<?> type, String path) {super(type,path);}
        qDomesticCat(String path) {super(path);}
    }
    
    public static class qItem<T extends Item> extends com.mysema.query.grammar.Types.DomainType<T>{
        public com.mysema.query.grammar.Types.Reference<String> id = _prop("id",String.class);
        qItem(com.mysema.query.grammar.Types.DomainType<?> type, String path) {super(type,path);}
        qItem(String path){super(path);}
    }
    
    public static class qName<T extends Name> extends com.mysema.query.grammar.Types.DomainType<T>{
        public final com.mysema.query.grammar.Types.Reference<String> firstName = _prop("firstName",String.class);
        qName(com.mysema.query.grammar.Types.DomainType<?> type, String path) {super(type,path);}
        qName(String path){super(path);}
    }
    
    public static class qPayment<T extends Payment> extends qItem<T>{
        qPayment(com.mysema.query.grammar.Types.DomainType<?> type, String path) {super(type,path);}
        qPayment(String path){super(path);}
    }
    
    public static class qUser<T extends User> extends com.mysema.query.grammar.Types.DomainType<T>{
        private qCompany<Company> company;
        public final com.mysema.query.grammar.Types.Reference<String> firstName = _prop("firstName",String.class);        
        public final com.mysema.query.grammar.Types.Reference<Long> id = _prop("id",Long.class);
        public final com.mysema.query.grammar.Types.Reference<String> lastName = _prop("lastName",String.class);
        public final com.mysema.query.grammar.Types.Reference<String> userName = _prop("userName",String.class);
        qUser(com.mysema.query.grammar.Types.DomainType<?> type, String path) {super(type,path);}        
        qUser(String path) {super(path);}
        public final qCompany<Company> company(){
            if (company == null) company = new qCompany<Company>(this,"company");
            return company;
        }        
    }

}
