package com.wexa.graphapp.exception;

/**
 * Thrown when CognoDB cannot be reached (network issue, wrong credentials,
 * instance paused, etc). Wrapping driver exceptions in this type lets the
 * global exception handler return a clean 503 instead of leaking driver
 * internals to the client.
 */
public class DatabaseUnavailableException extends RuntimeException {
    public DatabaseUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
