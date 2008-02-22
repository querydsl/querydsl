package com.mysema.query.test.domain;

public class Domain {
            
    public static class _AuditLog<T extends AuditLog> extends com.mysema.query.grammar.Types.DomainType<T>{
        private _Item<Item> item;
        _AuditLog(com.mysema.query.grammar.Types.DomainType<?> type, String path) {super(type,path);}        
        _AuditLog(String path) {super(path);}
        public _Item<Item> item() {
            if (item == null) item = new _Item<Item>(this,"item");
            return item;
        }   
    }
    
    public static class _Cat<T extends Cat> extends com.mysema.query.grammar.Types.DomainType<T>{
        public final com.mysema.query.grammar.Types.BooleanProperty alive = _boolean("alive");
        public final com.mysema.query.grammar.Types.Reference<Integer> bodyWeight = _prop("bodyWeight",Integer.class);        
        private _Cat<Cat> kittens;
        private _Cat<Cat> mate;
        public final com.mysema.query.grammar.Types.Reference<String> name = _prop("name",String.class);        
        _Cat(com.mysema.query.grammar.Types.DomainType<?> type, String path) {super(type,path);}
        _Cat(String path) {super(path);}        
        public final _Cat<Cat> kittens(){
            if (kittens == null) kittens = new _Cat<Cat>(this,"kittens"); 
            return kittens;
        }
        public final _Cat<Cat> mate(){
            if (mate == null) mate = new _Cat<Cat>(this,"mate");
            return mate;
        }        
    }
    
    public static class _Company<T extends Company> extends com.mysema.query.grammar.Types.DomainType<T>{
        public final com.mysema.query.grammar.Types.Reference<Long> id = _prop("id",Long.class);
        public final com.mysema.query.grammar.Types.Reference<String> name = _prop("name",String.class);
        _Company(com.mysema.query.grammar.Types.DomainType<?> type, String path) {super(type,path);}
        _Company(String path) {super(path);}
    }
    
    public static class _Customer<T extends Customer> extends com.mysema.query.grammar.Types.DomainType<T>{
        private _Name<Name> name;
        _Customer(com.mysema.query.grammar.Types.DomainType<?> type, String path) {super(type,path);}        
        _Customer(String path) {super(path);}
        public final _Name<Name> name(){
            if (name == null) name = new _Name<Name>(this, "name");
            return name;
        }
    }
    
    public static class _Document<T extends Document> extends com.mysema.query.grammar.Types.DomainType<T>{
        public final com.mysema.query.grammar.Types.Reference<String> name = _prop("name",String.class);
        public final com.mysema.query.grammar.Types.Reference<java.util.Date> validTo = _prop("validTo",java.util.Date.class);
        _Document(com.mysema.query.grammar.Types.DomainType<?> type, String path) {super(type,path);}
        _Document(String path){super(path);}
    }
    
    public static class _DomesticCat<T extends DomesticCat> extends _Cat<T>{
        _DomesticCat(com.mysema.query.grammar.Types.DomainType<?> type, String path) {super(type,path);}
        _DomesticCat(String path) {super(path);}
    }
    
    public static class _Item<T extends Item> extends com.mysema.query.grammar.Types.DomainType<T>{
        public com.mysema.query.grammar.Types.Reference<String> id = _prop("id",String.class);
        _Item(com.mysema.query.grammar.Types.DomainType<?> type, String path) {super(type,path);}
        _Item(String path){super(path);}
    }
    
    public static class _Name<T extends Name> extends com.mysema.query.grammar.Types.DomainType<T>{
        public final com.mysema.query.grammar.Types.Reference<String> firstName = _prop("firstName",String.class);
        _Name(com.mysema.query.grammar.Types.DomainType<?> type, String path) {super(type,path);}
        _Name(String path){super(path);}
    }
    
    public static class _Payment<T extends Payment> extends _Item<T>{
        _Payment(com.mysema.query.grammar.Types.DomainType<?> type, String path) {super(type,path);}
        _Payment(String path){super(path);}
    }
    
    public static class _User<T extends User> extends com.mysema.query.grammar.Types.DomainType<T>{
        private _Company<Company> company;
        public final com.mysema.query.grammar.Types.Reference<String> firstName = _prop("firstName",String.class);        
        public final com.mysema.query.grammar.Types.Reference<Long> id = _prop("id",Long.class);
        public final com.mysema.query.grammar.Types.Reference<String> lastName = _prop("lastName",String.class);
        public final com.mysema.query.grammar.Types.Reference<String> userName = _prop("userName",String.class);
        _User(com.mysema.query.grammar.Types.DomainType<?> type, String path) {super(type,path);}        
        _User(String path) {super(path);}
        public final _Company<Company> company(){
            if (company == null) company = new _Company<Company>(this,"company");
            return company;
        }        
    }

}
