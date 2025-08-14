// Nuevo archivo: com.tu_paquete_app.service.SessionManager.java
package com.appTurnosMedicos.servicio;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import com.appTurnosMedicos.excepciones.*;

import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.Cookie;

public class GestionDeSesionServicio {
    private static final Map<String, SesionInfo> activeSessions = new ConcurrentHashMap<>();
    private static final Map<String, String> userIdToSessionId = new ConcurrentHashMap<>();
    private static final String SESSION_COOKIE_NAME = "JSESSIONID";
    public static final long TIEMPO_MAX_INACTIVIDAD_MS = 1 * 60 * 1000; // 1 minuto

    private static class SesionInfo {
        private final String userId;
        private volatile long ultimaActividad;

        public SesionInfo(String userId) {
            this.userId = userId;
            this.ultimaActividad = System.currentTimeMillis();
        }

        public String getUserId() {
            return userId;
        }

        public long getUltimaActividad() {
            return ultimaActividad;
        }

        public void actualizarActividad() {
            this.ultimaActividad = System.currentTimeMillis();
        }
    }

    /**
     * Crea una nueva sesión para un usuario.
     * Si ya tenía una sesión activa, la invalida.
     * @param userId El ID del usuario autenticado.
     * @return El ID de la sesión recién creada.
     */
    public static synchronized String createSession(String userId) {
        // Si el usuario ya tiene sesión activa la inactivamos//////////////////////////////////////////
        if (userIdToSessionId.containsKey(userId)) {
            invalidateSession(userIdToSessionId.get(userId));
        }

        String sessionId = UUID.randomUUID().toString();
        activeSessions.put(sessionId, new SesionInfo(userId));
        userIdToSessionId.put(userId, sessionId);
        System.out.println("Sesión creada para usuario " + userId + ": " + sessionId);
        return sessionId;
    }

    public static String getUserId(String sessionId) {
        SesionInfo sesion = activeSessions.get(sessionId);

        if (sesion != null) {
            long ahora = System.currentTimeMillis();
            long inactivo = ahora - sesion.getUltimaActividad();

            if (inactivo > TIEMPO_MAX_INACTIVIDAD_MS) {
                // Sesión expirada
                activeSessions.remove(sessionId);
                userIdToSessionId.remove(sesion.getUserId());
                System.out.println("Sesión expirada: " + sessionId);
                return null;
            } else {
                sesion.actualizarActividad();
                return sesion.getUserId();
            }
        }

        return null;
    }

    public static void invalidateSession(String sessionId) {
        SesionInfo sesion = activeSessions.remove(sessionId);
        if (sesion != null) {
            userIdToSessionId.remove(sesion.getUserId());
        }
        System.out.println("Sesión invalidada: " + sessionId);
    }

    public static String getSessionCookieName() {
        return SESSION_COOKIE_NAME;
    }
    public static String getSessionIdFromCookie(HttpServerExchange exchange) {
        Cookie cookie = exchange.getRequestCookie(SESSION_COOKIE_NAME);
        return (cookie != null) ? cookie.getValue() : null;
    }
}
