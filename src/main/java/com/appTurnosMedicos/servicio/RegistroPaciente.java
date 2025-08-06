package com.appTurnosMedicos.servicio;

import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

import com.appTurnosMedicos.excepciones.PacienteExisteException;
import com.appTurnosMedicos.excepciones.RegistroDePacienteException;
import com.appTurnosMedicos.modelo.PacienteConPass;
import com.appTurnosMedicos.modelo.Usuario;
import com.appTurnosMedicos.persistencia.PacienteDAO;
import com.appTurnosMedicos.persistencia.UsuarioDAO;


public class RegistroPaciente {
    PacienteDAO pacienteDAO;
    
    public RegistroPaciente(){
        pacienteDAO = new PacienteDAO();
    }
    
    public void registraPaciente(PacienteConPass p) throws SQLException{
        System.out.println(p.getDni());
        
        try{    
            if(pacienteDAO.existePaciente(p.getDni()))
                throw new PacienteExisteException("El paciente ya esta registrado.");
            
            //crear paciente
            int maxIntentos = 3;
            int intento = 0;
           
            while (intento < maxIntentos) {
                try {
                    pacienteDAO.crearPaciente(p.getDni(),p.getNombrePaciente(),p.getApellidoPaciente(),p.getFechaNacimiento(),p.getMail(),p.getNacionalidad(),p.getCel()); // llama a tu método DAO original
                    System.out.println("paciente creado");
                    break;
                } catch (SQLException e) {
                    intento++;
                    System.err.println("Error al crear paciente, intento " + intento + " de " + maxIntentos);
                       e.printStackTrace();
                    if (intento < maxIntentos) {
                        try {
                            Thread.sleep(300); // espera 300 ms antes de reintentar
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                        }
                    } else {
                        throw new SQLException("Error en la creacion del paciente en la BD."); // si agotamos los intentos, falla
                    }
                }
            }

            //crear usuario
            String passwordHasheada = BCrypt.hashpw(p.getPass(), BCrypt.gensalt());
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            Usuario usuario;
            try{
                usuarioDAO.crearUsuario(String.valueOf(p.getDni()), passwordHasheada);
                usuario = usuarioDAO.obtenerUsuario(String.valueOf(p.getDni()));
                pacienteDAO.actualizarUsuarioIdDePaciente(p.getDni(),usuario.getId());
                System.out.println("usuario creado exitosamente");
                return;
            }catch (SQLException e) {
                throw new SQLException("Error durante la creacion del usuario en BD.");
            }

            
        }catch(Exception e){
            
            throw new RegistroDePacienteException("Error en el registro del paciente. " + e.getMessage());
        }
    }
            
           
        
}
