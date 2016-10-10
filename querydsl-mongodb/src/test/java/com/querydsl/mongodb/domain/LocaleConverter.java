package com.querydsl.mongodb.domain;

import java.util.Locale;

import org.mongodb.morphia.converters.SimpleValueConverter;
import org.mongodb.morphia.converters.TypeConverter;
import org.mongodb.morphia.mapping.MappedField;
import org.mongodb.morphia.mapping.MappingException;

public class LocaleConverter extends TypeConverter implements SimpleValueConverter {

    public LocaleConverter() {
        super(Locale.class);
    }

    @Override
    public final Object encode(Object value, MappedField optionalExtraInfo) throws MappingException {
        if (value == null) {
            return null;
        }
        if (!(value instanceof Locale)) {
            throw new MappingException("Unable to convert " + value.getClass().getName());
        }
        return ((Locale) value).toLanguageTag();
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Locale decode(Class targetClass, Object fromDBObject, MappedField optionalExtraInfo) throws MappingException {
        if (fromDBObject == null) {
            return null;
        }
        if (fromDBObject instanceof String) {
            return Locale.forLanguageTag((String) fromDBObject);
        }
        throw new MappingException("Unable to convert " + fromDBObject.getClass().getName());
    }
}
