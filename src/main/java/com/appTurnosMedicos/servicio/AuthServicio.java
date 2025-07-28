package com.appTurnosMedicos.servicio;

import com.appTurnosMedicos.persistencia.UsuarioDAO;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt; // Importar la librería BCrypt

public class AuthServicio {
    private final UsuarioDAO usuarioDAO;

    public AuthServicio(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    /**
     * Verifica las credenciales de un usuario contra la base de datos.
     * Asume que las contraseñas están hasheadas con BCrypt en la DB.
     *
     * @param usuario El nombre de usuario.
     * @param claveEnTextoPlano La contraseña en texto plano ingresada por el usuario.
     * @return true si las credenciales son correctas, false en caso contrario.
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     */
    public boolean verificarCredenciales(String usuario, String claveEnTextoPlano) throws SQLException {
        String hashAlmacenado = usuarioDAO.obtenerHashContrasena(usuario);

        if (hashAlmacenado == null) {
            // Usuario no encontrado en la base de datos
            return false;
        }

        // Compara la contraseña en texto plano con el hash almacenado usando BCrypt
        // BCrypt.checkpw() se encarga de extraer el salt del hash almacenado y hacer la comparación.
        return BCrypt.checkpw(claveEnTextoPlano, hashAlmacenado);
    }
    
    // Método para registrar un nuevo usuario (deberías tener una ruta /register para esto)
    public void registrarUsuario(String usuario, String claveEnTextoPlano) throws SQLException {
        String salt = BCrypt.gensalt(); // Genera un salt aleatorio
        String hashed_password = BCrypt.hashpw(claveEnTextoPlano, salt); // Hashea la contraseña con el salt
        usuarioDAO.crearUsuario(usuario, hashed_password);
    }
}