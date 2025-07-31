package com.appTurnosMedicos.handler;


import com.appTurnosMedicos.servicio.GestionDeSesionServicio;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.Cookie;
import io.undertow.util.AttachmentKey;
import io.undertow.util.Headers;

public class AuthHandler implements HttpHandler {
    private final HttpHandler next;
    public static final AttachmentKey<String> USER_ID_KEY = AttachmentKey.create(String.class);
    private final String loginPageUrl; // URL de tu página de login estática

    public AuthHandler(HttpHandler next, String loginPageUrl) {
        this.next = next;
        this.loginPageUrl = loginPageUrl;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        Cookie sessionCookie = null;
        String nombreCookie = GestionDeSesionServicio.getSessionCookieName();

        for (Cookie cookie : exchange.requestCookies()) {
            if (cookie.getName().equals(nombreCookie)) {
                sessionCookie = cookie;
                break;
            }
        }


        if (sessionCookie != null) {
            String sessionId = sessionCookie.getValue();
            String userId = GestionDeSesionServicio.getUserId(sessionId);

            if (userId != null) {
                // Sesión válida, adjuntar el ID de usuario a la petición
                exchange.putAttachment(USER_ID_KEY, userId);
                next.handleRequest(exchange); // Continuar con el siguiente handler (ej. DashboardController)
                return;
            }
        }

        // Si no hay cookie de sesión o no es válida
        System.out.println("No se encontró sesión válida.");
        exchange.setStatusCode(401);
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
        String errorJson = "{\"error\": \"Unauthorized\", \"message\": \"Se requiere autenticación para acceder a este recurso.\"}";
        exchange.getResponseSender().send(errorJson); 
        exchange.endExchange();
    }
}