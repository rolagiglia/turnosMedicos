package com.appTurnosMedicos.modelo;

public class Especialidad {
    private int id_especialidad;
    private String nombre_especialidad;
    private boolean borrado;

    public Especialidad(int id_especialidad, String nombre_especialidad) {
        this.id_especialidad = id_especialidad;
        this.nombre_especialidad = nombre_especialidad;
        this.borrado = false; // Por defecto, no está borrada
    }

    public int getId_especialidad() {
        return id_especialidad;
    }
    public String getNombre_especialidad() {
        return nombre_especialidad;
    }
    public boolean isBorrado() {
        return borrado;
    }
    public void setId_especialidad(int id_especialidad) {
        this.id_especialidad = id_especialidad;
    }
    public void setNombre_especialidad(String nombre_especialidad) {
        this.nombre_especialidad = nombre_especialidad;
    }
    public void setBorrado(boolean borrado) {
        this.borrado = borrado;
    }
    @Override
    public String toString() {
        return "Especialidad{" +
                "id_especialidad=" + id_especialidad +
                ", nombre_especialidad='" + nombre_especialidad + '\'' +
                ", borrado=" + borrado +
                '}';
    }
}
