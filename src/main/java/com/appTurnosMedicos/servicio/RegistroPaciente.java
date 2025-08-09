package com.appTurnosMedicos.servicio;

import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

import com.appTurnosMedicos.excepciones.PacienteExisteException;
import com.appTurnosMedicos.excepciones.RegistroDePacienteException;
import com.appTurnosMedicos.modelo.PacienteConPass;
import com.appTurnosMedicos.modelo.Usuario;
import com.appTurnosMedicos.persistencia.PacienteDAO;
import com.appTurnosMedicos.persistencia.UsuarioDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegistroPaciente {
    private static final Logger logger = LoggerFactory.getLogger(RegistroPaciente.class);
    private final PacienteDAO pacienteDAO;
    private final UsuarioDAO usuarioDAO;


    public RegistroPaciente(PacienteDAO pDAO, UsuarioDAO uDAO){
        this.pacienteDAO = pDAO;
        this.usuarioDAO = uDAO;

    }
    
    public void registraPaciente(PacienteConPass p) throws SQLException{
                
        try{    
            if(pacienteDAO.existePaciente(p.getDni()))
                throw new PacienteExisteException("El paciente ya esta registrado.");
            
            //crear paciente
            int maxIntentos = 3;
            int intento = 0;
           
            while (intento < maxIntentos) {
                try {
                    pacienteDAO.crearPaciente(p.getDni(),p.getNombrePaciente(),p.getApellidoPaciente(),p.getFechaNacimiento(),p.getMail(),p.getNacionalidad(),p.getCel()); // llama a tu método DAO original
                    logger.info("Paciente creado exitosamente: {}", p.getDni());
                    break;
                } catch (SQLException e) {
                    intento++;
                    logger.warn("Error al crear paciente, intento {} de {}", intento, maxIntentos, e);
                    if (intento < maxIntentos) {
                        try {
                            Thread.sleep(300); // espera 300 ms antes de reintentar
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                        }
                    } else {
                        logger.error("No se pudo crear el paciente después de {} intentos", maxIntentos, e);
                        throw new SQLException("Error en la creacion del paciente en la BD."); // si agotamos los intentos, falla
                    }
                }
            }

            //crear usuario
            String passwordHasheada = BCrypt.hashpw(p.getPass(), BCrypt.gensalt());
            Usuario usuario;
            try{
                usuarioDAO.crearUsuario(String.valueOf(p.getDni()), passwordHasheada);
                usuario = usuarioDAO.obtenerUsuario(String.valueOf(p.getDni()));
                pacienteDAO.actualizarUsuarioIdDePaciente(p.getDni(),usuario.getId());
                logger.info("Usuario creado exitosamente: {}", p.getDni());
                return;
            }catch (SQLException e) {
                 logger.error("Error en la creacion del usuario en BD");
                throw new SQLException("Error durante la creacion del usuario en BD.");
            }

            
        }catch(Exception e){
            
            throw new RegistroDePacienteException("Error en el registro del paciente. " + e.getMessage());
        }
    }
            
           
        
}
