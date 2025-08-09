
package com.appTurnosMedicos.controlador;


import com.appTurnosMedicos.modelo.PacienteConPass;
import com.appTurnosMedicos.persistencia.PacienteDAO;
import com.appTurnosMedicos.persistencia.UsuarioDAO;
import com.appTurnosMedicos.servicio.RegistroPaciente;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.undertow.server.handlers.BlockingHandler;
import io.undertow.server.handlers.PathTemplateHandler;



public class ControladorDeRegistroDePacientes {


    public void registrarRutas(PathTemplateHandler pathTemplateHandler) {
            pathTemplateHandler.add("/paciente/registroDeUsuario", new BlockingHandler(exchange -> {
            exchange.getRequestReceiver().receiveFullString(this::handleRegistro);
            
        }));
        
    }

    public void handleRegistro(HttpServerExchange exchange, String jsonBody) {
        // Indicamos a Undertow que maneje la solicitud de forma bloqueante
        // para poder leer el cuerpo del POST completo
    exchange.startBlocking();

    if (jsonBody != null && !jsonBody.trim().isEmpty()) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            PacienteConPass pacienteConPass = new PacienteConPass();
            pacienteConPass = objectMapper.readValue(jsonBody, PacienteConPass.class);
            //Verificar si existe en la base de datos y sino agregarlo y crear el usr y pass
            //Delegamos la tarea a un servicio
            RegistroPaciente registroDePaciente = new RegistroPaciente(new PacienteDAO(), new UsuarioDAO());
            registroDePaciente.registraPaciente(pacienteConPass);
            exchange.setStatusCode(200);
            // Enviar respuesta JSON OK
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
            String jsonResponse = "{\"ok\":true }";
            exchange.getResponseSender().send(jsonResponse);
        } catch (Exception e) {
            exchange.setStatusCode(500);
            exchange.getResponseSender().send("{\"error\": \" " + e.getMessage() + "\"}");
            e.printStackTrace();
        }
    } else {
        exchange.setStatusCode(500);
        exchange.getResponseSender().send("{\"error\": \"Error datos formulario.\"}");
    }

}



    /*
    public void handleTipos(HttpServerExchange exchange, String message) {
        TipoUsuarioDAO tipoUsuarioDAO = new TipoUsuarioDAO();
        try {
            // 1. Obtener los tipos de usuario de la base de datos
            List<TipoUsuario> tipos = tipoUsuarioDAO.obtenerTipos();
            
            // 2. Convertir la lista de objetos a formato JSON
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(tipos);
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
            // 3. Enviar la respuesta JSON al cliente
            exchange.getResponseSender().send(jsonResponse);
            
        } catch (Exception e) {
            // Manejo de errores
            exchange.setStatusCode(500); // Internal Server Error
           String jsonResponse = "{\"estado\":\"error\", \"mensaje\":\"Error del servidor al obtener los tipos.\"}" ;
            exchange.getResponseSender().send(jsonResponse);
        }
    }
    */
    


}