package com.rodasoft.recursos.humanos.repository;

import com.rodasoft.recursos.humanos.config.DataBaseConeccion;
import com.rodasoft.recursos.humanos.exception.RepositoryException;
import com.rodasoft.recursos.humanos.model.Rol;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RolRepository {


    private final DataBaseConeccion conexion;

 
    public RolRepository(DataBaseConeccion conexion) {
        this.conexion = conexion;
    }

    public List<Rol> listar() {
        String sql = "SELECT id_rol, nombre_rol FROM rol ORDER BY nombre_rol";
        try (Connection c = conexion.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Rol> roles = new ArrayList<>();
            while (rs.next()) {
                roles.add(new Rol(rs.getInt("id_rol"), rs.getString("nombre_rol")));
            }
            return roles;
        } catch (SQLException e) {
            throw new RepositoryException("Error al listar los roles.", e);
        }
    }
}

