package com.mysema.query.elasticsearch.jackson;

/**
 * Mapping exception for factory purposes.
 *
 * @author kevinleturc
 */
public class MappingException extends RuntimeException {

    /**
     * Default constructor.
     *
     * @param message The message.
     * @param cause   The cause.
     */
    public MappingException(String message, Throwable cause) {
        super(message, cause);
    }

}
