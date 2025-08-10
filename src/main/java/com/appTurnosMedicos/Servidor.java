package com.appTurnosMedicos;

import com.appTurnosMedicos.controlador.*;
import com.appTurnosMedicos.persistencia.BaseDeDatos;
import com.appTurnosMedicos.persistencia.UsuarioDAO;
import com.appTurnosMedicos.handler.CORSHandler;
import com.appTurnosMedicos.handler.LogoutHandler;
import com.appTurnosMedicos.servicio.AuthServicio;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathTemplateHandler;

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
        ControladorPaciente pacienteControlador = new ControladorPaciente();
        ControladorDeRegistroDePacientes registroDePacientesControlador = new ControladorDeRegistroDePacientes();
        ControladorRecuperoDePass recuperoDePassControlador = new ControladorRecuperoDePass();
        
        // --- 2. Definición de Rutas de la API con PathTemplateHandler ---
        PathTemplateHandler pathTemplateHandler = new PathTemplateHandler();


        // Delegar el registro de rutas
        loginController.registrarRutas(pathTemplateHandler);
        pacienteControlador.registrarRutas(pathTemplateHandler, FRONTEND_LOGIN_PAGE);
        registroDePacientesControlador.registrarRutas(pathTemplateHandler);
        recuperoDePassControlador.registrarRutas(pathTemplateHandler);
        
        
        
        pathTemplateHandler.add("/logout", new LogoutHandler());
        // Se usa un HttpHandler simple en lugar de un controlador porque el logout solo necesita invalidar la sesión,
        // eliminar la cookie y responder sin lógica compleja ni acceso a base de datos.
        



        // --- 3. Aplicar CORSHandler como el handler raíz ---
        // Envuelve todas las rutas de la API con el CORSHandler
        //  si el frontend y backend están en orígenes distintos, necesitas CORSHandler para que el navegador permita la comunicación.
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