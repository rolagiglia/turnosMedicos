package com.appTurnosMedicos.controlador;


import com.appTurnosMedicos.excepciones.PacienteNoExisteException;
import com.appTurnosMedicos.modelo.Paciente;
import com.appTurnosMedicos.persistencia.PacienteDAO;
import com.appTurnosMedicos.persistencia.PasswordResetDAO;
import com.appTurnosMedicos.persistencia.UsuarioDAO;
import com.appTurnosMedicos.servicio.EmailServicio;
import com.appTurnosMedicos.servicio.GestionDeSesionServicio;
import com.appTurnosMedicos.servicio.HaseaPassword;

import io.github.cdimascio.dotenv.Dotenv; //almacenamiento de variables de entorno api key local
//resolver para produccion

import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.BlockingHandler;
import io.undertow.server.handlers.CookieImpl;
import io.undertow.server.handlers.PathTemplateHandler;
import io.undertow.util.Headers;

public class ControladorRecuperoDePass {
    // Controlador para manejar la recuperación de contraseña
    
    
     public void registrarRutas(PathTemplateHandler pathTemplateHandler) {
        
        // Define las rutas para el controlador de recuperación de contraseña
        pathTemplateHandler.add("/paciente/recuperaPass/email", new BlockingHandler(exchange -> {
            exchange.getRequestReceiver().receiveFullString(this::handleEmail);
            }));
        pathTemplateHandler.add("/paciente/recuperaPass/verificaCodigo", new BlockingHandler(exchange -> {
            exchange.getRequestReceiver().receiveFullString(this::handleVerificarCodigo);
            
        }));

        pathTemplateHandler.add("/paciente/recuperaPass/cambiarPassword", new BlockingHandler(exchange -> {
            exchange.getRequestReceiver().receiveFullString(this::handleCambiarPassword);
            
        }));
        //lee toda la cadena del cuerpo de la solicitud http y la pasa como parametro string a la funcion handleEmail.
        //Forzando a la solicitud a esperar hasta que la lectura del cuerpo esté completa. y luego delega en handleEmail
        
    }


