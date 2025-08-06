package com.appTurnosMedicos.excepciones;



public class PacienteExisteException extends RuntimeException {
    public PacienteExisteException(String mensaje) {
        super(mensaje);
    }
}