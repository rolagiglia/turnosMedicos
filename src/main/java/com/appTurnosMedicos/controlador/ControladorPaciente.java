package com.appTurnosMedicos.controlador;

import com.appTurnosMedicos.handler.AuthHandler;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public class ControladorPaciente {

    public void handlePaciente(HttpServerExchange exchange) {
        // Este handler solo se ejecutará si la autenticación fue correcta
        String userId = exchange.getAttachment(AuthHandler.USER_ID_KEY); // Obtener el ID de usuario del attachment
        exchange.setStatusCode(200);
        // Enviar respuesta JSON con URL para redirigir
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
        String jsonResponse = "{\"ok\":true, \"userName\":\"" + userId +"\"}";
        exchange.getResponseSender().send(jsonResponse);
    }
}