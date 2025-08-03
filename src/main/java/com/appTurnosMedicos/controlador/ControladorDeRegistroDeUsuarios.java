// Modificado: com.tu_paquete_app.controller.LoginController.java
package com.appTurnosMedicos.controlador;

import com.appTurnosMedicos.modelo.TipoUsuario;
import com.appTurnosMedicos.modelo.Usuario;
import com.appTurnosMedicos.persistencia.TipoUsuarioDAO;
import com.appTurnosMedicos.servicio.AuthServicio;
import com.appTurnosMedicos.servicio.GestionDeSesionServicio; // <-- Importar SessionManager
import com.google.gson.Gson;

import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.BlockingHandler;
import io.undertow.server.handlers.Cookie;
import io.undertow.server.handlers.CookieImpl;
import io.undertow.server.handlers.PathTemplateHandler;
import io.undertow.util.Headers;
import com.appTurnosMedicos.excepciones.*;

import java.sql.SQLException;
import java.util.Deque;
import java.util.List;
import java.util.Map;

public class ControladorDeRegistroDeUsuarios {
    private final AuthServicio authService;

    public ControladorDeRegistroDeUsuarios(AuthServicio authService) {
        this.authService = authService;
    }

    public void registrarRutas(PathTemplateHandler pathTemplateHandler) {
            pathTemplateHandler.add("/registroDeUsuario", new BlockingHandler(exchange -> {
            exchange.getRequestReceiver().receiveFullString(this::handleRegistro);
            
        }));
        pathTemplateHandler.add("/registroDeUsuario/tipos", new BlockingHandler(exchange -> {
            exchange.getRequestReceiver().receiveFullString(this::handleTipos);
        }));
        // Usa BlockingHandler porque las operaciones de DB son bloqueantes. Permite ejecutar operaciones que toman tiempo sin bloquear el servidor principal.
        // exchange: Objeto que contiene todos los detalles de la petición y permite construir la respuesta.
        // getRequestReceiver(): Accede al componente para leer el cuerpo de la petición HTTP.
        // receiveFullString(): Lee el cuerpo completo de la petición (ej. datos de formulario) como un String.
        // loginController::handleLogin: Pasa el cuerpo de la petición al método handleLogin para procesar la lógica de inicio de sesión.

    }
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
    public void handleRegistro(HttpServerExchange exchange, String message) {

    }
    


}