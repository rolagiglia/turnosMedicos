package com.appTurnosMedicos.modelo;


import java.sql.Date;

public class Paciente {

    private int idPaciente;
    private int dni;
    private String nombrePaciente;
    private String apellidoPaciente;
    private Date fechaNacimiento;
    private String mail;
    private String nacionalidad;
    private String cel;
    private int idUsuario; // Puede ser null
    private Boolean borrado;

    // Constructor vacío
    public Paciente() {
    }

    // Constructor con todos los campos
    public Paciente(int id_paciente, int dni, String nombrePaciente, String apellidoPaciente,
                    Date fechaNacimiento2, String mail, String nacionalidad, String cel,
                    int idUsuario, boolean borrado) {
        this.idPaciente = id_paciente;
        this.dni = dni;
        this.nombrePaciente = nombrePaciente;
        this.apellidoPaciente = apellidoPaciente;
        this.fechaNacimiento = fechaNacimiento2;
        this.mail = mail;
        this.nacionalidad = nacionalidad;
        this.cel = cel;
        this.idUsuario = idUsuario;
        this.borrado = borrado;
    }

    // Getters y Setters
    public int getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    public String getApellidoPaciente() {
        return apellidoPaciente;
    }

    public void setApellidoPaciente(String apellidoPaciente) {
        this.apellidoPaciente = apellidoPaciente;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getCel() {
        return cel;
    }

    public void setCel(String cel) {
        this.cel = cel;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Boolean getBorrado() {
        return borrado;
    }

    public void setBorrado(Boolean borrado) {
        this.borrado = borrado;
    }
}
