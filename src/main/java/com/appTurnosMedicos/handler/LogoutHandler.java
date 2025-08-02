package com.appTurnosMedicos.handler;
import com.appTurnosMedicos.servicio.GestionDeSesionServicio;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;
import io.undertow.server.handlers.Cookie;
import io.undertow.server.handlers.CookieImpl;

// Un Handler para el logout que se encargaría de la lógica
public class LogoutHandler implements HttpHandler {
    
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        // La lógica que tenías originalmente
        Cookie sessionCookie = exchange.getRequestCookie(GestionDeSesionServicio.getSessionCookieName());
        
        if (sessionCookie != null) {
            GestionDeSesionServicio.invalidateSession(sessionCookie.getValue());

            // Crear una cookie expirada manualmente usando los headers
            String nombreCookie = GestionDeSesionServicio.getSessionCookieName();

            String cookieHeader = new CookieImpl(nombreCookie, "")
                .setPath("/")
                .setMaxAge(0)  // Expira de inmediato
                .setHttpOnly(true)
                .setSecure(false) // Cambiar a true si usás HTTPS en producción
                .toString();

            exchange.getResponseHeaders().add(Headers.SET_COOKIE, cookieHeader);
        }
        
        // Responder al cliente con éxito
        exchange.setStatusCode(StatusCodes.NO_CONTENT); // 204 No Content es una buena práctica para logout
        exchange.endExchange();
    }
}