package com.mysema.query.sql;

import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;

public class FirebirdTemplates extends SQLTemplates {

    private String limitOffsetTemplate = "\nrows {0} to {1}";

    private String limitTemplate = "\nrows {0}";

    private String offsetTemplate = "\nrows {0} to " + Integer.MAX_VALUE;

    public static Builder builder() {
        return new Builder() {
            @Override
            protected SQLTemplates build(char escape, boolean quote) {
                return new FirebirdTemplates(escape, quote);
            }
        };
    }

    public FirebirdTemplates() {
        this('\\', false);
    }

    public FirebirdTemplates(boolean quote) {
        this('\\', quote);
    }

    public FirebirdTemplates(char escape, boolean quote) {
        super("`", escape, quote);
        // TODO
    }

    @Override
    protected void serializeModifiers(QueryMetadata metadata, SQLSerializer context) {
        QueryModifiers mod = metadata.getModifiers();
        if (mod.isRestricting()) {
            if (mod.getLimit() != null) {
                if (mod.getOffset() != null) {
                    context.handle(limitOffsetTemplate, mod.getOffset() + 1, mod.getOffset() + mod.getLimit());
                } else {
                    context.handle(limitTemplate, mod.getLimit());
                }
            } else {
                context.handle(offsetTemplate, mod.getOffset() + 1);
            }
        }
    }
}
