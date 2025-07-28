package com.appTurnosMedicos.controlador;

import com.appTurnosMedicos.handler.AuthHandler;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public class ControladorPanelTrabajo {

    public void handleDashboard(HttpServerExchange exchange) {
        // Este handler solo se ejecutará si la autenticación fue correcta
        String userId = exchange.getAttachment(AuthHandler.USER_ID_KEY); // Obtener el ID de usuario del attachment
        exchange.setStatusCode(200);
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/html; charset=UTF-8");
        exchange.getResponseSender().send("<h1>Bienvenido, " + userId + "!</h1><p>Esta es tu página de Dashboard.</p><a href='/logout'>Cerrar Sesión</a>");
    }
}