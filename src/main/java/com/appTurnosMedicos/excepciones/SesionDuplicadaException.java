package com.appTurnosMedicos.excepciones;



public class SesionDuplicadaException extends RuntimeException {
    public SesionDuplicadaException(String mensaje) {
        super(mensaje);
    }
}