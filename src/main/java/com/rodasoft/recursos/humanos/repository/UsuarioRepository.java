package com.rodasoft.recursos.humanos.repository;

import com.rodasoft.recursos.humanos.config.DataBaseConeccion;
import com.rodasoft.recursos.humanos.exception.RepositoryException;
import com.rodasoft.recursos.humanos.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;


public class UsuarioRepository {

    private final DataBaseConeccion conexion;

    public UsuarioRepository(DataBaseConeccion conexion) {
        this.conexion = conexion;
    }

    public Optional<Usuario> buscarPorNombre(String nombreUsuario) {
        String sql = "SELECT id_usuario, nombre_usuario, contrasena_hash, id_rol FROM usuario WHERE nombre_usuario = ?";
        
        try (Connection c = conexion.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setString(1, nombreUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Usuario(
                            rs.getInt("id_usuario"),
                            rs.getString("nombre_usuario"),
                            rs.getString("contrasena_hash"),
                            rs.getInt("id_rol")
                    ));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
       
            e.printStackTrace();
            throw new RepositoryException("Error SQL al buscar usuario: " + e.getMessage(), e);
        }
    }

    public void insertar(Usuario usuario) {
        String sql = "INSERT INTO usuario (nombre_usuario, contrasena_hash, id_rol) VALUES (?, ?, ?)";
        try (Connection c = conexion.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, usuario.getNombreUsuario());
            ps.setString(2, usuario.getContrasenaHash());
            ps.setInt(3, usuario.getIdRol());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RepositoryException("Error SQL al registrar usuario: " + e.getMessage(), e);
        }
    }
}