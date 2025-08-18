package com.appTurnosMedicos.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.appTurnosMedicos.modelo.Profesional;


public class ProfesionalDAO {
    
    // Aquí los métodos para interactuar con la base de datos
    // relacionados con los profesionales

    public void obtenerProfesionales(List<Profesional> profesionales){
         String sql = "SELECT id_profesional, nombre_profesional, apellido_profesional, nro_matricula, id_especialidad FROM rrhh.Profesional WHERE borrado = 0";
            
            try (Connection conn = BaseDeDatos.obtenerConexion();
                PreparedStatement statement = conn.prepareStatement(sql)) {
                ResultSet resultSet = statement.executeQuery();
                    while (resultSet.next()) {
                        // Mapea los datos de la fila a un objeto Usuario
                        int id = resultSet.getInt("id_profesional");
                        String nombre = resultSet.getString("nombre_profesional");
                        String apellido = resultSet.getString("apellido_profesional");
                        int nro_matricula = resultSet.getInt("nro_matricula");
                        int id_especialidad = resultSet.getInt("id_especialidad");                      
                        
                        profesionales.add(new Profesional(id, nombre, apellido, nro_matricula, id_especialidad)); 
                    }
            } catch (SQLException e) {
                System.err.println("Error al buscar profesionales: " + e.getMessage());
                // Aquí podrías lanzar una excepción personalizada o loguear mejor
            }
    }
}
