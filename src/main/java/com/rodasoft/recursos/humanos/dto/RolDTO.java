
package com.rodasoft.recursos.humanos.dto;

public record RolDTO(int id, String nombre) {
    @Override
    public String toString() {
        return nombre;
    }
}

