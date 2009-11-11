package com.mysema.query.sql;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.QueryMetadata;
import com.mysema.query.support.QueryBaseWithDetach;
import com.mysema.query.types.path.PEntity;

/**
 * @author tiwe
 *
 */
public class SQLSubQuery extends QueryBaseWithDetach<SQLSubQuery>{

    private static final SQLTemplates templates = new SQLTemplates();
    
    public SQLSubQuery(QueryMetadata metadata) {
        super(metadata);
    }
    
    public SQLSubQuery() {
        super(new DefaultQueryMetadata());
    }
    
    public SQLSubQuery from(PEntity<?>... o){
        getMetadata().addFrom(o);
        return _this;
    }
    
    @Override
    public String toString(){
        if (!getMetadata().getJoins().isEmpty()){
            SQLSerializer serializer = new SQLSerializer(templates);
            serializer.serialize(getMetadata(), false);
            return serializer.toString().trim();
        }else{
            return super.toString();
        }        
    }
    
//
//    SQLQuery fullJoin(Expr<?> o);
//
//    SQLQuery innerJoin(Expr<?> o);
//
//    SQLQuery join(Expr<?> o);
//
//    SQLQuery leftJoin(Expr<?> o);
//    
//    SQLQuery on(EBoolean condition);

}
