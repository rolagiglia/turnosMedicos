
package com.appTurnosMedicos.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;





public class PasswordResetDAO {
    private static final Logger logger = LoggerFactory.getLogger(PasswordResetDAO.class);
    // Método para crear un registro en la tabla logins.passwordReset
    public String crearCodigo(int id_usuario) throws SQLException {
        String sql = "INSERT INTO logins.passwordReset (id_usuario, codigo, duracion) VALUES (?, ?, DATEADD(SECOND, 60, GETDATE()))";
        
        try (Connection conn = BaseDeDatos.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 1. Genera un código único de 4 caracteres
            String codigoUnico = generarCodigoUnico(4);
            
            
            // 2. Establece los parámetros de la consulta
            pstmt.setInt(1, id_usuario);
            pstmt.setString(2, codigoUnico);
            
            
            // 3. Ejecuta la consulta
            pstmt.executeUpdate();
            
            logger.info("Registro de restablecimiento de contraseña creado para el usuario " + id_usuario);
            return codigoUnico; // Retorna el código generado
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

    // Método para verificar el código de restablecimiento
    public boolean verificarCodigo(int dni, String codigo) throws SQLException {
        String sql = "SELECT r.id FROM pacientes.paciente p INNER JOIN logins.passwordReset r ON p.id_usuario=r.id_usuario WHERE p.dni = ? AND r.codigo=? AND r.usado=0 AND r.duracion>GETDATE()";
        
        try (Connection conn = BaseDeDatos.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setInt(1, dni);
            pstmt.setString(2, codigo);
            
            var rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int idReset = rs.getInt("id");
                // Si se encuentra un registro válido, marcarlo como usado
                marcarCodigoComoUsado(idReset);
                return true;
            } else {
                return false; // Código no válido o expirado
            }
        }
    }
    // Método para marcar el código como usado
    private void marcarCodigoComoUsado(int idReset) throws SQLException {    
        String sql = "UPDATE logins.passwordReset SET usado = 1 WHERE id = ?";
        
        try (Connection conn = BaseDeDatos.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setInt(1, idReset);
            pstmt.executeUpdate();
            
            logger.info("Código de restablecimiento marcado como usado para el registro id " + idReset);
        }
    }

}