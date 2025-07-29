package com.appTurnosMedicos;

import com.appTurnosMedicos.controlador.ControladorPanelTrabajo;
import com.appTurnosMedicos.controlador.ControladorLogin;
import com.appTurnosMedicos.persistencia.BaseDeDatos;
import com.appTurnosMedicos.persistencia.UsuarioDAO;
import com.appTurnosMedicos.handler.AuthHandler;
import com.appTurnosMedicos.handler.CORSHandler;
import com.appTurnosMedicos.servicio.AuthServicio;
import com.appTurnosMedicos.servicio.GestionDeSesionServicio;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.BlockingHandler;
import io.undertow.server.handlers.CookieImpl;
import io.undertow.server.handlers.Cookie;
import io.undertow.server.handlers.PathTemplateHandler;
import io.undertow.util.Headers;

public class Servidor {

    // Define las URLs de tu frontend estático
    private static final String FRONTEND_ORIGIN = "http://localhost:83"; // O "https://mis-turnos-estaticos.com" en producción
    private static final String FRONTEND_LOGIN_PAGE = FRONTEND_ORIGIN + "/index.html";
    private static final String FRONTEND_DASHBOARD_PAGE = FRONTEND_ORIGIN + "/paciente.html";

    public static void main(String[] args) {
        // --- 1. Inicialización de Dependencias ---
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        AuthServicio authService = new AuthServicio(usuarioDAO);
        ControladorLogin loginController = new ControladorLogin(authService, FRONTEND_DASHBOARD_PAGE, FRONTEND_LOGIN_PAGE);
        ControladorPanelTrabajo panelControlador = new ControladorPanelTrabajo();

        // --- 2. Definición de Rutas de la API con PathTemplateHandler ---
        PathTemplateHandler pathTemplateHandler = new PathTemplateHandler();

        // Ruta de LOGIN (POST)
        // Usa BlockingHandler porque las operaciones de DB son bloqueantes.
        pathTemplateHandler.add("/login", new BlockingHandler(exchange -> {
            exchange.getRequestReceiver().receiveFullString(loginController::handleLogin);
        }));

        // Ruta de DASHBOARD (GET) - PROTEGIDA
        // Envuelve el DashboardController con AuthHandler para la verificación de sesión
        // y luego con BlockingHandler para las operaciones internas.
        pathTemplateHandler.add("/paciente", new BlockingHandler(new AuthHandler(panelControlador::handleDashboard, FRONTEND_LOGIN_PAGE)));

        // Ruta de LOGOUT
        pathTemplateHandler.add( "/logout", new BlockingHandler(exchange -> {
            Cookie sessionCookie = exchange.getRequestCookies().get(GestionDeSesionServicio.getSessionCookieName());
            if (sessionCookie != null) {
                GestionDeSesionServicio.invalidateSession(sessionCookie.getValue());
                // Remover la cookie del navegador estableciendo Max-Age a 0
                Cookie expiredCookie = new CookieImpl(GestionDeSesionServicio.getSessionCookieName(), "")
                                        .setPath("/")
                                        .setMaxAge(0)
                                        .setHttpOnly(true)
                                        .setSecure(false); // Cambiar a true en producción con HTTPS
                exchange.getResponseCookies().put(GestionDeSesionServicio.getSessionCookieName(), expiredCookie);
            }
            // Redirigir siempre a la página de login del frontend
            exchange.setStatusCode(302);
            exchange.getResponseHeaders().put(Headers.LOCATION, FRONTEND_LOGIN_PAGE);
            exchange.endExchange();
        }));

        // --- 3. Aplicar CORSHandler como el handler raíz ---
        // Envuelve todas las rutas de la API con el CORSHandler
        HttpHandler rootHandler = new CORSHandler(pathTemplateHandler, FRONTEND_ORIGIN);

        // --- 4. Construir y Lanzar el Servidor Undertow ---
        Undertow server = Undertow.builder()
            .addHttpListener(8080, "0.0.0.0") // API escuchando en el puerto 8080
            .setHandler(rootHandler) // Usa el CORSHandler como el handler principal
            .build();

        server.start();
        System.out.println("Servidor de API iniciado en http://localhost:8080");
        System.out.println("Asegúrate de que tu frontend esté en: " + FRONTEND_ORIGIN);
        System.out.println("Usuario de prueba: 'test', Contraseña de prueba: '123456' (¡Hashea en DB!)");


        // --- 5. Shutdown Hook para cerrar el Pool de Conexiones ---
        Runtime.getRuntime().addShutdownHook(new Thread(() -> BaseDeDatos.cerrarPool()));
    }
}