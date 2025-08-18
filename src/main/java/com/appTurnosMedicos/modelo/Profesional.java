package com.appTurnosMedicos.modelo;

public class Profesional {
    private int id;
    private String nombre_profesional;  
    private String apellido_profesional;
    private int nro_matricula;
    private int id_especialidad;
    private int id_usuario;
    private boolean borrado;

    public Profesional() {
        // Constructor por defecto
    }
    public Profesional(int id, String nombre_profesional, String apellido_profesional, int nro_matricula, int id_especialidad, int id_usuario, boolean borrado) {
        this.id = id;
        this.nombre_profesional = nombre_profesional;
        this.apellido_profesional = apellido_profesional;
        this.nro_matricula = nro_matricula;
        this.id_especialidad = id_especialidad;
        this.id_usuario = id_usuario;
        this.borrado = borrado;
    }

    public Profesional(int id, String nombre_profesional, String apellido_profesional, int nro_matricula, int id_especialidad) {
        this.id = id;
        this.nombre_profesional = nombre_profesional;
        this.apellido_profesional = apellido_profesional;
        this.nro_matricula = nro_matricula;
        this.id_especialidad = id_especialidad;
    }
    public int getId() {
        return id;
    }
    public String getNombre_profesional() {
        return nombre_profesional;
    }
    public String getApellido_profesional() {
        return apellido_profesional;
    }
    public int getNro_matricula() {
        return nro_matricula;
    }
    public int getId_especialidad() {
        return id_especialidad;
    }
    public int getId_usuario() {
        return id_usuario;
    }
    public boolean isBorrado() {
        return borrado;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setNombre_profesional(String nombre_profesional) {
        this.nombre_profesional = nombre_profesional;
    }
    public void setApellido_profesional(String apellido_profesional) {
        this.apellido_profesional = apellido_profesional;
    }
    public void setNro_matricula(int nro_matricula) {
        this.nro_matricula = nro_matricula;
    }
    public void setId_especialidad(int id_especialidad) {
        this.id_especialidad = id_especialidad;
    }
    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }
    public void setBorrado(boolean borrado) {
        this.borrado = borrado;
    }

    
}
