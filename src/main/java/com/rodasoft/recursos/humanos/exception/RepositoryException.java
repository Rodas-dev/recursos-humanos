
package com.rodasoft.recursos.humanos.exception;

public class RepositoryException extends RuntimeException {
    public RepositoryException(String mensaje) {
        super(mensaje);
    }

    public RepositoryException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}

