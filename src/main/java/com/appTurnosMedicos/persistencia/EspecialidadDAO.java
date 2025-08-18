package com.appTurnosMedicos.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.appTurnosMedicos.modelo.Especialidad;

public class EspecialidadDAO {
     public void obtenerEspecialidades(List<Especialidad> especialidades) {
         String sql = "SELECT id_especialidad, nombre_especialidad FROM rrhh.Especialidad WHERE borrado = 0";
            
            try (Connection conn = BaseDeDatos.obtenerConexion();
                PreparedStatement statement = conn.prepareStatement(sql)) {
                ResultSet resultSet = statement.executeQuery();
                    while (resultSet.next()) {
                        // Mapea los datos de la fila a un objeto Usuario
                        int id = resultSet.getInt("id_especialidad");
                        String nombre = resultSet.getString("nombre_especialidad");                     
                        
                        especialidades.add(new Especialidad(id, nombre)); 
                    }
            } catch (SQLException e) {
                System.err.println("Error al buscar profesionales: " + e.getMessage());
                // Aquí podrías lanzar una excepción personalizada o loguear mejor
            }
    }
}
