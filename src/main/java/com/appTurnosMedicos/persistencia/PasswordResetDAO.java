
package com.appTurnosMedicos.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Random;



public class PasswordResetDAO {

    // Método para crear un registro en la tabla logins.passwordReset
    public String crearCodigo(int id_usuario) throws SQLException {
        String sql = "INSERT INTO logins.passwordReset (id_usuario, codigo, duracion) VALUES (?, ?, ?)";
        
        try (Connection conn = BaseDeDatos.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 1. Genera un código único de 4 caracteres
            String codigoUnico = generarCodigoUnico(4);
            
            // 2. Calcula la fecha de expiración (ahora + 60 segundos)
            LocalDateTime duracion = LocalDateTime.now().plusSeconds(60);
            
            // 3. Establece los parámetros de la consulta
            pstmt.setInt(1, id_usuario);
            pstmt.setString(2, codigoUnico);
            pstmt.setTimestamp(3, Timestamp.valueOf(duracion));
            
            // 4. Ejecuta la consulta
            pstmt.executeUpdate();
            
            System.out.println("Registro de restablecimiento de contraseña creado para el usuario " + id_usuario);
            return codigoUnico;
        } 
    }

    
    // Método para generar un código alfanumérico único
    private String generarCodigoUnico(int longitud) {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder codigo = new StringBuilder(longitud);
        for (int i = 0; i < longitud; i++) {
            codigo.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }
        return codigo.toString();
    }
}