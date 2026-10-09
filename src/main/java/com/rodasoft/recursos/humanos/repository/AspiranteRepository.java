package com.rodasoft.recursos.humanos.repository;

import com.rodasoft.recursos.humanos.config.DataBaseConeccion;
import com.rodasoft.recursos.humanos.exception.RepositoryException;
import com.rodasoft.recursos.humanos.model.Aspirante;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AspiranteRepository {

    private final DataBaseConeccion conexion;

    public AspiranteRepository(DataBaseConeccion conexion) {
        this.conexion = conexion;
    }

    public List<Aspirante> listar() {
        String sql = "SELECT id_aspirante, nombre, apellido, correo, telefono, direccion, edad FROM aspirante ORDER BY id_aspirante";
        try (Connection c = conexion.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Aspirante> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(new Aspirante(rs.getInt("id_aspirante"), rs.getString("nombre"), rs.getString("apellido"),
                        rs.getString("correo"), rs.getString("telefono"), rs.getString("direccion"),
                        rs.getInt("edad")));
            }
            return lista;
        } catch (SQLException e) {
            throw new RepositoryException("Error al listar los aspirantes.", e);
        }
    }

    public void insertar(Aspirante a) {
        String sql = "INSERT INTO aspirante (nombre, apellido, correo, telefono, direccion, edad) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = conexion.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, a.getNombre());
            ps.setString(2, a.getApellido());
            ps.setString(3, a.getCorreo());
            ps.setString(4, a.getTelefono());
            ps.setString(5, a.getDireccion());
            ps.setInt(6, a.getEdad());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Error al agregar el aspirante.", e);
        }
    }

    public void actualizar(Aspirante a) {
        String sql = "UPDATE aspirante SET nombre = ?, apellido = ?, correo = ?, telefono = ?, "
                + "direccion = ?, edad = ? WHERE id_aspirante = ?";
        try (Connection c = conexion.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, a.getNombre());
            ps.setString(2, a.getApellido());
            ps.setString(3, a.getCorreo());
            ps.setString(4, a.getTelefono());
            ps.setString(5, a.getDireccion());
            ps.setInt(6, a.getEdad());
            ps.setInt(7, a.getIdAspirante());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Error al actualizar el aspirante.", e);
        }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM aspirante WHERE id_aspirante = ?";
        try (Connection c = conexion.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Error al eliminar el aspirante.", e);
        }
    }

    public boolean existeCorreo(String correo, int idExcluido) {
        String sql = "SELECT COUNT(*) FROM aspirante WHERE correo = ? AND id_aspirante <> ?";
        try (Connection c = conexion.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, correo);
            ps.setInt(2, idExcluido);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error al verificar el correo.", e);
        }
    }
}

