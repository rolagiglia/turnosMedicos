package com.appTurnosMedicos.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.appTurnosMedicos.modelo.TipoUsuario;


public class TipoUsuarioDAO {
    public List<TipoUsuario> obtenerTipos(){
        List<TipoUsuario> listaTipos = new ArrayList<>();
        String sql = "SELECT id_tipo, tipo_usuario FROM logins.TipoUsuario";
            try (Connection conn = BaseDeDatos.obtenerConexion();
                PreparedStatement statement = conn.prepareStatement(sql)) {
                ResultSet resultSet = statement.executeQuery();
                    while (resultSet.next()) {
                        // Mapea los datos de la fila a un objeto 
                        int id = resultSet.getInt("id_tipo");
                        String tipo_usuario = resultSet.getString("tipo_usuario");                        
                        TipoUsuario tipoUsuario = new TipoUsuario(id, tipo_usuario);
                        listaTipos.add(tipoUsuario);
                    }
            } catch (SQLException e) {
                System.err.println("Error al obtener los tipos: " + e.getMessage());
                // Aquí podría lanzar una excepción personalizada 
            }
            return listaTipos;
    }
}
