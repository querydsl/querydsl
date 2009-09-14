package com.mysema.query.hql.jpa;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.QueryMetadata;
import com.mysema.query.dml.DeleteClause;
import com.mysema.query.hql.HQLSerializer;
import com.mysema.query.hql.HQLTemplates;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.path.PEntity;

/**
 * @author tiwe
 *
 */
public class JPADeleteClause implements DeleteClause<JPADeleteClause>{

    private static final HQLTemplates DEFAULT_TEMPLATES = new HQLTemplates();
    
    private final QueryMetadata md = new DefaultQueryMetadata();
    
    private final EntityManager em;
    
    private final HQLTemplates templates;
    
    public JPADeleteClause(EntityManager em, PEntity<?> entity){
        this(em, entity, DEFAULT_TEMPLATES);
    }
    
    public JPADeleteClause(EntityManager em, PEntity<?> entity, HQLTemplates templates){
        this.em = em;
        this.templates = templates;
        md.addFrom(entity);        
    }
    
    @Override
    public long execute() {
        HQLSerializer serializer = new HQLSerializer(templates);
        serializer.serializeForDelete(md);
        Map<Object,String> constants = serializer.getConstantToLabel();

        Query query = em.createQuery(serializer.toString());
        JPAUtil.setConstants(query, constants);
        return query.executeUpdate();
    }

    @Override
    public JPADeleteClause where(EBoolean... o) {
        md.addWhere(o);
        return this;
    }

}
