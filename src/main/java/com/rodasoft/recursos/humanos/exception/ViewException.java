
package com.rodasoft.recursos.humanos.exception;

public class ViewException extends RuntimeException {
    public ViewException(String mensaje) {
        super(mensaje);
    }

    public ViewException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
