// Nuevo archivo: com.tu_paquete_app.service.SessionManager.java
package com.appTurnosMedicos.servicio;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class GestionDeSesionServicio {
    // Almacenamiento de sesiones: sessionId -> userId (o un objeto de sesión más complejo)
    private static final Map<String, String> activeSessions = new ConcurrentHashMap<>();
    private static final String SESSION_COOKIE_NAME = "JSESSIONID";

    /**
     * Crea una nueva sesión para un usuario.
     * @param userId El ID del usuario autenticado.
     * @return El ID de la sesión recién creada.
     */
    public static String createSession(String userId) {
        String sessionId = UUID.randomUUID().toString();
        activeSessions.put(sessionId, userId);
        System.out.println("Sesión creada para usuario " + userId + ": " + sessionId);
        return sessionId;
    }

    /**
     * Valida si un ID de sesión existe y retorna el ID de usuario asociado.
     * @param sessionId El ID de sesión del cliente.
     * @return El ID de usuario si la sesión es válida, null en caso contrario.
     */
    public static String getUserId(String sessionId) {
        return activeSessions.get(sessionId);
    }

    /**
     * Invalida una sesión.
     * @param sessionId El ID de sesión a invalidar.
     */
    public static void invalidateSession(String sessionId) {
        activeSessions.remove(sessionId);
        System.out.println("Sesión invalidada: " + sessionId);
    }

    public static String getSessionCookieName() {
        return SESSION_COOKIE_NAME;
    }
}