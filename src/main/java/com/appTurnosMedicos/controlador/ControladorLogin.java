// Modificado: com.tu_paquete_app.controller.LoginController.java
package com.appTurnosMedicos.controlador;

import com.appTurnosMedicos.modelo.Usuario;
import com.appTurnosMedicos.servicio.AuthServicio;
import com.appTurnosMedicos.servicio.GestionDeSesionServicio; // <-- Importar SessionManager
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.Cookie;
import io.undertow.server.handlers.CookieImpl;
import io.undertow.util.Headers;


import java.sql.SQLException;
import java.util.Deque;
import java.util.Map;

public class ControladorLogin {
    private final AuthServicio authService;
    private final String dashboardPageUrl; // URL de tu página de dashboard estática
    private final String loginPageUrl;     // URL de tu página de login estática

    public ControladorLogin(AuthServicio authService, String dashboardPageUrl, String loginPageUrl) {
        this.authService = authService;
        this.dashboardPageUrl = dashboardPageUrl;
        this.loginPageUrl = loginPageUrl;
    }

    @SuppressWarnings("removal")
    public void handleLogin(HttpServerExchange exchange, String message) {
        String usuario = null;
        String clave = null;

        // Parsear el cuerpo de la petición (asumiendo application/x-www-form-urlencoded)
        // Undertow tiene un mecanismo para esto, pero para un POST simple, esto funciona:
        if (message != null) {
            Map<String, Deque<String>> queryParameters = exchange.getQueryParameters(); // Si usas GET
            // Para POST, es mejor usar FormParserFactory si esperas form-urlencoded:
            // exchange.startBlocking(); // Necesario antes de parsear el formulario completo
            // FormData parsedData = formParserFactory.createParser(exchange).parseBlocking();
            // usuario = parsedData.getFirst("usuario").getValue();
            // clave = parsedData.getFirst("clave").getValue();

            // O una solución más rudimentaria para el formato "usuario=x&clave=y":
            String[] parts = message.split("&");
            for (String part : parts) {
                String[] keyValue = part.split("=");
                if (keyValue.length == 2) {
                    if ("usuario".equals(keyValue[0])) {
                        usuario = keyValue[1];
                    } else if ("clave".equals(keyValue[0])) {
                        clave = keyValue[1];
                    }
                }
            }
        }

        //SI USUARIO O CLAVE ESTAN VACIOS RESPONDE CON CODIGO DE ERROR 400
        if (usuario == null || clave == null || usuario.isEmpty() || clave.isEmpty()) {
            exchange.setStatusCode(400); // Bad Request
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
            exchange.getResponseSender().send("{\"estado\":\"error\", \"mensaje\":\"Usuario y clave son requeridos.\"}" );
            return;
        }
        
        //SI NO ESTAN VACIOS
        try {
            Usuario usuarioLogueado = authService.verificarCredenciales(usuario, clave);
            if (usuarioLogueado != null) {
                // Login exitoso
                String sessionId = GestionDeSesionServicio.createSession(usuario); // Crear sesión

                // Crear y añadir la cookie de sesión
                Cookie sessionCookie = new CookieImpl(GestionDeSesionServicio.getSessionCookieName(), sessionId)
                                        .setPath("/")
                                        .setHttpOnly(true)
                                        .setSecure(false); // Cambiar a true en producción con HTTPS
                                        // .setMaxAge(3600); // 1 hora de vida de la cookie si no es de sesión (opcional)
                
                //Añade la cookie a la respuesta HTTP
                exchange.getResponseCookies().put(GestionDeSesionServicio.getSessionCookieName(), sessionCookie);

                //AGREGA EL TIPO DE CONTENIDO DE LA RESPUESTA 
                exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
                
                //GENERA EL MENSAJE JSON
                String jsonResponse = "{\"ok\":true,\"redirectUrl\":\"" + dashboardPageUrl + "\", \"userName\":\"" + usuarioLogueado.getUsername() +"\"}";
                
                // Enviar respuesta HTTP CON JSON
                exchange.getResponseSender().send(jsonResponse);

            } else {
                // Login fallido
                exchange.setStatusCode(401); // Unauthorized
                exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
                String jsonResponse = "{\"estado\":\"error\", \"mensaje\":\"Usuario o contraseña inválidas.\"}" ;
                exchange.getResponseSender().send(jsonResponse);
            }
        } catch (SQLException e) {
            System.err.println("Error de base de datos durante el login: " + e.getMessage());
            e.printStackTrace(); // Log completo para depuración del servidor
            exchange.setStatusCode(500); // Internal Server Error
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
            exchange.getResponseSender().send("{\"estado\":\"error\", \"mensaje\":\"Error interno del servidor al procesar el login.\"}" );
        } catch (Exception e) {
             System.err.println("Error inesperado durante el login: " + e.getMessage());
             e.printStackTrace();
             exchange.setStatusCode(500);
             exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
             exchange.getResponseSender().send("{\"estado\":\"error\", \"mensaje\":\"Error interno del servidor.\"}" );
        }
    }
}