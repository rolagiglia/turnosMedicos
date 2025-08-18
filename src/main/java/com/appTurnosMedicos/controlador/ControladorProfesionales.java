package com.appTurnosMedicos.controlador;

import java.util.List;
import java.util.Map;

import com.appTurnosMedicos.handler.AuthHandler;
import com.appTurnosMedicos.modelo.Especialidad;
import com.appTurnosMedicos.modelo.Profesional;
import com.appTurnosMedicos.servicio.CatalogoDeProfesionales;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.BlockingHandler;
import io.undertow.server.handlers.PathTemplateHandler;
import io.undertow.util.Headers;

public class ControladorProfesionales {
       public void registrarRutas(PathTemplateHandler pathTemplateHandler, String frontendLoginPage) {
        // Ruta para obtener profesionales
        pathTemplateHandler.add("/paciente/obtenerProfesionales", new BlockingHandler(new AuthHandler(this::handleProfesionales, frontendLoginPage)
        ));
    }


        public void handleProfesionales(HttpServerExchange exchange) {
            // Aquí se manejaría la lógica para obtener los profesionales
            // Por ejemplo, podrías consultar una base de datos y devolver un JSON con la lista de profesionales
            CatalogoDeProfesionales catalogo = new CatalogoDeProfesionales();
            // Llamar al método del DAO para obtener los profesionales  
            System.out.println("Obteniendo profesionales...");
            Map<Especialidad, List<Profesional>> mapa = null;
            
            try{
                mapa = catalogo.obtenerProfesionales();
                if (mapa == null || mapa.isEmpty()) {
                exchange.setStatusCode(404);
                exchange.getResponseSender().send("{\"error\": \"No se encontraron profesionales.\"}");
                return;
                }
                // Convertir a JSON
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(mapa);
                System.out.println("Profesionales obtenidos: " + json);
                
                exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
                exchange.setStatusCode(200);
                exchange.getResponseSender().send(json);
                } catch (Exception e) {
                    System.err.println("Error al obtener profesionales: " + e.getMessage());
                    exchange.setStatusCode(500);
                    exchange.getResponseSender().send("{\"error\": \"Error al obtener profesionales.\"}");
                    return;
                }
            
            
            
        }
        
}



