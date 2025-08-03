package com.appTurnosMedicos.persistencia;

import com.appTurnosMedicos.modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class UsuarioDAO {
    //TRAE USUARIO DE LA BD---------------------------------------------------------
    /**
     * Obtiene el hash de la contraseña de un usuario desde la base de datos.
     *
     * @param usuario El nombre de usuario.
     * @return El hash de la contraseña si el usuario existe, o null si no.
     * @throws SQLException Si ocurre un error de base de datos.
     */

    public Usuario obtenerUsuario(String nombreUsuario){
        // VERIFICA USUARIO....
        if (validarUsuario(nombreUsuario)) {       
            String sql = "SELECT id_usuario, usuario, password_hash, fecha_de_creacion, tipo_usuario FROM logins.Usuario WHERE usuario = ?";
            
            try (Connection conn = BaseDeDatos.obtenerConexion();
                PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, nombreUsuario);
                ResultSet resultSet = statement.executeQuery();
                    if (resultSet.next()) {
                        // Mapea los datos de la fila a un objeto Usuario
                        int id = resultSet.getInt("id_usuario");
                        String user = resultSet.getString("usuario");
                        String passwordHash = resultSet.getString("password_hash");
                        Date fecha_creacion = resultSet.getDate("fecha_de_creacion");
                        int tipoUsuario = resultSet.getInt("tipo_usuario");
                        
                        return new Usuario(id, user, passwordHash, fecha_creacion, tipoUsuario);
                    }
            } catch (SQLException e) {
                System.err.println("Error al buscar usuario por nombre de usuario: " + e.getMessage());
                // Aquí podrías lanzar una excepción personalizada o loguear mejor
            }
        }
        return null; // No se encontró el usuario
    }

    private boolean validarUsuario(String username) {
        if (username == null || username.trim().isEmpty() ||
            username.trim().length() < 4 || username.trim().length() > 20 ||
            !username.trim().matches("^[a-zA-Z0-9_-]+$")) 
            return false; 
        else 
            return true;
    }





    
    public String obtenerHashContrasena(String usuario) throws SQLException {
        String sql = "SELECT password_hash FROM logins.Usuario WHERE usuario = ?";
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