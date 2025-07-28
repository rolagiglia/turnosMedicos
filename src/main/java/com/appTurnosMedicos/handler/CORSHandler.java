package com.appTurnosMedicos.handler;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.Methods;

public class CORSHandler implements HttpHandler {
    private final HttpHandler next;
    private final String allowedOrigin; // El dominio de tu servidor estático

    public CORSHandler(HttpHandler next, String allowedOrigin) {
        this.next = next;
        this.allowedOrigin = allowedOrigin;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        // Establecer las cabeceras CORS para todas las respuestas
        exchange.getResponseHeaders().put(Headers.ACCESS_CONTROL_ALLOW_ORIGIN, allowedOrigin);
        exchange.getResponseHeaders().put(Headers.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().put(Headers.ACCESS_CONTROL_ALLOW_HEADERS, "Content-Type, Authorization"); // Si usas tokens JWT o headers personalizados
        exchange.getResponseHeaders().put(Headers.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true"); // Si usas cookies de sesión o autenticación HTTP

        // Manejar solicitudes preflight (OPTIONS)
        if (exchange.getRequestMethod().equals(Methods.OPTIONS)) {
            exchange.setStatusCode(204); // No Content
            exchange.endExchange();
            return;
        }

        next.handleRequest(exchange);
    }
}

