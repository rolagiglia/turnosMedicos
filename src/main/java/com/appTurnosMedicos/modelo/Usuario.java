package com.appTurnosMedicos.modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

// Puedes añadir más campos según tu tabla de usuarios
public class Usuario {
    private int id;
    private String usuario;
    private String password_hash; // Nunca la contraseña en texto plano
    private String tipo_usuario;
    private Date fecha_creacion;

    public Usuario(int id, String username, String passwordHash, Date fecha_creacion, String tipo_usuario) {
        this.id = id;
        this.usuario = username;
        this.password_hash = passwordHash;
        this.fecha_creacion = fecha_creacion;
        this.tipo_usuario = tipo_usuario;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return usuario; }
    public void setUsername(String username) { this.usuario = username; }
    public String getPasswordHash() { return password_hash; }
    public void setPasswordHash(String passwordHash) { this.password_hash = passwordHash; }
    public Date getFechaCreacion(){return fecha_creacion;}
}