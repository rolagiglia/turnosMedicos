package com.appTurnosMedicos.excepciones;

public class PacienteNoExisteException extends RuntimeException {

    public PacienteNoExisteException(String mensaje) {
        super(mensaje);
        }
    }