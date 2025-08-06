package com.appTurnosMedicos.persistencia;

import com.appTurnosMedicos.modelo.Paciente;
import java.sql.*;
import java.util.Date;

public class PacienteDAO {

    public void crearPaciente(int dni, String nombrePaciente, String apellidoPaciente,
                            Date fechaNacimiento, String mail, String nacionalidad, 
                            String cel) throws SQLException {
        java.sql.Date sqlFecha = new java.sql.Date(fechaNacimiento.getTime());
        String sql = "INSERT INTO pacientes.Paciente (dni, nombre_paciente, apellido_paciente, fecha_nacimiento, mail, nacionalidad, cel, borrado) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = BaseDeDatos.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, dni);
            stmt.setString(2, nombrePaciente);
            stmt.setString(3, apellidoPaciente);
            stmt.setDate(4, sqlFecha);
            stmt.setString(5, mail);
            stmt.setString(6, nacionalidad);
            stmt.setString(7, cel);
            stmt.setInt(8, 0); 

            System.out.println(stmt);

            stmt.executeUpdate();
            System.out.println("Paciente creado exitosamente.");
        }
    }

    public Paciente obtenerPacientePorDni(int dni) throws SQLException {
        String sql = "SELECT * FROM pacientesPaciente WHERE dni = ?";

        try (Connection conn = BaseDeDatos.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dni);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id_paciente = rs.getInt("id_paciente");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                java.sql.Date fechaNacimiento = rs.getDate("fecha_nacimiento");
                String mail = rs.getString("mail");
                String nacionalidad = rs.getString("nacionalidad");
                String celular = rs.getString("celular");
                int idUsuario = rs.getInt("id_usuario");
                boolean borrado = rs.getBoolean("borrado");

                return new Paciente(id_paciente, dni, nombre, apellido, fechaNacimiento, mail, nacionalidad, celular, idUsuario,borrado);
            }
        }
        return null; // No se encontró el paciente
    }

    public boolean existePaciente(int dni) throws SQLException {
        String sql = "SELECT 1 FROM pacientes.Paciente WHERE dni = ?";

        try (Connection conn = BaseDeDatos.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dni);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }
    public void actualizarUsuarioIdDePaciente(int dni, int usuarioId) throws SQLException {
    String sql = "UPDATE pacientes.Paciente SET id_usuario = ? WHERE dni = ?";
    try (Connection conn = BaseDeDatos.obtenerConexion();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, usuarioId);
        stmt.setInt(2, dni);
        stmt.executeUpdate();
        }
    }
}
