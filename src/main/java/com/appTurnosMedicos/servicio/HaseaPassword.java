package com.appTurnosMedicos.servicio;

import org.mindrot.jbcrypt.BCrypt;

public final class HaseaPassword {
    
    public static String hashearPassword(String password) {
        String passwordHasheada = BCrypt.hashpw(password, BCrypt.gensalt());
        return passwordHasheada; 
    }
}