    public void handleEmail(HttpServerExchange exchange, String formBody) {
        try {        
            if (formBody != null) {
                String [] parts = formBody.split("=");             
                
                String dni = parts[1];

                if(!dni.isEmpty()){
                    System.out.println("DNI recibido: " + dni);
                    int dniN = Integer.parseInt(dni);

                    PacienteDAO pacienteDAO = new PacienteDAO();
                    Paciente paciente = pacienteDAO.obtenerPacientePorDni(dniN);                   
                    
                    // Carga las variables del archivo .env (apikey)/////////////local
                    Dotenv dotenv = Dotenv.load();

                    // Obtiene el valor de la API_KEY //////////////////local
                    String apiKey = dotenv.get("API_KEY");
                    System.out.println("API_KEY: " + apiKey);

                    EmailServicio emailServicio = new EmailServicio(apiKey); 
                    PasswordResetDAO passwordResetDAO = new PasswordResetDAO();
                    
                    // Genera un código único de 6 dígitos
                    String codigo = passwordResetDAO.crearCodigo(paciente.getIdUsuario());
                    System.out.println("Código generado: " + codigo);
                    emailServicio.enviarCorreo(paciente.getMail(), paciente.getNombrePaciente(),"Codigo de recuperacion", codigo + " es tu codigo de recuperacion de contraseña. El mismo tiene una duracion de 60 segundos. Si no lo has solicitado, por favor ignora este mensaje.");

                    exchange.setStatusCode(200);
                    exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
                    exchange.getResponseSender().send("{\"ok\":\"true\",\"mensaje\": \"Código enviado al correo registrado\"}");

                }
                else {
                    throw new PacienteNoExisteException("El usuario ingresado no esta registrado.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            exchange.setStatusCode(400);
            exchange.getResponseSender().send("{\"error\": \"" + e.getMessage() + "\"}");
        }


    }

public void handleVerificarCodigo(HttpServerExchange exchange, String formBody) {
        try {
            if (formBody != null) {
                String[] parts = formBody.split("&");
                String codigo = null;
                String dni = null;
                for (String part : parts) {
                    String[] keyValue = part.split("=");
                    if (keyValue.length == 2) {
                        if ("codigo".equals(keyValue[0])) {
                            codigo = keyValue[1];
                        } else if ("dni".equals(keyValue[0])) {
                            dni = keyValue[1];
                        }
                    }
                }

                if (codigo != null && dni != null) {
                    System.out.println("Código recibido: " + codigo);
                    System.out.println("DNI recibido: " + dni);
                    
                    int dniN = Integer.parseInt(dni);
                    PasswordResetDAO passwordResetDAO = new PasswordResetDAO();
                    boolean isValid = passwordResetDAO.verificarCodigo(dniN, codigo);
                    System.out.println("Verificación del código: " + dniN + ", " + codigo + " - Resultado: " + isValid  );
                    
                    if (isValid) {
                                         
                        // Crear una nueva sesión temporal para el restablecimiento de contraseña
                        String resetSessionId = GestionDeSesionServicio.createSession("RESET_" + dni);
                        
                        // Configura la cookie con las opciones requeridas
                        exchange.setResponseCookie(
                            new CookieImpl(GestionDeSesionServicio.getSessionCookieName(), resetSessionId)
                                .setHttpOnly(true)
                                //.setSecure(true) invalidado para pruebas locales
                                .setPath("/") //para que sea accesible en todo el dominio
                                .setSameSiteMode("Strict")
                                .setMaxAge(10 * 60) // 10 minutos en segundos
                        );
                        
                        // Respuesta exitosa
                        exchange.setStatusCode(200);
                        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
                        exchange.getResponseSender().send("{\"ok\":true, \"mensaje\": \"Código verificado correctamente\"}");
                    } else {
                        throw new PacienteNoExisteException("Código invalido o caducado.");
                    }
                } else {
                    throw new PacienteNoExisteException("Datos incompletos para la verificación.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            exchange.setStatusCode(400);
            exchange.getResponseSender().send("{\"error\": \"" + e.getMessage() + "\"}");
        }
        
    }

    public void handleCambiarPassword(HttpServerExchange exchange, String formBody) {
        try {
            String sessionId = GestionDeSesionServicio.getSessionIdFromCookie(exchange);
            String userId = GestionDeSesionServicio.getUserId(sessionId);

            if (userId == null || !userId.startsWith("RESET_")) {
                exchange.setStatusCode(403);
                exchange.getResponseSender().send("{\"error\": \"Acceso no autorizado o sesión expirada.\"}");
                return; 
            }
            String password = null;
            String confirmPassword = null;
            if (formBody != null) {
                String[] parts = formBody.split("&");
                
                for (String part : parts) {
                    String[] keyValue = part.split("=");
                    if (keyValue.length == 2) {
                        if ("nuevaPass".equals(keyValue[0])) {
                            password = keyValue[1];
                        } else if ("repitePass".equals(keyValue[0])) {
                            confirmPassword = keyValue[1];
                        }
                    }
                }
            }

            if (!password.equals(confirmPassword) || password == null || confirmPassword == null || password.isEmpty() || confirmPassword.isEmpty()) {
                exchange.setStatusCode(400);
                exchange.getResponseSender().send("{\"error\": \"Contraseñas no validas.\"}");
                return;
            }

            String dni = userId.substring(6); // quitar "RESET_"
            String hash = HaseaPassword.hashearPassword(password);

            UsuarioDAO usuarioDAO = new UsuarioDAO();
            usuarioDAO.actualizarPassword(dni, hash);

            GestionDeSesionServicio.invalidateSession(sessionId);

            exchange.setStatusCode(200);
            exchange.getResponseSender().send("{\"ok\": true, \"message\": \"Contraseña cambiada correctamente.\"}");
        } catch (Exception e) {
            e.printStackTrace();
            exchange.setStatusCode(500);
            exchange.getResponseSender().send("{\"ok\": false, \"error\": \"" + e.getMessage() + "\"}");
        }
    }
}
