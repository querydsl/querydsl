package com.mysema.query.jdoql.dml;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.QueryMetadata;
import com.mysema.query.dml.DeleteClause;
import com.mysema.query.jdoql.JDOQLSerializer;
import com.mysema.query.jdoql.JDOQLTemplates;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.path.PEntity;

/**
 * @author tiwe
 *
 */
public class JDOQLDeleteClause implements DeleteClause<JDOQLDeleteClause>{

    private static final JDOQLTemplates DEFAULT_TEMPLATES = new JDOQLTemplates();
    
    private final QueryMetadata md = new DefaultQueryMetadata();
    
    private final PersistenceManager pm;
    
    private final JDOQLTemplates templates;
    
    private final PEntity<?> entity;
    
    public JDOQLDeleteClause(PersistenceManager pm, PEntity<?> entity){
        this(pm, entity, DEFAULT_TEMPLATES);
    }
    
    public JDOQLDeleteClause(PersistenceManager pm, PEntity<?> entity, JDOQLTemplates templates){
        this.entity = entity;
        this.pm = pm;
        this.templates = templates;
        md.addFrom(entity);        
    }
    
    @Override
    public long execute() {
        JDOQLSerializer serializer = new JDOQLSerializer(templates, entity);
        serializer.serializeForDelete(md);
        List<Object> constants = serializer.getConstants();
        
        Query query = pm.newQuery(serializer.toString());
        long rv;
        if (!constants.isEmpty()) {            
            rv = query.deletePersistentAll(constants.toArray());
        } else {
            rv = query.deletePersistentAll();
        }
        return rv;
    }

    @Override
    public JDOQLDeleteClause where(EBoolean... o) {
        md.addWhere(o);
        return this;
    }

}
