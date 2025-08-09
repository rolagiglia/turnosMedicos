package com.appTurnosMedicos.modelo;

import java.util.Date;

public class PacienteConPass {
    private int dni;
    private String nombre_paciente;
    private String apellido_paciente;
    private Date fecha_nacimiento;
    private String mail;
    private String nacionalidad;
    private String cel;
    private String password;

    public void setDni(int dni) {
        this.dni = dni;
    }
    public void setNombre_paciente(String nombre_paciente) {
        this.nombre_paciente = nombre_paciente;
    }
    public void setApellido_paciente(String apellido_paciente) {
        this.apellido_paciente = apellido_paciente;
    }
    public void setFecha_nacimiento(java.util.Date fecha) {
        this.fecha_nacimiento = fecha;
    }
    public void setMail(String mail) {
        this.mail = mail;
    }
    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }
    public void setCel(String cel) {
        this.cel = cel;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    @Override
    public String toString() {
        return "PacienteConPass [dni=" + dni + ", nombre_paciente=" + nombre_paciente + ", apellido_paciente="
                + apellido_paciente + ", fecha_nacimiento=" + fecha_nacimiento + ", mail=" + mail + ", nacionalidad="
                + nacionalidad + ", cel=" + cel + ", password=" + password + "]";
    }
    public PacienteConPass() {
        
    }
     public int getDni() {
        return dni;
    }


    public String getNombrePaciente() {
        return nombre_paciente;
    }


    public String getApellidoPaciente() {
        return apellido_paciente;
    }


    public Date getFechaNacimiento() {
        return fecha_nacimiento;
    }


    public String getMail() {
        return mail;
    }


    public String getNacionalidad() {
        return nacionalidad;
    }


    public String getCel() {
        return cel;
    }


    public String getPass() {
        return password;
    }


    
}
