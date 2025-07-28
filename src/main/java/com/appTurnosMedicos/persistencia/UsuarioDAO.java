package com.appTurnosMedicos.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    /**
     * Obtiene el hash de la contraseña de un usuario desde la base de datos.
     *
     * @param usuario El nombre de usuario.
     * @return El hash de la contraseña si el usuario existe, o null si no.
     * @throws SQLException Si ocurre un error de base de datos.
     */
    public String obtenerHashContrasena(String usuario) throws SQLException {
        String sql = "SELECT password_hash FROM usuarios WHERE usuario = ?";
        try (Connection conn = BaseDeDatos.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("password_hash");
            }
            return null; // Usuario no encontrado
        }
    }

    // Método para crear un usuario (solo para demostración, en una app real sería más robusto)
    public void crearUsuario(String usuario, String passwordHash) throws SQLException {
        String sql = "INSERT INTO usuarios (usuario, password_hash) VALUES (?, ?)";
        try (Connection conn = BaseDeDatos.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario);
            stmt.setString(2, passwordHash);
            stmt.executeUpdate();
            System.out.println("Usuario '" + usuario + "' creado con éxito en la DB.");
        }
    }
}