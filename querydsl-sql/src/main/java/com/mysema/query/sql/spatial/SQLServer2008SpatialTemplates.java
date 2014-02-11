package com.mysema.query.sql.spatial;

import com.mysema.query.sql.SQLServer2008Templates;
import com.mysema.query.sql.SQLTemplates;

public class SQLServer2008SpatialTemplates extends SQLServer2008Templates {

    public static Builder builder() {
        return new Builder() {
            @Override
            protected SQLTemplates build(char escape, boolean quote) {
                return new SQLServer2008SpatialTemplates(escape, quote);
            }
        };
    }

    public SQLServer2008SpatialTemplates() {
        this('\\',false);
    }

    public SQLServer2008SpatialTemplates(boolean quote) {
        this('\\',quote);
    }

    public SQLServer2008SpatialTemplates(char escape, boolean quote) {
        super(escape, quote);
        add(SpatialTemplatesSupport.getSpatialOps("ST", false));
    }


}