package com.rodasoft.recursos.humanos.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConeccion {

    public Connection obtenerConexion() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontró el driver de MySQL (Connector/J): " + e.getMessage(), e);
        }   
        return DriverManager.getConnection(
                Credentials.URL_DATA_BASE,
                Credentials.USER_DB,
                Credentials.PASS_DB
        );
    }
}

